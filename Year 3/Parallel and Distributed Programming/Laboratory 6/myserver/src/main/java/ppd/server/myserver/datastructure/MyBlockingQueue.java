package ppd.server.myserver.datastructure;

import ppd.server.myserver.entity.Participant;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue {
    private final int MAX = 50;
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
    private final Participant[] queue = new Participant[101];

    public void finish() {
        lock.lock();
        notEmpty.signal();
        notFull.signal();
        lock.unlock();
    }

    public void put(Participant node) throws InterruptedException {
        lock.lock();
        try {
            while (count == MAX) {
                notFull.await();
            }
            queue[last++] = node;
            if (last == MAX) {
                last = 0;
            }
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Participant take() throws InterruptedException {
        Participant result;
        lock.lock();
        try {
            while (count == 0) {
                if (totalProducerTasks.get() == 0) {
                    return null;
                }
                notEmpty.await();
            }
            result = queue[first++];

            if (first == MAX) {
                first = 0;
            }
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

    public void setTotalProducerTasks(int totalProducerTasks) {
        this.totalProducerTasks.set(totalProducerTasks);
    }
}
