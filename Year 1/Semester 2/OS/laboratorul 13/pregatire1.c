#include <stdio.h>
#include <stdlib.h> 
#include <pthread.h>
#include <unistd.h>

pthread_mutex_t mut;

int n, m, sir[1000];

void * functie(void*a){
    int k = *(int*)a;
    printf("Threadul %d a inceput",k);
    int i = 0;
    while(i<m){
        int r = random()%n;
        pthread_mutex_lock(&mut);
        if(sir[r] < 9){
            printf("Threadul %d spune ca pe pozitia %d este o cifra, anume: %d\n",k,r,sir[r]);
        }
        else{
            int suma = 0;
            int nr = sir[r];
            while(nr){
                suma+=nr%10;
                nr/=10;
            }
            sir[r]=suma;
            printf("Threadul %d spune ca pe pozitia %d nu e cifra, deci transforma sirul, care va arata asa:\n",k,r);
            for(int j = 0; j<n;j++){
                printf("%d ",sir[j]);
            }
            printf("\n");
        }

        pthread_mutex_unlock(&mut);
        i++;
    }
    return NULL;
}

int main(int argc, char ** argv){
    
    srandom(getpid());
    printf("DATI N = ");
    scanf("%d",&n);
    printf("DATI M = ");
    scanf("%d",&m);
    pthread_mutex_init(&mut,NULL);
    pthread_t t[n];
    int ar[n];
    printf("Sirul este: ");
    for(int i=0;i<n;i++){
        int r = random()%901+100;
        sir[i]=r;
        printf("%d ",r);
    }
    printf("\n");
    for(int i=0;i<n;i++){
        ar[i]=i;
        pthread_create(&t[i],NULL, functie, &ar[i]);
    }

    for(int i=0;i<n;i++){
        pthread_join(t[i],NULL);
    }

    pthread_mutex_destroy(&mut);
    return 0;
}
