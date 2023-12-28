
## checkin

```
Arch:     amd64-64-little
RELRO:    Partial RELRO
Stack:    No canary found
NX:       NX enabled
PIE:      PIE enabled
```

Basic amd64 alphanumeric shellcode challenge

```c
int __fastcall main()
{
  __int64 v0; // rbx
  __int64 v1; // rbx
  __int64 v2; // rbx
  unsigned __int64 v4; // [rsp+8h] [rbp-28h]
  char *addr; // [rsp+10h] [rbp-20h]
  int i; // [rsp+1Ch] [rbp-14h]

  addr = (char *)mmap((void *)0x20230000, 0x1000uLL, 7, 34, -1, 0LL);
  if ( addr == (char *)-1LL )
  {
    perror("mmap");
    exit(EXIT_FAILURE);
  }
  write(STDOUT_FILENO, "Give me your shellcode: ", 24uLL);
  v4 = read(STDIN_FILENO, addr + 0x30, 0x100uLL);
  for ( i = 0; i < v4; ++i )
  {
    if ( (addr[i + 48] <= '`' || addr[i + 48] > 'z')
      && (addr[i + 48] <= '@' || addr[i + 48] > 'Z')
      && (addr[i + 48] <= '/' || addr[i + 48] > '9')
      && addr[i + 48] != '/' )
    {
      printf("Invalid character: %c\n", (unsigned int)addr[i]);
      exit(1);
    }
  }
  v0 = qword_4088;
  *(_QWORD *)addr = payload;
  *((_QWORD *)addr + 1) = v0;
  v1 = qword_4098;
  *((_QWORD *)addr + 2) = qword_4090;
  *((_QWORD *)addr + 3) = v1;
  v2 = qword_40A8;
  *((_QWORD *)addr + 4) = qword_40A0;
  *((_QWORD *)addr + 5) = v2;
  sandbox();
  ((void (*)(void))addr)();
  return 0;
}
```

Exploitation:

Utilize the offset of the entry address on the stack, and set RAX to the entry address. Subsequently, generate the shellcode directly using Alpha3.
