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

#include "userfaultfd_tool.h"

sem_t done;

void fault_init(void) __attribute__((constructor));

void fault_init()
{
    require_fault_hook = require_fault_page;
    init_fault_hook = init_fault_page;
}

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
        page = mmap(PAGE_COPY_ADDR, page_size, PROT_READ | PROT_WRITE,
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

        sem_post(&done);
        pollfd.fd = uffd;
        pollfd.events = POLLIN;
        nready = poll(&pollfd, 1, -1);
        if (nready == -1)
            errExit("poll");

        // wait
        if(require_fault_hook)
        {
            require_fault_hook();
        }

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
        fault_cnt++;

        uffdio_copy.src = (unsigned long) page;

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

void *get_userfault_page(int num_pages)
{
    long uffd;          /* userfaultfd file descriptor */
    char *addr;         /* Start of region handled by userfaultfd */
    unsigned long len;  /* Length of region handled by userfaultfd */
    pthread_t thr;      /* ID of thread that handles page faults */
    struct uffdio_api uffdio_api;
    struct uffdio_register uffdio_register;
    int s;

    if(init_fault_hook)
    {
        init_fault_hook();
    }

    page_size = sysconf(_SC_PAGE_SIZE);
    len = num_pages * page_size;

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

    addr = mmap(FAULT_PAGE_ADDR, len, PROT_READ | PROT_WRITE,
                MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
    if (addr == MAP_FAILED)
        errExit("mmap");

    // printf("Address returned by mmap() = %p\n", addr);

    /* Register the memory range of the mapping we just created for
        handling by the userfaultfd object. In mode, we request to track
        missing pages (i.e., pages that have not yet been faulted in). */

    uffdio_register.range.start = (unsigned long) addr;
    uffdio_register.range.len = len;
    uffdio_register.mode = UFFDIO_REGISTER_MODE_MISSING;
    if (ioctl(uffd, UFFDIO_REGISTER, &uffdio_register) == -1)
        errExit("ioctl-UFFDIO_REGISTER");

    /* Create a thread that will process the userfaultfd events */

    s = pthread_create(&thr, NULL, fault_handler_thread, (void *) uffd);
    if (s != 0) {
        errno = s;
        errExit("pthread_create");
    }

    /* Main thread now touches memory in the mapping, touching
        locations 1024 bytes apart. This will trigger userfaultfd
        events for all pages in the region. */

    // int l;
    // l = 0xf;    /* Ensure that faulting address is not on a page
    //                 boundary, in order to test that we correctly
    //                 handle that case in fault_handling_thread() */
    // while (l < len) {
    //     char c = addr[l];
    //     printf("Read address %p in main(): ", addr + l);
    //     printf("%c\n", c);
    //     l += 1024;
    //     usleep(100000);         /* Slow things down a little */
    // }

    // exit(EXIT_SUCCESS);
    sem_wait(&done);
    return (void *)addr;
}

void release_fault_page()
{
    sem_post(&fault_sem);
    usleep(100 * 1000);
}

void require_fault_page()
{
    puts("fault_sem lock");
    sem_wait(&fault_sem);
    puts("fault_sem release");
}

void init_fault_page()
{
    sem_init(&fault_sem,0,0);
}

void print_hex(unsigned char *addr, int size, int mode)
{
    int i, ii;
    unsigned long long temp;
    switch(mode)
    {
    case 0:
        for(i = 0; i < size; )
        {
            for(ii = 0; i < size && ii < 8; i++, ii++)
            {
                printf("%02X ", addr[i]);
            }
            printf("    ");
            for(ii = 0; i < size && ii < 8; i++, ii++)
            {
                printf("%02X ", addr[i]);
            }
            puts("");
        }
        break;

    case 1:
        for(i = 0; i < size; )
        {
            temp = *(unsigned long long *)(addr + i);
            for(ii = 0; i < size && ii < 8; i++, ii++)
            {
                printf("%02X ", addr[i]);
            }
            printf("    ");
            printf("0x%llx\n", temp);
        }
        break;
    }
    
}