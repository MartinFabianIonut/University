#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>

int main(int argc, char**argv){
    //int n;
    int p[2];
   // char buf[10];
    pipe(p);
    n = read(p[0],buf,10);
    printf("%d\n",n);
    return 0;
}
