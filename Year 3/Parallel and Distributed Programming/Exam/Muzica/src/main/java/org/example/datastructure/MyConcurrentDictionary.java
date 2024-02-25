package org.example.datastructure;

import org.example.domain.Mesaj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyConcurrentDictionary {
    private final Map<String, List<Mesaj>> dictionary = new TreeMap<>();
    private final Lock dictionaryLock = new ReentrantLock();

    public long size() {
        // size of all lists
        dictionaryLock.lock();
        long size = 0;
        for (List<Mesaj> list : dictionary.values()) {
            size += list.size();
        }
        dictionaryLock.unlock();
        return size;
    }

    public void addToDictionary(String key, Mesaj value) {
        dictionaryLock.lock();
        //System.out.println("THREADUL " + Thread.currentThread().getName() + " a adaugat in dictionar: " + key + " -> " + value);
        if (dictionary.containsKey(key)) {
            List<Mesaj> existingList = dictionary.get(key);
            existingList.add(value);
        } else {
            List<Mesaj> newList = new ArrayList<>();
            newList.add(value);
            dictionary.put(key, newList);
        }
        long size = this.size();
        dictionaryLock.unlock();
    }
}
