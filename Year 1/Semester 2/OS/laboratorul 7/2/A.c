#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>
#include <sys/types.h>

int main(int argc, char ** argv){

    int a2b[2],b2a[2];
    pipe(a2b);pipe(b2a);
    if(fork()==0){
        close(a2b[1]);close(b2a[0]);
        int r;
        srandom(getpid());
        int rb = random()%901 + 100; //[Y,X+Y-1] 
        int found = 0;
        while(!found){
            read(a2b[0],&r,sizeof(int));
            printf("Am primit de la A numarul: %d.\n",r);
            if(abs(r-rb)<50){
                found = 1;
            }
            write(b2a[1],&found,sizeof(int));
        }
        close(a2b[0]);close(b2a[1]);
        exit(0);  
    }
    srandom(getpid());
    close(a2b[0]);close(b2a[1]);
    int found = 0,c=0;
    while(!found){
        c++;
        int ra = random()%1001 + 50;
        write(a2b[1],&ra,sizeof(int));
        read(b2a[0],&found,sizeof(int));
    }
    printf("Procesul a generat %d numere",c);
    close(a2b[1]); close(b2a[0]);
    wait(0);
}
