#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <time.h>

///Scrieti un program C care genereaza N numere intregi aleatoate (N dat la linia de comanda). Apoi creeaza un proces fiu si ii trimite numerele prin pipe. 
///Procesul fiu calculeaza media numerelor si trimite rezultatul inapoi parintelui.



int main(int argc, char*argv[]){
    srandom(time(NULL));
    int p2f[2],f2p[2];
    pipe(p2f);pipe(f2p);
    if(argc<2){
        printf("arg");
        exit(1);
    }
    int i,n = atoi(argv[1]), r[n];
    for(i=0;i<n;i++){
        int ra = random()%1001-500;
        r[i]=ra;
        printf("nr rand %d = %d\n",i,ra);
    }
    int f = fork();
    if(f<0){
        perror("fork");
        exit(1);
    }
    else{
        if(f==0){
            close(p2f[1]);close(f2p[0]);  
            int n;
            if(read(p2f[0],&n,sizeof(int))<0){
                perror("re");exit(1);
            }
            int t[n];
            if(read(p2f[0],t,sizeof(int)*n)<0){
                perror("re");exit(1);
            }
            double media=0;
            for(int i = 0;i<n;i++){
                media+= t[i];
            }
            media /= n;
            if(write(f2p[1],&media,sizeof(double))<0){
                perror("wr"); exit(1);
            }
            close(p2f[0]);close(f2p[1]);
            exit(0);
        }
        else{
            close(p2f[0]);close(f2p[1]);
            if(write(p2f[1],&n,sizeof(int))<0){
                perror("wr");exit(1);
            }
            if(write(p2f[1],r,sizeof(int)*n)<0){
                perror("wr");
                exit(1);
            }
            double m;
            if(read(f2p[0],&m,sizeof(double))<0){
                perror("re");exit(1);
            }
            close(p2f[1]);close(f2p[0]);
            printf("Media numerelor este: %f",m);
        }
    }
    wait(0);
    return 0;
}

