#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

int f[10]={0}; // 10 pozitii critice

pthread_mutex_t m; // pentru optimizare facem vector m[10]


void* myF (void* a){
    char* c = (char*)a;
    int nr = atoi(c);
    printf("daa %d\n",nr);
    int uc;
    pthread_mutex_lock(&m);
    while(nr){
        uc=nr%10;
        f[uc]++;
        nr/=10;
    }
    pthread_mutex_unlock(&m);
    return NULL;
}

int main(int argc, char** argv){
    // printf("%d",argc);
    int i;
    pthread_t T[argc];
    pthread_mutex_init(&m,NULL);
    for(i=0;i<argc-1;i++){
        int N = atoi(argv[i+1]);
        printf("%d\n",N);
        pthread_create(&T[i],NULL,myF,argv[i+1]);

    }    
    for(i=0;i<argc-1;i++){
        pthread_join(T[i],NULL);
    }
    pthread_mutex_destroy(&m);
    for(i=0;i<10;i++){
        printf("f[%d] = %d\n",i,f[i]);
    }
    return 0;
}
