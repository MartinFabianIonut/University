package org.example;

import org.example.domain.Parcare;
import org.example.util.IOHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    private static final int n = 100;
    private static final int p = 25;
    private static final int nr_intrari=3;
    private static final int nr_iesiri=2;
    private static AtomicInteger totalEntries = new AtomicInteger(nr_intrari);
    private static AtomicInteger totalExits = new AtomicInteger(nr_iesiri);


    public static void main(String[] args) {

        Parcare parcare = new Parcare(n, p);
        ReentrantLock lock = new ReentrantLock();
        Condition notFull = lock.newCondition();
        Condition notEmpty = lock.newCondition();


        List<Thread> entries = new ArrayList<>();
        List<Thread> exits = new ArrayList<>();

        for (int i = 0; i < nr_intrari; i++) {
            entries.add(new Entry(parcare, lock, notFull, notEmpty));
        }
        for (int i = 0; i < nr_iesiri; i++) {
            exits.add(new Exit(parcare, lock, notFull, notEmpty));
        }
        Thread iterator = new Exit.IteratorThread(parcare, lock);

        iterator.start();
        for (Thread entry : entries) {
            entry.start();
        }
        for (Thread exit : exits) {
            exit.start();
        }
        for (Thread entry : entries) {
            try {
                entry.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Thread exit : exits) {
            try {
                exit.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        iterator.interrupt();
        try {
            iterator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Entry extends Thread {
        private Parcare parcare;
        private Condition notFull;
        private Condition notEmpty;
        private ReentrantLock lock;

        public Entry(Parcare parcare, ReentrantLock lock, Condition notFull, Condition notEmpty) {
            this.parcare = parcare;
            this.lock = lock;
            this.notFull = notFull;
            this.notEmpty = notEmpty;
        }


        @Override
        public void run() {
            for (int i = 0; i < 200; i++) {
                lock.lock();
                try {
                    while (parcare.getP().equals(parcare.getN())) {
                        notFull.await();
                    }
                    parcare.Intrare();
                    notEmpty.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    System.out.println("Entry thread interrupted with id: " + Thread.currentThread().getId());
                }
            }
            totalEntries.decrementAndGet();
        }
    }

    static class Exit extends Thread {
        private Parcare parcare;
        private Condition notFull;
        private Condition notEmpty;
        private ReentrantLock lock;

        public Exit(Parcare parcare, ReentrantLock lock, Condition notFull, Condition notEmpty) {
            this.parcare = parcare;
            this.lock = lock;
            this.notFull = notFull;
            this.notEmpty = notEmpty;
        }

        @Override
        public void run() {
            for (int i = 0; i < 275; i++) {
                lock.lock();
                try {
                    while (parcare.getP().equals(0)) {
                        notEmpty.await();
                    }
                    parcare.Iesire();
                    notFull.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    System.out.println("Exit thread interrupted with id: " + Thread.currentThread().getId());
                    //e.printStackTrace();
                }
            }
            totalExits.decrementAndGet();
        }

        static class IteratorThread extends Thread {
            private Parcare parcare;
            private ReentrantLock lock;

            public IteratorThread(Parcare parcare, ReentrantLock lock) {
                this.parcare = parcare;
                this.lock = lock;
            }

            @Override
            public void run() {
                while (totalEntries.get() != 0 || totalExits.get() != 0) {
                    lock.lock();
                    IOHandler.writeToFile(parcare.getTranzactii());
                    lock.unlock();
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        System.out.println("Iterator thread interrupted with id: " + Thread.currentThread().getId());
                        //e.printStackTrace();
                    }
                }
                IOHandler.writeToFile(parcare.getTranzactii());
            }
        }
    }

}

