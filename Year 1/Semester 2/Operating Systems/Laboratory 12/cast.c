#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

//int f[10]={0}; // 10 pozitii critice

//pthread_mutex_t m; // pentru optimizare facem vector m[10]


void* myF (void* a){
  /*  char* c = (char*)a;
    int nr = atoi(c);
    printf("daa %d\n",nr);
    int uc;*/
    int p = (int)a;
    printf("dsa %d yes\n",p);
    return NULL;
}
int main(int argc, char** argv){
    // printf("%d",argc);
    int i=5;
   
       
   pthread_t t;
//   int a[1]={5};
   pthread_create(&t,NULL,myF,&i);
   pthread_join(t,NULL); 
   return 0;
}
