#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
pthread_mutex_t m;

int n = 500;

void* myrandom(void*a){
    srandom(getpid());
    int r = random()%1001 - 500;
    //pthread_mutex_lock(&m);
    while(1){
        pthread_mutex_lock(&m);
        if(n>=0 && n <= 1000){
            n+=r;
            printf("T%d are n = %d\n",*(int*)a,n);
        }
        else{
            pthread_mutex_unlock(&m);
            break;    
        }
        pthread_mutex_unlock(&m);  
    }
    return NULL;
}

int main(int argc, char** argv){
    pthread_t t1, t2;
    int i=1,j=2;
    pthread_mutex_init(&m,NULL);
    pthread_create(&t1, NULL, myrandom, &i);
    pthread_create(&t2, NULL, myrandom, &j);
    pthread_join(t1, NULL);
    pthread_join(t2, NULL);
    pthread_mutex_destroy(&m);
    return 0;
} 
