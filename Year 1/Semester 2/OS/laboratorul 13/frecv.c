#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>



//int f[10]={0}; // 10 pozitii critice

//pthread_mutex_t m; // pentru optimizare facem vector m[10]

typedef struct{
    int * flag;
    int * vector;
    int * dim;
    pthread_mutex_t * m;
    pthread_barrier_t * b;
    

}data;

void* myF (void* a){
    data d = *((data*)a);
    //pthread_barrier_wait(d.b);
    int i,j,k;
    //printf("%d",*(d.dim));
    //i = random()%(*(d.dim));
    //j = random()%(*(d.dim));
    int sortat = 1;
    int aux;
    while(1){
        i = random()%(*(d.dim));
        j = random()%(*(d.dim));
        pthread_mutex_lock(d.m); 
        k=1; 
        sortat=1;
        while(k<(*(d.dim)) && sortat==1){
            if( d.vector[k-1] > d.vector[k]){ 
                sortat=0; 
            }
            k++;
        }
        if(sortat){
            break;}
        else{
        if(i<j && d.vector[i]>d.vector[j]){
            aux=d.vector[i];
            d.vector[i]=d.vector[j];
            d.vector[j]=aux;
        }
        if(i>j && d.vector[i]<d.vector[j]){
            aux=d.vector[i];
            d.vector[i]=d.vector[j];
            d.vector[j]=aux;
        }
        } pthread_mutex_unlock(d.m);
        
    }
    pthread_mutex_unlock(d.m);
    pthread_barrier_wait(d.b); 
    return NULL;
}

void* afisare(void*a){
    data d =*((data*)a);
    pthread_barrier_wait(d.b);
    int i,dim = (*(d.dim));
    for(i=0;i<dim;i++){
        printf("%d ",d.vector[i]);
    }
    return NULL;
}

int main(int argc, char** argv){
    // printf("%d",argc);
    int i,n;
    srandom(getpid());
    data d;
    scanf("%d",&n);
    

    pthread_mutex_t * m = malloc(sizeof(pthread_mutex_t));
    pthread_barrier_t * b = malloc(sizeof(pthread_barrier_t));
    int * flag = malloc(sizeof(int));
    int * dim = malloc(sizeof(int));
    int * vector = malloc(sizeof(int)*n);
    pthread_mutex_init(m,NULL);
    pthread_barrier_init(b,NULL,n+1);
    *flag = 0;
    *dim=n;
    d.flag = flag;
    d.m = m;
    d.b = b;

    for(i=0;i<n;i++){
        int r = random()%1001;
        vector[i] = r;
        printf("%d ",r);
    }
    printf("\n");
    d.vector = vector;
    d.dim = dim;

    pthread_t T[n+1];
    
    for(i=0;i<n;i++){
        //printf("Threadul %d\n",i);
        pthread_create(&T[i],NULL,myF,&d);
    }  
    //printf("am creat n");
    pthread_create(&T[n],NULL,afisare,&d);
    for(i=0;i<n+1;i++){
        pthread_join(T[i],NULL);
    }
     
    pthread_mutex_destroy(m);
    pthread_barrier_destroy(b);

    free(m);
    free(b);
    free(dim);
    free(flag);
    free(vector);
    return 0;
}
