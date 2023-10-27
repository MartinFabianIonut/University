package org.example;

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static class MyThread extends Thread {

        private int start;
        private int end;
        private int []A;
        private int []B;
        private int []C;
        public MyThread( int start, int end, int []A, int []B, int []C) {
            this.start = start;
            this.end = end;
            this.A = A;
            this.B = B;
            this.C = C;
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                C[i] = A[i] + B[i];
            }
        }
    }

    public static class MyThread2 extends Thread {
        private int id;
        private int step;
        private int []A;
        private int []B;
        private int []C;
        public MyThread2(int id, int step, int []A, int []B, int []C) {
            this.id = id;
            this.step = step;
            this.A = A;
            this.B = B;
            this.C = C;
        }

        @Override
        public void run() {
            // this approach is worse, because it can cause cache misses
            for (int i = id; i < A.length; i += step) { // cyclic distribution
                C[i] = A[i] + B[i];
            }
        }
    }

    public static void main(String[] args) {
        Random rand = new Random();
        int N = 20;
        int L = 5;

        int []A = new int[N];
        int []B = new int[N];
        int []C = new int[N];

        for(int i = 0; i < N; i++) {
            A[i] = rand.nextInt(L);
            B[i] = rand.nextInt(L);
            C[i] = 0;
        }
        int P = 8; // number of threads = number of cores

        /*int start = 0, end;
        int cat = N/P, rest = N%P;

        MyThread threads[] = new MyThread[P];
        for (int i = 0; i < P; i++) {
            end = start + cat;
            if(rest > 0) {
                end++;
                rest--;
            }
            threads[i] = new MyThread(start, end, A, B, C);
            threads[i].start(); // create the thread and associate it with the current process and run it
            start = end;
        }*/
        MyThread2 threads[] = new MyThread2[P];
        for (int i = 0; i < P; i++) {
            threads[i] = new MyThread2(i, P, A, B, C);
            threads[i].start(); // create the thread and associate it with the current process and run it
        }
        for (int i = 0; i < P; i++) {
            try {
                threads[i].join(); // wait for the thread to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Arrays.toString(A));
        System.out.println(Arrays.toString(B));
        System.out.println(Arrays.toString(C));
    }
}