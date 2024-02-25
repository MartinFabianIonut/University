package org.example.datastructure;

import org.example.domain.Node;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingLinkedList<T> {
    public Node<?> head;
    public Node<?> tail;
    public final Lock lock;

    public MyBlockingLinkedList(ReentrantLock lock) {
        this.lock = lock;
        head = null;
        tail = null;
    }

    public void add(T nodeData) {
        lock.lock();
        Node<T> node = new Node<>(nodeData, null, null);
        if (head == null) {
            head = node;
            tail = node;
            lock.unlock();
            return;
        }
        tail.next = node;
        node.previous = tail;
        tail = node;

        lock.unlock();
    }

    public void printList() {
        lock.lock();
        Node<?> current = head;
        System.out.println("List: ");
        while (current != null) {
            System.out.println(current.getData());
            current = current.next;
        }
        lock.unlock();
    }
}
