#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <time.h>

typedef struct {
        int *arr, idx, n;
            pthread_mutex_t *m;
                pthread_barrier_t *b;
} data;

void *f(void *arg) {
        data d = *((data*) arg);
            d.arr[d.idx] = random() % 11 + 10;
                printf("T[%d] -> %d\n", d.idx, d.arr[d.idx]);
                    pthread_barrier_wait(d.b);
                        int i, stop;
                            while(1) {
                                        stop = -1;
                                                pthread_mutex_lock(d.m);
                                                        if(d.arr[d.idx] <= 0)
                                                                        break;
                                                                stop = 1;
                                                                        for(i = 0; i < d.n; i++) {
                                                                                        if(i != d.idx) {
                                                                                                            d.arr[i]--;
                                                                                                                            if(d.arr[i] > 0)
                                                                                                                                                    stop = 0;
                                                                                                                                        }
                                                                                                }
                                                                                if(stop == 1)
                                                                                                break;
                                                                                        pthread_mutex_unlock(d.m);
                                                                                                // this is here only to produce more contention between threads
                                                                                                usleep(100);
                                                                                                    }
                                if(stop == -1)
                                            printf("Thread %d has been eliminated!\n", d.idx);
                                    else
                                                printf("Thread %d has won!\n", d.idx);
                                        pthread_mutex_unlock(d.m);
                                            return NULL;
}

int main(int argc, char *argv[]) {
        int n;
            printf("n = ");
                scanf("%d", &n);
                    srandom(time(NULL));
                        int i;
                            int *arr = malloc(sizeof(int) * n);
                                data *args = malloc(sizeof(data) * n);
                                    pthread_mutex_t *m = malloc(sizeof(pthread_mutex_t));
                                        pthread_mutex_init(m, NULL);
                                            pthread_barrier_t *b = malloc(sizeof(pthread_barrier_t));
                                                pthread_barrier_init(b, NULL, n);
                                                    pthread_t *th = malloc(sizeof(pthread_t) * n);
                                                        for(i = 0; i < n; i++) {
                                                                    args[i].m = m;
                                                                            args[i].b = b;
                                                                                    args[i].arr = arr;
                                                                                            args[i].n = n;
                                                                                                    args[i].idx = i;
                                                                                                            if(0 > pthread_create(&th[i], NULL, f, &args[i])) {
                                                                                                                            perror("Error on thread create");
                                                                                                                                    }
                                                                                                                }

                                                            for(i = 0; i < n; i++) {
                                                                        pthread_join(th[i], NULL);
                                                                            }

                                                                for(i = 0; i < n; i++) {
                                                                            printf("%5d", arr[i]);
                                                                                }
                                                                    printf("\n");

                                                                        pthread_mutex_destroy(m);
                                                                            pthread_barrier_destroy(b);
                                                                                free(th);
                                                                                    free(args);
                                                                                        free(arr);
                                                                                            free(m);
                                                                                                free(b);
                                                                                                    return 0;
}

