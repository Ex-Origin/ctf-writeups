#!/usr/bin/python
# -*- coding:utf-8 -*-

import re

s = '''
   00000008    0000003B    00000001    00000020
   00000007    00000034    00000009    0000001F
   00000018    00000024    00000013    00000003
   00000010    00000038    00000009    0000001B
   00000008    00000034    00000013    00000002
   00000008    00000022    00000012    00000003
   00000005    00000006    00000012    00000003
   0000000F    00000022    00000012    00000017
   00000008    00000001    00000029    00000022
   00000006    00000024    00000032    00000024
   0000000F    0000001F    0000002B    00000024
   00000003    00000015    00000041    00000041


'''

def indexOf(str, ch):
    i = 0
    for v in str:
        if (v == ch):
            return i
        i+=1

l = re.findall('[\dA-F]+', s)
l = [int(v, 16) for v in l]

d  = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/='
d2 = '0123456789QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm+/='

o = ''

for v in l:
    o += d[v - 1]

print(o)

o2 = ''

for v in o:
    o2 += d[indexOf(d2, v)]

print(o2)



