#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <sys/stat.h>

//prog B 
int main(int argc, char*argv[]){
    int rd, wr;
    rd = open("fifo1",O_RDONLY);
    if(rd == -1){perror("f");exit(1);} 
    wr = open("fifo2",O_WRONLY);
    if(wr == -1){perror("f");close(rd);exit(1);}
    int nr = 0;srandom(getpid());
    while(nr!=10){
        if(read(rd,&nr,sizeof(int))<0){perror("f");break;}
        printf("copilul a citit %d\n",nr);
        nr = random()%10+1; 
        if(write(wr,&nr,sizeof(int))<0){perror("f");break;}
        printf("copilul a trimis %d\n",nr);

    }
    close(rd);close(wr);
    
    return 0;
}
