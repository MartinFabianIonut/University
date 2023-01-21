#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char ** argv){
    int fiu2parinte[2];
    pipe(fiu2parinte);    
    if(fork()==0){
        if(argc==1){
            printf("Nu ati dat numele fisierului!\n");
            exit(1);
        }
        close(fiu2parinte[0]);
        FILE * f = fopen(argv[1],"r");
        if(f==NULL){
            perror("Error opening file");
            exit(1);
        }
        int suma=0,i,j,n, **mat;
        fscanf(f,"%d",&n);
        mat = malloc(sizeof(int*)*n);
        for(i=0;i<n;i++){
            mat[i]=malloc(sizeof(int)*n);
        }
        for(i=0;i<n;i++){
            for(j=0;j<n;j++){
                fscanf(f,"%d",&mat[i][j]);
            }
        }
        for(i=0;i<n;i++){
            suma+=mat[i][i];
        }
        for(i=0;i<n;i++){
            free(mat[i]);
        }
        free(mat);
        printf("Fiul are suma: ");
        printf("%d",suma);
        printf("\n");
        fclose(f);
        int status = 0;
        if(-1 == write(fiu2parinte[1],&suma,sizeof(int))){
            status = 1;
        }
        close(fiu2parinte[1]);
        exit(status);
    }
   
        close(fiu2parinte[1]);
        int s;
        read(fiu2parinte[0],&s,sizeof(int));
        wait(0);
        close(fiu2parinte[0]);
        printf("Parintele are suma: ");
        printf("%d",s);
        printf("\n");
    
    return 0;
}
