package org.example.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyConcurrentDictionary {
    private final Map<String, List<String>> dictionary = new TreeMap<>();
    private final Lock dictionaryLock = new ReentrantLock();

    private int size() {
        // size of all lists
        int size = 0;
        for (List<String> list : dictionary.values()) {
            size += list.size();
        }
        return size;
    }

    public synchronized int addToDictionary(String key, String value) {
        dictionaryLock.lock();
        System.out.println("THREADUL " + Thread.currentThread().getName() + " a adaugat in dictionar: " + key + " -> " + value);
        if (dictionary.containsKey(key)) {
            List<String> existingList = dictionary.get(key);
            existingList.add(value);
        } else {
            List<String> newList = new ArrayList<>();
            newList.add(value);
            dictionary.put(key, newList);
        }
        int size = this.size();
        dictionaryLock.unlock();
        return size;
    }

    public synchronized void afisare() {
        dictionaryLock.lock();
        for (String key : dictionary.keySet()) {
            List<String> values = dictionary.get(key);
            System.out.println("Key: " + key + ", Values: " + values);
        }
        System.out.println("\n");
        dictionaryLock.unlock();
    }
}
