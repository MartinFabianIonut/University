package org.example.datastructure;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<T> {
    private final int MAX = 20;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private final AtomicInteger totalProducerTasks;

    public MyBlockingQueue(AtomicInteger totalProducersTasks) {
        this.totalProducerTasks = totalProducersTasks;
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

    public void push(T element) throws InterruptedException {
        lock.lock();
        try {
            while (count == MAX) {
                notFull.await();

            }
            queue.add(element);
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T pop() throws InterruptedException {
        T result;
        lock.lock();
        try {
            while (count == 0) {
                if (totalProducerTasks.get() == 0) {
                    return null;
                }
                notEmpty.await();
            }
            result = queue.remove();
            --count;
            notFull.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        return count == 0;
    }
}