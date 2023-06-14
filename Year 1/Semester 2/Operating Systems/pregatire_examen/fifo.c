#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>

int main(int argc, char**argv){
    int r,n=0;
    r=open("abc",O_RDONLY);
    if(r==-1){
        printf("read problem");
        exit(1);
    }
    n++;
    printf("am trecut de read");
    //w=open("abc",O_WRONLY);
    //if(w==-1){
      //  printf("write problem");
       // exit(1);
    //}
    n++;
    printf("%d\n",n);
    return 0;
}
