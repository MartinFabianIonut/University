package org.example.datastructure;

import org.example.domain.Node;

public class MyBlockingLinkedList<T> {
    public final Node head = new Node(null, null, null);
    public final Node tail = new Node(null, null, null);

    public MyBlockingLinkedList() {
        head.next = tail;
        tail.previous = head;
    }

    public void add(T nodeData) {
        head.lock();
        head.next.lock();

        Node right = head.next;

        Node node = new Node(nodeData, null, null);
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
