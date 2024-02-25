package org.example.datastructure;

import org.example.domain.Node;

public class MyBlockingLinkedList<T> {
    public final Node head = new Node(null, null, null);
    public final Node tail = new Node(null, null, null);

    public MyBlockingLinkedList() {
        head.next = tail;
        tail.previous = head;
    }

    public void sort() {
        boolean sorted=false;
        do {


        } while (!sorted);
    }

    public Node update(T nodeData) {
        Node current = head.next;
        if (head.next != tail) {
            while (current.isNotLastNode()) {
                current.lock();
                if (current.equals(nodeData)) {
                    //current.getParticipant().setScore(current.getParticipant().getScore() + participant.getScore());
                    current.unlock();
                    return current;
                }
                current.unlock();
                current = current.next;
            }
        }
        return null;
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

    public void delete(T nodeData) {
        head.lock();
        head.next.lock();
        if (head.next == tail) {
            head.unlock();
            head.next.unlock();
            return;
        }
        Node current = head.next;

        while (current.isNotLastNode()) {
            current.next.lock();
//            if (current.getData().getId().equals(nodeData.getId())) {
//                Node left = current.previous;
//                Node right = current.next;
//
//                left.next = right;
//                right.previous = left;
//                left.unlock();
//                current.unlock();
//                right.unlock();
//                return;
//            }
            current.previous.unlock();
            current = current.next;
        }

        current.previous.unlock();
        current.unlock();
    }
}