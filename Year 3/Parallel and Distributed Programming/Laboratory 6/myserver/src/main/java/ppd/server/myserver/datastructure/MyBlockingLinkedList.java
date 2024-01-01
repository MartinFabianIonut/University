package ppd.server.myserver.datastructure;

import ppd.server.myserver.entity.Participant;
import ppd.server.myserver.util.Node;

import java.util.ArrayList;
import java.util.List;

public class MyBlockingLinkedList {
    public final Node head = new Node(null, null, null);
    public final Node tail = new Node(null, null, null);

    public MyBlockingLinkedList() {
        head.next = tail;
        tail.previous = head;
    }

    public void sort() {
        boolean sorted;
        do {
            sorted = true;

            Node current = head.next;

            while (current.next != tail) {
                if (current.getParticipant().getScore() < current.next.getParticipant().getScore()) {
                    Participant temporaryData = current.getParticipant();
                    current.setParticipant(current.next.getParticipant());
                    current.next.setParticipant(temporaryData);
                    sorted = false;
                }
                current = current.next;
            }

        } while (!sorted);
    }

    public Node update(Participant participant) {
        Node current = head.next;
        if (head.next != tail) {
            while (current.isNotLastNode()) {
                current.lock();
                if (current.getParticipant().getId().equals(participant.getId())) {
                    current.getParticipant().setScore(current.getParticipant().getScore() + participant.getScore());
                    current.unlock();
                    return current;
                }
                current.unlock();
                current = current.next;
            }
        }
        return null;
    }

    public void add(Participant participant) {
        head.lock();
        head.next.lock();

        Node right = head.next;

        Node node = new Node(participant, null, null);
        node.lock();

        head.next = node;
        node.previous = head;
        node.next = right;
        right.previous = node;

        right.unlock();
        node.unlock();
        head.unlock();
    }

    public void delete(Participant participant) {
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
            if (current.getParticipant().getId().equals(participant.getId())) {
                Node left = current.previous;
                Node right = current.next;

                left.next = right;
                right.previous = left;
                left.unlock();
                current.unlock();
                right.unlock();
                return;
            }
            current.previous.unlock();
            current = current.next;
        }

        current.previous.unlock();
        current.unlock();
    }

    public List<Participant> toList() {
        List<Participant> list = new ArrayList<>();
        Node current = head.next;
        while (current.isNotLastNode()) {
            list.add(current.getParticipant());
            current = current.next;
        }
        return list;
    }

    public void deleteAll() {
        head.next = tail;
        tail.previous = head;
    }
}
