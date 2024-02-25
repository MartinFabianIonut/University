package org.example;

import org.example.datastructure.MyBlockingLinkedList;
import org.example.datastructure.MyBlockingQueue;
import org.example.domain.MyObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static int size = 40;

    public static void main(String[] args) {
        int n = 20;
        int p = 3;
        int c = 2;

        AtomicInteger totalProducers = new AtomicInteger(p);
        AtomicInteger totalConsumers = new AtomicInteger(c);

        ReentrantLock lock = new ReentrantLock();
        MyBlockingQueue<Integer> banda = new MyBlockingQueue<>(lock, totalProducers, n);
        MyBlockingLinkedList<MyObject> lista = new MyBlockingLinkedList<>(lock);

        List<Thread> producers = new ArrayList<>();
        List<Thread> consumers = new ArrayList<>();

        for (int i = 0; i < p; i++) {
            producers.add(new Producer(lista, banda, totalProducers, i));
            producers.get(i).start();
        }
        for (int i = 0; i < c; i++) {
            consumers.add(new Consumer(lista, banda, totalConsumers, p + i));
            consumers.get(i).start();
        }
        Thread iterator = new ListIterator(lista, banda, totalProducers, totalConsumers);
        iterator.start();

        for (Thread producer : producers) {
            try {
                producer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Thread consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            iterator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static class Producer extends Thread {
        private final MyBlockingLinkedList<MyObject> list;
        private final MyBlockingQueue<Integer> banda;
        private final AtomicInteger totalProducers;
        private final int threadId;

        public Producer(MyBlockingLinkedList<MyObject> list,
                        MyBlockingQueue<Integer> banda,
                        AtomicInteger totalProducers,
                        int threadId) {
            this.list = list;
            this.banda = banda;
            this.totalProducers = totalProducers;
            this.threadId = threadId;
        }

        @Override
        public void run() {
            for (int i = 0; i < size; i++) {
                try {
                    for (int j = 0; j < 4; j++) {
                        banda.push(i,list,threadId);
                    }
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            totalProducers.decrementAndGet();
        }
    }

    private static class Consumer extends Thread {
        private final MyBlockingLinkedList<MyObject> list;
        private final MyBlockingQueue<Integer> banda;
        private final AtomicInteger totalConsumers;
        private final int threadId;

        public Consumer(MyBlockingLinkedList<MyObject> list,
                        MyBlockingQueue<Integer> banda,
                        AtomicInteger totalConsumers,
                        int threadId) {
            this.list = list;
            this.banda = banda;
            this.totalConsumers = totalConsumers;
            this.threadId = threadId;
        }

        @Override
        public void run() {
            while (banda.isEmpty()) {
                try {
                    for (int j = 0; j < 3; j++) {
                        banda.pop(list,threadId);
                    }
                    sleep(8);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            totalConsumers.decrementAndGet();
        }
    }

    private static class ListIterator extends Thread {
        private final MyBlockingLinkedList<MyObject> list;
        private final MyBlockingQueue<Integer> banda;
        private final AtomicInteger totalProducers;
        private final AtomicInteger totalConsumers;

        public ListIterator(MyBlockingLinkedList<MyObject> list, MyBlockingQueue<Integer> banda, AtomicInteger totalProducers, AtomicInteger totalConsumers) {
            this.list = list;
            this.banda = banda;
            this.totalProducers = totalProducers;
            this.totalConsumers = totalConsumers;
        }

        @Override
        public void run() {
            while (totalProducers.get() != 0 || totalConsumers.get() != 0) {
                try {
                    synchronized (banda) {
                        list.printList();
                    }

                    sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            list.printList();
        }
    }
}

