#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>

int main(int argc, char*argv[]){
    int f, a2b[2],b2a[2];
    srandom(getpid());
    int nr = random()%151+50;
    printf("Parintele a generat m = %d\n",nr);
    if(nr%2!=0){nr++;printf("Dar numarul nu e par, asa ca il incrementeaza cu 1 si m = %d\n",nr);}
    pipe(a2b);pipe(b2a);
    f = fork();
    if(f<0){perror("fork error");exit(1);}
    else{
        if(f==0){
            
            int m = 201;
            close(a2b[1]);close(b2a[0]);
            while(m > 5){
                //close(a2b[1]);close(b2a[0]);
                if(read(a2b[0],&m,sizeof(int))<0){perror("rd");exit(1);}
                printf("copilul a primit m = %d\n",m);
                m/=2;
                if(write(b2a[1],&m,sizeof(int))<0){perror("wr");exit(1);}
                printf("copilul a trimis m = %d\n",m);
                //close(a2b[0]);close(b2a[1]);
                //exit(0);
            }
            close(a2b[0]);close(b2a[1]);exit(0);
        }
        else{
            close(a2b[0]);close(b2a[1]);
            if(write(a2b[1],&nr,sizeof(int))<0){perror("wr");exit(1);}
            printf("parintele a trimis m = %d\n",nr);
            while(nr > 5){
                //close(a2b[0]);close(b2a[1]);

                if(read(b2a[0],&nr,sizeof(int))<0){perror("rd");exit(1);}
                printf("parintele a primit m = %d\n",nr);
                nr/=2;
                if(write(a2b[1],&nr,sizeof(int))<0){perror("wr");exit(1);}
                printf("parintele a trimis m = %d\n",nr);
                //close(a2b[1]);close(b2a[0]);
            }
            wait(0);close(a2b[1]);close(b2a[0]);exit(0);
        }

    }
    return 0;
}
