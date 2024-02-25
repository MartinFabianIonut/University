package org.example.datastructure;

import org.example.domain.Node;

public class MyBlockingLinkedList<T> {
    public final Node<T> head = new Node<>(null, null, null);
    public final Node<T> tail = new Node<>(null, null, null);

    public MyBlockingLinkedList() {
        head.next = tail;
        tail.previous = head;
    }

    public void add(T data) {
        head.lock();
        head.next.lock();

        Node<T> right = head.next;

        Node<T> node = new Node<>(data, null, null);
        node.lock();

        head.next = node;
        node.previous = head;
        node.next = right;
        right.previous = node;

        right.unlock();
        node.unlock();
        head.unlock();
    }
}
