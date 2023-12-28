
## nception

In the context of a stack overflow vulnerability in the "edit" function, allowing control over exception handling, it becomes possible to manipulate the value of RBP. Moreover, within the catch block, the address 0000000000402E2C performs a write operation on the memory pointed to by RBP. Hence, leveraging this characteristic, one can modify the lower bits of the "ptrs" pointer to position it under control. This manipulation facilitates arbitrary address read and write operations.

```
.text:0000000000402E16 ;   catch(std::exception) // owned by 402DC0
.text:0000000000402E16                 cmp     rdx, 1
.text:0000000000402E1A                 jz      short loc_402E24
.text:0000000000402E1C                 mov     rdi, rax        ; struct _Unwind_Exception *
.text:0000000000402E1F                 call    __Unwind_Resume
.text:0000000000402E24 ; ---------------------------------------------------------------------------
.text:0000000000402E24
.text:0000000000402E24 loc_402E24:                             ; CODE XREF: main+11D↑j
.text:0000000000402E24                 mov     rdi, rax        ; void *
.text:0000000000402E27                 call    ___cxa_begin_catch
.text:0000000000402E2C                 mov     [rbp+e], rax
```
