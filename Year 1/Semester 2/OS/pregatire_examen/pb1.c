#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>

int main(int argc, char** argv){
    int i;
    char* s[3] = {"A","B","C"};
    for(i=0;i<3;i++){
        execl("/bin/echo","/bin/echo",s[i],NULL);
    }

    return 0;
}
