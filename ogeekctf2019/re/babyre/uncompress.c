// gcc -s -O3 uncompress.c -o uncompress
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void recover(char *out_str, unsigned int position, unsigned int num, size_t out, char *buf)
{
    int i, p, o;
    char ch;
    p = position - 1;
    o = out;
    for (i = 0; i < num; i++)
    {
        if (((p - out) & 0xfff) >= 0 && ((p - out) & 0xfff) < 17)
        {
            p = position - 1;
            buf[(o & 0xfff)] = buf[(p & 0xfff)];
            out_str[o++] = buf[(p++ & 0xfff)]; // out_str[p++];
        }
        else
        {
            buf[(o & 0xfff)] = buf[(p & 0xfff)];
            out_str[o++] = buf[(p++ & 0xfff)]; // out_str[p++];
        }
    }
}

int main(int argc, char **argv)
{
    int bit, used;
    register union{
        int value;
        char bytes[4];
    }bits;
    unsigned char ch, *in_str, *out_str, buf[0x1000];
    unsigned int position, num;
    FILE *in_fp, *out_fp;
    size_t in = 0, all_in, out = 0;

    if(argc < 3)
    {
        puts("Usage: ./uncompress in_file out_file");
        exit(0);
    }

    in_fp = fopen(argv[1], "rb");

    if(in_fp == NULL)
    {
        perror("fopen error!");
        exit(-1);
    }

    out_fp = fopen(argv[2], "wb");

    if(out_fp == NULL)
    {
        perror("fopen error!");
        exit(-1);
    }

    setbuf(out_fp, NULL);

    in_str = malloc(62914560);
    out_str = malloc(62914560);

    memset(buf, 0, 0x1000);

    all_in = fread(in_str, 1, 62914560, in_fp);

    bits.bytes[3] = in_str[in++];
    bits.bytes[2] = in_str[in++];
    bits.bytes[1] = in_str[in++];
    bits.bytes[0] = in_str[in++];

    used = 0;

    while (in < all_in)
    {
        if(bits.value & 0x80000000)
        {
            bits.value <<= 1;
            ch = bits.bytes[3];
            bits.value <<= 8;
            buf[out & 0xfff] = ch;
            out_str[out++] = ch;
            used += 9;
        }
        else
        {
            bits.value <<= 1;
            position = (bits.value & 0xfff00000) >> 20;
            bits.value <<= 12;
            num = ((bits.value & 0xf0000000) >> 28) + 2;
            bits.value <<= 4;
            recover(out_str, position, num, out, buf);
            out += num;
            used += 17;
        }

        while(used / 8)
        {
            bits.value |= (in_str[in++] << (used - 8));
            used -= 8;
        }
    }

    fwrite(out_str, 1, out, out_fp);

    fclose(out_fp);
    fclose(in_fp);
    free(in_str);
    free(out_str);

    return 0;
}