// gcc -fPIC -O3 -shared hook.c -c -o hook.o
// ld -shared -ldl hook.o -o hook.so
#include <stdio.h>
#include <dlfcn.h>

typedef void (*FUNC)(char *, char *);

void _init()
{
    FUNC func;
    char *printable = "_0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    #define LENGTH 63
    char in[8], out[8];
    char *image_base = *(void **)dlopen(NULL, 1);
    printf("Image base: %p\n", image_base);
    func = image_base + 0xC22;
    *(size_t *)in = 0;
    for (int i = 0; i < LENGTH; i++)
    {
        in[0] = printable[i];
        for (int ii = 0; ii < LENGTH; ii++)
        {
            in[1] = printable[ii];
            for (int iii = 0; iii < LENGTH; iii++)
            {
                in[2] = printable[iii];
                for (int iiii = 0; iiii < LENGTH; iiii++)
                {
                    in[3] = printable[iiii];
                    *(size_t *)out = 0;
                    func(in, out);
                    switch(*(size_t *)out)
                    {
                    case 0x746373:
                        fprintf(stderr, "%s -> %s\n",in, out);
                        break;
                    case 0x395F66:
                        fprintf(stderr, "%s -> %s\n",in, out);
                        break;
                    case 0x323031:
                        fprintf(stderr, "%s -> %s\n",in, out);
                        break;
                    }
                }
            }
        }
    }
    printf("over\n");
    exit(0);
}