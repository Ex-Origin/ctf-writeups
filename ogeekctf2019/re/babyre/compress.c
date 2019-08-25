// gcc -s -O3 compress.c -o compress
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct out_container
{
    FILE *out_fp;
    int field_8;
    int field_C;
    int amount;
} out_container;

void push_one_bit(out_container *a1, int a2)
{
    int v2; // ebx

    if (a2)
        *(unsigned char *)&(a1->field_8) |= *(unsigned char *)&(a1->field_C);
    *(unsigned char *)&(a1->field_C) >>= 1;
    if (!*(unsigned char *)&(a1->field_C))
    {
        v2 = a1->field_8;
        if (v2 == _IO_putc(a1->field_8, a1->out_fp))
            ++a1->amount;
        else
            puts("write fail.");
        a1->field_8 = 0;
        *(unsigned char *)&(a1->field_C) = 0x80u;
    }
}

#define _WORD short

void push_n_bits(out_container *a1, size_t a2, char a3)
{
    int v3;     // ebx
    unsigned i; // [rsp+28h] [rbp-18h]

    for (i = 1 << (a3 - 1); i; i >>= 1)
    {
        if (a2 & i)
            *(unsigned char *)&(a1->field_8) |= *(unsigned char *)&(a1->field_C);
        *(unsigned char *)&(a1->field_C) >>= 1;
        if (!*(unsigned char *)&(a1->field_C))
        {
            v3 = a1->field_8;
            if (v3 == _IO_putc(a1->field_8, a1->out_fp))
                ++a1->amount;
            else
                puts("write fail.");
            a1->field_8 = 0;
            *(unsigned char *)&(a1->field_C) = -128;
        }
    }
}

void compress(_IO_FILE *in_fp, out_container *out)
{
    signed int i;   // [rsp+1Ch] [rbp-24h]
    signed int j;   // [rsp+1Ch] [rbp-24h]
    signed int k;   // [rsp+1Ch] [rbp-24h]
    int l;          // [rsp+1Ch] [rbp-24h]
    signed int m;   // [rsp+20h] [rbp-20h]
    signed int v7;  // [rsp+24h] [rbp-1Ch]
    int v8;         // [rsp+28h] [rbp-18h]
    signed int v9;  // [rsp+2Ch] [rbp-14h]
    signed int number; // [rsp+30h] [rbp-10h]
    char buf[0x1000];
    char byte_202040[17];
    int position; // [rsp+34h] [rbp-Ch]
    int v12; // [rsp+38h] [rbp-8h]
    int v13; // [rsp+38h] [rbp-8h]

    memset(buf, 0, 0x1000uLL);
    v8 = 1;
    for (i = 0; i <= 16; ++i)
    {
        v12 = _IO_getc(in_fp);
        if (v12 == -1)
            break;
        buf[i + 1] = v12;
    }
    v7 = i;
    number = 0;
    position = 0;
    while (v7)
    {
        if (number > v7)
            number = v7;
        if (number > 1)
        {
            v9 = number;
            push_one_bit(out, 0);
            push_n_bits(out, position, 12);
            push_n_bits(out, number - 2, 4);
        }
        else
        {
            v9 = 1;
            push_one_bit(out, 1);
            push_n_bits(out, buf[v8], 8);
        }
        for (j = 0; j < v9; ++j)
        {
            v13 = _IO_getc(in_fp);
            if (v13 == -1)
                --v7;
            else
                buf[((_WORD)v8 + 17 + (_WORD)j) & 0xFFF] = v13;
        }
        v8 = ((_WORD)v8 + (_WORD)v9) & 0xFFF;
        if (v7)
        {
            for (k = 0; k <= 16; ++k)
                byte_202040[k] = buf[((_WORD)v8 + (_WORD)k) & 0xFFF];
            number = 0;
            for (l = ((_WORD)v8 + 17) & 0xFFF; l != v8; l = ((_WORD)l + 1) & 0xFFF)
            {
                if (l)
                {
                    for (m = 0; m <= 16 && buf[((_WORD)l + (_WORD)m) & 0xFFF] == byte_202040[m]; ++m)
                        ;
                    if (m >= number)
                    {
                        number = m;
                        position = l;
                    }
                }
            }
        }
    }
    push_one_bit(out, 0);
    push_n_bits(out, 0LL, 12);
}

int main(int argc, char **argv)
{
    
    FILE *in_fp;
    out_container *out = malloc(sizeof(out_container));

    if(argc < 3)
    {
        puts("Usage: ./compress in_file out_file");
        exit(0);
    }

    in_fp = fopen(argv[1], "rb");

    if(in_fp == NULL)
    {
        perror("fopen error!");
        exit(-1);
    }
    
    out->out_fp = fopen(argv[2], "wb");

    if(out->out_fp == NULL)
    {
        perror("fopen error!");
        exit(-1);
    }

    setbuf(out->out_fp, NULL);
    out->field_8 = 0;
    *(unsigned char *)&(out->field_C) = -128;
    out->amount = 0;

    compress(in_fp, out);

    fputc(0, out->out_fp); 

    fclose(in_fp);
    fclose(out->out_fp);

    free(out);

    return 0;
}