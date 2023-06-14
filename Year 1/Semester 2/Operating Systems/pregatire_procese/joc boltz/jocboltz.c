#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <time.h>

int sapte(int nr){
    if(nr%7==0){
        return 1;
    }
    while(nr){
        if(nr%10==7){
            return 1;
        }
        nr/=10;
    }
    return 0;
}

void joc(int pipes[][2], int index, int n){
    if(index + 1 < n){
        int f = fork();
        if(f<0){
            perror("fork");
            exit(1);
        }
        else{
            if(f == 0){
                joc(pipes,index+1,n);
                return;
            }
        }
    }
    int nr = 1,i;
    int readi = index%n, writei = (index+1)%n;
    for(i=0;i<n;i++){
        if(i==readi){
            close(pipes[i][1]);
        }
        else{
            if(i==writei){
                close(pipes[i][0]);
            }
            else{
                close(pipes[i][0]);close(pipes[i][1]);
            }
        }
    }
    if(index == 0){
        printf("%d - start\n",nr);
        if(write(pipes[writei][1],&nr,sizeof(int))<0){
            perror("wr");
           
        }
    }
    while(nr >= 1){
        if(0 > read(pipes[readi][0], &nr, sizeof(int))) {
            perror("Error on reading number");
        }
        if(nr != 0) {
            nr++;
            int success = random() % 3;
            if(sapte(nr)) {
                if(!success) {
                    printf("%d - fail\n", nr);
                    nr = 0;
                } else {
                    printf("Boltz!\n");
                }
            }
            else{
                printf("%d\n", nr);
            }
            if(0 > write(pipes[writei][1], &nr, sizeof(int))) {
                perror("Error on writing number");
            }
       } else {
           if(0 > write(pipes[writei][1], &nr, sizeof(int))) {
              perror("Error on writing number");
           }
           break;
       }
    }
    wait(0);
    close(pipes[readi][0]);
    close(pipes[writei][1]);
}

int main(int argc, char*argv[]){
    if(argc < 2){
        printf("Arg");
        exit(1);
    }
    int n = atoi(argv[1]);
    int i;
    int pipes[n][2];
    for(i=0;i<n;i++){
        if(pipe(pipes[i])<0){
            perror("pipe");
            exit(1);
        }
    }
    srandom(time(NULL));
    joc(pipes,0,n);
    return 0;
}
