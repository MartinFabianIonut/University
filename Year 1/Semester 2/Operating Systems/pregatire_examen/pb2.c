#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <math.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>

int main(int argc, char** argv){
    int i;
    
//    printf("Hello world! %f\n", sqrt(2));

    char* s[9] = {"A","B","C","D","E","F","G","H","I"};
    for(i=0;i<9;i++){
        if(fork()!=0){
            execl("/bin/echo","/bin/echo",s[i],NULL);
        }
    }

    return 0;
}
