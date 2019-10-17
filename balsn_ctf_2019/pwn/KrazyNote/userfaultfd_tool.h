#ifndef USERFAULTFD_TOOL
#define USERFAULTFD_TOOL

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

// #define MUSL_GCC

#ifdef MUSL_GCC
#define __u64 uint64_t
#define __u32 uint32_t
#define __u16 uint16_t
#define __u8  uint8_t
#define __s64 int64_t

#define UFFDIO_API 0xc018aa3f
#define UFFDIO_REGISTER 0xc020aa00
#define UFFDIO_COPY 0xc028aa03

#define UFFD_API ((__u64)0xAA)      
#define __NR_userfaultfd 323 
struct uffdio_range {
    __u64 start;
    __u64 len;
};
struct uffdio_copy {
    __u64 dst;
    __u64 src;
    __u64 len;

#define UFFDIO_COPY_MODE_DONTWAKE        ((__u64)1<<0)
    __u64 mode;
    __s64 copy;
};

struct uffd_msg {
    __u8    event;

    __u8    reserved1;
    __u16    reserved2;
    __u32    reserved3;

    union {
        struct {
            __u64    flags;
            __u64    address;
            union {
                __u32 ptid;
            } feat;
        } pagefault;

        struct {
            __u32    ufd;
        } fork;

        struct {
            __u64    from;
            __u64    to;
            __u64    len;
        } remap;

        struct {
            __u64    start;
            __u64    end;
        } remove;

        struct {
            /* unused reserved fields */
            __u64    reserved1;
            __u64    reserved2;
            __u64    reserved3;
        } reserved;
    } arg;
} __packed;



struct uffdio_api {
    __u64 api;

#define UFFD_FEATURE_PAGEFAULT_FLAG_WP        (1<<0)
#define UFFD_FEATURE_EVENT_FORK            (1<<1)
#define UFFD_FEATURE_EVENT_REMAP        (1<<2)
#define UFFD_FEATURE_EVENT_REMOVE        (1<<3)
#define UFFD_FEATURE_MISSING_HUGETLBFS        (1<<4)
#define UFFD_FEATURE_MISSING_SHMEM        (1<<5)
#define UFFD_FEATURE_EVENT_UNMAP        (1<<6)
#define UFFD_FEATURE_SIGBUS            (1<<7)
#define UFFD_FEATURE_THREAD_ID            (1<<8)
    __u64 features;

    __u64 ioctls;
};

struct uffdio_register {
    struct uffdio_range range;
#define UFFDIO_REGISTER_MODE_MISSING    ((__u64)1<<0)
#define UFFDIO_REGISTER_MODE_WP        ((__u64)1<<1)
    __u64 mode;
    __u64 ioctls;
};       

/*
 * Start at 0x12 and not at 0 to be more strict against bugs.
 */
#define UFFD_EVENT_PAGEFAULT	0x12
#define UFFD_EVENT_FORK		0x13
#define UFFD_EVENT_REMAP	0x14
#define UFFD_EVENT_REMOVE	0x15
#define UFFD_EVENT_UNMAP	0x16
#else
#include <linux/userfaultfd.h>
#endif

#define PAGE_COPY_ADDR (void *)0x1234000
#define FAULT_PAGE_ADDR (void *)0x5678000

#define errExit(msg)        \
    do                      \
    {                       \
        perror(msg);        \
        exit(EXIT_FAILURE); \
    } while (1);

#include <semaphore.h>
sem_t fault_sem;

static int page_size;


void *get_userfault_page(int num_pages);

void release_fault_page();
void require_fault_page();
void init_fault_page();
void (*require_fault_hook)(void);
void (*init_fault_hook)(void);

void print_hex(unsigned char *addr, int size, int mode);



#endif