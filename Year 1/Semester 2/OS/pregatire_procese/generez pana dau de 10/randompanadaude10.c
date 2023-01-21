#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <sys/stat.h>
//prog A
int main(int argc, char*argv[]){
    if(mkfifo("fifo1",0600)<0){
        perror("f");exit(1);
    }
    if(mkfifo("fifo2",0600)<0){
        perror("f");exit(1);
    }
    int rd, wr;
    wr = open("fifo1",O_WRONLY);
    if(wr == -1){perror("f");exit(1);}
    rd = open("fifo2",O_RDONLY);
    if(rd == -1){perror("f");close(wr);exit(1);}
    srandom(getpid());
    int nr = 0;
    while(nr!=10){
        nr = random()%10+1;
        if(write(wr,&nr,sizeof(int))<0){perror("f");break;}
        printf("parintele a trimis %d\n",nr);
        if(read(rd,&nr,sizeof(int))<0){perror("f");break;}
        printf("parintele a primit %d\n",nr);

    }
    close(rd);close(wr);
    unlink("fifo1");unlink("fifo2");
    return 0;
}
