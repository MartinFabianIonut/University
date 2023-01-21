#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>                                                                                         
#include <sys/wait.h>


int main(int argc, char** argv) {
     int i, j;
      for(i=0; i<3; i++) {
           if(fork() == 0) {
sleep(10);                 printf("%d %d\n", getppid(), getpid());
                 for(j=0; j<3; j++) {
                      if(fork() == 0) {
           sleep(10);                 printf("%d %d\n", getppid(), getpid());
                           exit(0);
                            }
                       }
                  for(j=0; j<3; j++) {
                       wait(0);
                        }
                   exit(0);
                    }
            }
       for(i=0; i<3; i++) {
            wait(0);
             }
       sleep(10); 
        return 0;
}
