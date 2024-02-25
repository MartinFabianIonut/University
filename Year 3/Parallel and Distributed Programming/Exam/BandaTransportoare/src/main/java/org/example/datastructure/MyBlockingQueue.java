package org.example.datastructure;

import org.example.domain.MyObject;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<T> {
    private final int MAX;
    private final Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;
    private final AtomicInteger totalProducerTasks;

    public MyBlockingQueue(ReentrantLock lock, AtomicInteger totalProducerTasks, int max ) {
        this.lock = lock;
        this.totalProducerTasks = totalProducerTasks;
        this.MAX = max;
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    public int first = 0;

    public int last = 0;
    public int count = 0;
    private final Queue<T> queue = new java.util.LinkedList<>();

    public void push(T element, MyBlockingLinkedList<MyObject> list, int threadId) throws InterruptedException {
        lock.lock();
        try {
            while (count == MAX) {
                notFull.await();
            }
            queue.add(element);
            ++count;
            notEmpty.signal();
        } finally {
            list.add(new MyObject(threadId, count, "push"));
            lock.unlock();
        }
    }

    public T pop(MyBlockingLinkedList<MyObject> list, int threadId) throws InterruptedException {
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
            list.add(new MyObject(threadId, count, "pop"));
            return result;
        } finally {
            lock.unlock();
        }
    }

    public synchronized boolean isEmpty() {
        if (totalProducerTasks.get() == 0) {
            return !queue.isEmpty();
        }
        return true;
    }

    public int size() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}