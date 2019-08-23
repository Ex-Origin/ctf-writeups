#include <stdio.h>
#include <string.h>

char f(char x, char x1)
{
    return ((x % 7) + x1) ^ ((x ^ 18) * 3 + 2);
}

char printable[] = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ \t\n\r\x0b\x0c";
#define LENGTH 0x100

char enc[] = {0xf3, 0x2e, 0x18, 0x36, 0xe1, 0x4c, 0x22, 0xd1, 0xf9, 0x8c, 0x40, 0x76, 0xf4, 0x0e, 0x00, 0x05, 0xa3, 0x90, 0x0e};

void next(char *str)
{
    int i, ii;
    for (i = 1; i < sizeof(enc); i++)
    {
        for (ii = 0; ii < LENGTH; ii++)
        {
            if (f(str[i], printable[ii]) == enc[i])
            {
                str[i + 1] = printable[ii];
                if (i > sizeof(enc) - 2)
                {
                    puts(str);
                }
            }
        }
    }
}

int main()
{
    int i, ii;
    char buf[0x100] = {0};

    for (i = 0; i < LENGTH; i++)
    {
        for (ii = 0; ii < LENGTH; ii++)
        {
            if (f(printable[i], printable[ii]) == enc[0])
            {
                memset(buf, 0, 0x100);
                buf[0] = printable[i];
                buf[1] = printable[ii];
                next(buf);
            }
        }
    }
}