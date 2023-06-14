#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

//int f[10]={0}; // 10 pozitii critice

//pthread_mutex_t m; // pentru optimizare facem vector m[10]

typedef struct{
    int * flag;
    pthread_mutex_t * m;
    pthread_barrier_t * b;
}data;

void* myF (void* a){
  /*  char* c = (char*)a;
    int nr = atoi(c);
    printf("daa %d\n",nr);
    int uc;*/
    data d = *((data*)a);
    pthread_barrier_wait(d.b);
    int r;
    while(1){
        r = random()%111112;
        pthread_mutex_lock(d.m);
        if(*(d.flag)==0){
            printf("%d\n",r);
            if(r%1001==0){
                *(d.flag) = 1;
                break;
            }
        }
        else{
            break;
        }
        pthread_mutex_unlock(d.m); 
    }
    pthread_mutex_unlock(d.m);
    return NULL;
}

int main(int argc, char** argv){
    // printf("%d",argc);
    int i;
    srandom(getpid());
    data d;
   
    if(argc<2){
        printf("Nu e argument");
        exit(1);
    }
    int N = atoi(argv[1]);
    

    pthread_mutex_t * m = malloc(sizeof(pthread_mutex_t));
    pthread_barrier_t * b = malloc(sizeof(pthread_barrier_t));
    int * flag = malloc(sizeof(int));
    pthread_mutex_init(m,NULL);
    pthread_barrier_init(b,NULL,N);
    *flag = 0;
    d.flag = flag;
    d.m = m;
    d.b = b;

    pthread_t T[N];
    
    for(i=0;i<N;i++){
       // printf("Threadul %d\n",i);
        pthread_create(&T[i],NULL,myF,&d);
    }    
    for(i=0;i<N;i++){
        pthread_join(T[i],NULL);
    }
     
    pthread_mutex_destroy(m);
    pthread_barrier_destroy(b);

    free(m);
    free(b);
    free(flag);

    return 0;
}
