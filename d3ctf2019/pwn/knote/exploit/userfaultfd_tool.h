/*
 * Author: Ex
 * Time: 2019-11-27
 * Email: 2462148389@qq.com
 **/

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
#define __u8 uint8_t
#define __s64 int64_t

#define UFFDIO_API 0xc018aa3f
#define UFFDIO_REGISTER 0xc020aa00
#define UFFDIO_COPY 0xc028aa03

#define UFFD_API ((__u64)0xAA)
#define __NR_userfaultfd 323
struct uffdio_range
{
    __u64 start;
    __u64 len;
};
struct uffdio_copy
{
    __u64 dst;
    __u64 src;
    __u64 len;

#define UFFDIO_COPY_MODE_DONTWAKE ((__u64)1 << 0)
    __u64 mode;
    __s64 copy;
};

struct uffd_msg
{
    __u8 event;

    __u8 reserved1;
    __u16 reserved2;
    __u32 reserved3;

    union {
        struct
        {
            __u64 flags;
            __u64 address;
            union {
                __u32 ptid;
            } feat;
        } pagefault;

        struct
        {
            __u32 ufd;
        } fork;

        struct
        {
            __u64 from;
            __u64 to;
            __u64 len;
        } remap;

        struct
        {
            __u64 start;
            __u64 end;
        } remove;

        struct
        {
            /* unused reserved fields */
            __u64 reserved1;
            __u64 reserved2;
            __u64 reserved3;
        } reserved;
    } arg;
} __packed;

struct uffdio_api
{
    __u64 api;

#define UFFD_FEATURE_PAGEFAULT_FLAG_WP (1 << 0)
#define UFFD_FEATURE_EVENT_FORK (1 << 1)
#define UFFD_FEATURE_EVENT_REMAP (1 << 2)
#define UFFD_FEATURE_EVENT_REMOVE (1 << 3)
#define UFFD_FEATURE_MISSING_HUGETLBFS (1 << 4)
#define UFFD_FEATURE_MISSING_SHMEM (1 << 5)
#define UFFD_FEATURE_EVENT_UNMAP (1 << 6)
#define UFFD_FEATURE_SIGBUS (1 << 7)
#define UFFD_FEATURE_THREAD_ID (1 << 8)
    __u64 features;

    __u64 ioctls;
};

struct uffdio_register
{
    struct uffdio_range range;
#define UFFDIO_REGISTER_MODE_MISSING ((__u64)1 << 0)
#define UFFDIO_REGISTER_MODE_WP ((__u64)1 << 1)
    __u64 mode;
    __u64 ioctls;
};

/*
 * Start at 0x12 and not at 0 to be more strict against bugs.
 */
#define UFFD_EVENT_PAGEFAULT 0x12
#define UFFD_EVENT_FORK 0x13
#define UFFD_EVENT_REMAP 0x14
#define UFFD_EVENT_REMOVE 0x15
#define UFFD_EVENT_UNMAP 0x16
#else
#include <linux/userfaultfd.h>
#endif

/*
 * Default padding address, the exception handle will copy following memory into fault_page when page fault exception happened.
 **/
#define PAGE_COPY_ADDR (void *)0x1234000
#define FAULT_PAGE_ADDR (void *)0x5678000

/*
 * If result isn't the expected value, then print error information and exit process.
 **/
#define ASSERT(value, expected, error_info)                                    \
    if ((value) != (expected))                                                 \
    {                                                                          \
        fprintf(stderr, "%s  %m  at %s:%d\n", error_info, __FILE__, __LINE__); \
        exit(EXIT_FAILURE);                                                    \
    }

#include <semaphore.h>
sem_t fault_sem;

static int page_size;

/*
 * Get a page that bind the exception handle.
 * If program read or write this page, then the exception handle will
 * interrupt operation with a thread lock until the function release_fault_page
 * be called. In the meantime, you have plenty of time to do what you want to do.
 * 
 * You can also set the hook to accomplish your exploit.
 **/
void *get_userfault_page(int num_pages);

/*
 * Default fcuntion, it is used to send semaphore.
 **/
void release_fault_page();

/*
 * When program happen page fault exception, it will call following hook in advance.
 * Default it is used to wait semaphore.
 **/
extern void (*require_fault_hook)(void);

/*
 * It is a hook in get_userfault_page, here it is used to initial semaphore default.
 **/
extern void (*init_fault_hook)(void);

/*
 * It can run your funtion with 5 parameters at most in a new thread.
 * If you don't want the compiler show the warnnings, then use macro RUN_JOB.
 * Note: I only write x64 version.
 **/
pthread_t run_job(void (*job)(), ...);
/*
 * It can run your funtion with 5 parameters at most in a new thread.
 * Note: It must have two parameter at least, or some troubles will happen.
 **/
#define RUN_JOB(job, ...)                      \
    {                                          \
        run_job((void (*)())job, __VA_ARGS__); \
    }

/*
 * The tool can print memory like hexdump,
 * mode 0 => only print BYTE
 * mode 1 => BYTE on left, and unsigned long long on right with hex format.
 **/
void print_hex(unsigned char *addr, int size, int mode);

/*
 * Log variable with hex format.
 **/
#define LOGV(variable)                           \
    {                                            \
        printf("" #variable ": 0x%llx (%llu)\n", \
               (unsigned long long)(variable),   \
               (unsigned long long)(variable));  \
    }


#define DEFAULT_STATIC_KERNER_BASE 0xffffffff81000000
/*
 * In short, it can calculate real offset with static address.
 **/
#define REAL_OFFSET(addr) ((addr) - (DEFAULT_STATIC_KERNER_BASE))

#endif