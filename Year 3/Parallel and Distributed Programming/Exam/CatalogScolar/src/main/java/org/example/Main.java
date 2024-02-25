package org.example;


import org.example.datastructure.MyBlockingLinkedList;
import org.example.domain.Cursant;
import org.example.util.GenerateClass;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static final int N = 5;
    private static AtomicInteger secretareNumber = new AtomicInteger(N);
    private static MyBlockingLinkedList catalog = new MyBlockingLinkedList();

    static ReentrantLock lock = new ReentrantLock();
    static Condition cond = lock.newCondition();


    public static void main(String[] args) {

        int n=100;
        var cursanti= GenerateClass.generateRandomCursanti(n);
        var chunk=n/N;
        var secretare=new Secretara[N];
        for (int i=0;i<N;i++){
            secretare[i]=new Secretara(cursanti.subList(i*chunk,(i+1)*chunk),lock,cond);
            secretare[i].start();
        }
        var manager=new Manager(lock,cond);
        manager.start();

        for (int i=0;i<N;i++){
            try {
                secretare[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try{
            manager.join();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

    }


    private static class Manager extends Thread {

        private ReentrantLock lock;
        private Condition cond;

        public Manager(ReentrantLock lock, Condition cond) {
            this.lock = lock;
            this.cond = cond;
        }

        @Override
        public void run() {
            try {
                while (secretareNumber.get() > 0) {
                    lock.lock();
                    cond.await();
                    catalog.printUnderMark5();
                    lock.unlock();
                }
                catalog.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class Secretara extends Thread {
        private List<Cursant> cursanti;
        private ReentrantLock lock;
        private Condition cond;

        public Secretara(List<Cursant> cursanti, ReentrantLock lock, Condition cond) {
            this.cursanti = cursanti;
            this.lock = lock;
            this.cond = cond;
        }


        @Override
        public void run() {


            for (Cursant cursant : cursanti) {
                catalog.add(cursant);
                if (cursant.getMedie() < 5) {
                    try {
                        lock.lock();
                        cond.signal();
                        lock.unlock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            secretareNumber.decrementAndGet();
            try{
                lock.lock();
                cond.signal();
                lock.unlock();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("Secretara "+currentThread().getId()+" finished");
        }
    }
}
