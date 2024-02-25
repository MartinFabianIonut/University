package org.example.datastructure;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyBlockingQueue<T> {
    private final int MAX = 50;
    public final Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;
    private final AtomicInteger totalProducerTasks;

    public MyBlockingQueue(AtomicInteger totalProducersTasks, Lock lock) {
        this.totalProducerTasks = totalProducersTasks;
        this.lock = lock;
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    public int first = 0;

    public int last = 0;
    public int count = 0;
    private final Queue<T> queue = new java.util.LinkedList<>();

    public void finish() {
        lock.lock();
        notEmpty.signal();
        notFull.signal();
        lock.unlock();
    }

    public void put(T element) throws InterruptedException {
        lock.lock();
        try {
            while (count == MAX) {
                //System.out.println("Queue is full " + Thread.currentThread().getName() + " is waiting , size: " + count);
                notFull.await();
            }
            queue.add(element);
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        T result;
        lock.lock();
        try {
            while (count == 0) {
                if (totalProducerTasks.get() == 0) {
                    return null;
                }
                //System.out.println("Queue is empty " + Thread.currentThread().getName() + " is waiting , size: " + count);
                notEmpty.await();
            }
            result = queue.remove();
            --count;
            notFull.signal();
            //System.out.println("Queue - take: " + result);
            return result;
        } finally {
            lock.unlock();
        }
    }

    public synchronized boolean isEmpty() {
        return count == 0;
    }

    public long size() {
        lock.lock();
        long size = count;
        lock.unlock();
        return size;
    }
}
