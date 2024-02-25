package org.example.domain;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    public final Lock lock = new ReentrantLock();
    private Cursant data;
    public Node next;
    public Node previous;

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public Node(Cursant data, Node next, Node previous) {
        this.data = data;
        this.next = next;
        this.previous = previous;
    }

    public Cursant getData() {
        return data;
    }

    public void setData(Cursant data) {
        this.data = data;
    }

    public boolean isNotLastNode() {
        return data != null;
    }
}