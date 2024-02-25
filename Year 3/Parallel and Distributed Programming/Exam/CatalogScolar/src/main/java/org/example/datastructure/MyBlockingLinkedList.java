package org.example.datastructure;

import org.example.domain.Cursant;
import org.example.domain.Node;

public class MyBlockingLinkedList {
    public final Node head = new Node(null, null, null);
    public final Node tail = new Node(null, null, null);

    public MyBlockingLinkedList() {
        head.next = tail;
        tail.previous = head;
    }

    public void add(Cursant nodeData) {
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

    public void printUnderMark5(){
        System.out.println("Cursanti cu media sub 5:");
        Node current = head.next;
        while (current.isNotLastNode()) {
            current.lock();
            if (current.getData().getMedie()<5) {
                System.out.println(current.getData().getId() + " " + current.getData().getMedie());
            }
            current.unlock();
            current = current.next;
        }
        System.out.println();
    }

    public void print(){
        System.out.println("Cursanti:");
        Node current = head.next;
        while (current.isNotLastNode()) {
            current.lock();
            System.out.println(current.getData().getId() + " " + current.getData().getMedie());
            current.unlock();
            current = current.next;
        }
        System.out.println();
    }
}