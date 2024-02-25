package org.example.datastructure;

import org.example.util.IOHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyDictionary {
    private final HashMap<String, HashMap<String, Integer>> dictionary;
    public Lock lock = new ReentrantLock();
    public Condition conditionReader = lock.newCondition();
    public Condition conditionWorker = lock.newCondition();
    public boolean isShown = false;

    public AtomicInteger transactionsAdded = new AtomicInteger(0);

    public MyDictionary() {
        dictionary = new HashMap<>();
        ;
    }

    public synchronized boolean verifyKey(String key) {
        return dictionary.containsKey(key);
    }

    public synchronized void insertKey(String key) {
        dictionary.put(key, new HashMap<>());
    }

    public synchronized void pushValue(String key, String valueKey, Integer value) {
        lock.lock();
        while (transactionsAdded.get() % 50 == 0 && transactionsAdded.get() != 0) {
            try {
                if (!isShown) {
                    isShown = true;
                    conditionReader.signal();

                    conditionWorker.await();
                    HashMap<String, Integer> innerMap = dictionary.get(key);
                    if (innerMap.containsKey(valueKey)) {
                        innerMap.put(valueKey, innerMap.get(valueKey) + value);
                    } else {
                        innerMap.put(valueKey, value);
                    }
                }else {
                    conditionWorker.await();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (!isShown) {

                HashMap<String, Integer> innerMap = dictionary.get(key);
                if (innerMap.containsKey(valueKey)) {
                    innerMap.put(valueKey, innerMap.get(valueKey) + value);
                } else {
                    innerMap.put(valueKey, value);
                }
                transactionsAdded.incrementAndGet();

        }
        isShown = false;
//        IOHandler.writeText("Transactions done: " + transactionsAdded.get() + " finished\n");
        IOHandler.writeText(transactionsAdded.get() + ": pushing value for " + key + " with operation type " + valueKey + " and value " + value + "\n");
        lock.unlock();

    }

    public synchronized void deleteValue(String key, String valueKey, Integer value) {
        HashMap<String, Integer> innerMap = dictionary.get(key);
        innerMap.remove(valueKey);
    }

    public Iterator<Map.Entry<String, HashMap<String, Integer>>> iterator() {
        lock.lock();
        try {
            return dictionary.entrySet().iterator();
        } finally {
            lock.unlock();
        }
    }
}