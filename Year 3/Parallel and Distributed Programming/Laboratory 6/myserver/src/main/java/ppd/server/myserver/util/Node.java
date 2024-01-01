package ppd.server.myserver.util;

import lombok.Getter;
import lombok.Setter;
import ppd.server.myserver.entity.Participant;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
public class Node {
    public final Lock lock = new ReentrantLock();
    private Participant participant;
    public Node next;
    public Node previous;

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public Node(Participant data, Node next, Node previous) {
        this.participant = data;
        this.next = next;
        this.previous = previous;
    }
    public boolean isNotLastNode() {
        return participant != null;
    }
}
