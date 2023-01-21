#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <string.h>

int x;

pthread_cond_t con;

void * fx(void *a){
    char *fis = *(char**)a;
    printf("%s\n",fis);
    FILE * fs = fopen(fis,"r");
    int aa,b;
    fscanf(fs,"%d%d",&aa,&b);
    printf("A = %d, B = %d\n",aa,b);
    fclose(fs);
    return NULL;
}

void * fy(void *a){
    return NULL;
}


int main(int argc, char** argv){
    if(argc<2){
        printf("DATI CEL PUTIN UN ARGUMENT!");
    }
    int i;
    pthread_cond_init(&con, NULL);

    char ** fisiere = malloc((argc-1)*sizeof(char*));
    for(i=0;i<argc-1;i++){
        fisiere[i]=malloc(strlen(argv[i+1])*sizeof(char));
        strcpy(fisiere[i],argv[i+1]);
        printf("Fisierul %d cu numele: %s\n",i,fisiere[i]);
    }

    pthread_t t[argc-1], t2[argc-1];

    for(i=0;i<argc-1;i++){
        pthread_create(&t[i],NULL, fx, &fisiere[i]);
        pthread_create(&t2[i],NULL, fy, NULL);
    }

    for(i=0;i<argc-1;i++){
        pthread_join(t[i],NULL);
        pthread_join(t2[i],NULL);
    }
    
    for(i=0;i<argc-1;i++){
        free(fisiere[i]);
    }
    free(fisiere);

    return 0;
}
