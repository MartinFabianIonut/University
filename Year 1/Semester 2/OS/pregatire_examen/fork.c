#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

int main(int argc, char**argv){
    if(fork() || fork()){
        fork();
        //printf("da\n");
    }
    sleep(15);
    //printf("da\n");
    return 0;
}
