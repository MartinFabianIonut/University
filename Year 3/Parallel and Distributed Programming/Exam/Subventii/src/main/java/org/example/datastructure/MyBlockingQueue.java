package org.example.datastructure;

import org.example.domain.Cerere;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<T> {
    private final int MAX = 20;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public int first = 0;

    public int last = 0;
    public int count = 0;
    private final Queue<T> queue = new java.util.LinkedList<>();

    public void put(T element) throws InterruptedException {
        lock.lock();
        try {
            while (count == MAX) {
                notFull.await();
            }
            if (element instanceof Cerere) {
                ((Cerere) element).setCod(++last);
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
        return count;
    }

}
