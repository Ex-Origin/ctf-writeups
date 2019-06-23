// gcc -fPIC -O3 -shared hook2.c -c -o hook2.o
// ld -shared -ldl hook2.o -o hook2.so
#include <stdio.h>
#include <dlfcn.h>

typedef int (*FUNC)(int);

void _init()
{
    FUNC func;
    int array[0x100] = {0};
    array[0] = 0xD8BF92EF;
    array[1] = 0x9FCC401F;
    array[2] = 0xC5AF7647;
    array[3] = 0xBE040680;

    char *image_base = *(void **)dlopen(NULL, 1);
    printf("Image base: %p\n", image_base);
    func = image_base + 0x1464;
    for (int i = 0; i < 26; i++)
    {
        array[i + 4] = array[i] ^ func(array[i+1] ^ array[i+2] ^ array[i+3]);
    }

    for(int i=3;i>=0;i--)
    {
        printf("%08X\n", array[26 + i]);
        printf("%c%c%c%c\n", ((char *)&array[26 + i])[0], ((char *)&array[26 + i])[1], ((char *)&array[26 + i])[2], ((char *)&array[26 + i])[3]);
    }
    printf("over\n");
    exit(0);
}