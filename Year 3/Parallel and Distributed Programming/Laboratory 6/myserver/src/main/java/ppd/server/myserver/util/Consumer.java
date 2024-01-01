package ppd.server.myserver.util;

import ppd.server.myserver.datastructure.MyBlackList;
import ppd.server.myserver.datastructure.MyBlockingLinkedList;
import ppd.server.myserver.datastructure.MyBlockingQueue;
import ppd.server.myserver.entity.Participant;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer extends Thread {

    private final MyBlockingQueue myBlockingQueue;

    private final MyBlackList blackList;

    private final MyBlockingLinkedList resultList;

    private final Map<String, ReentrantLock> locks;

    private final AtomicInteger totalProducerTasks;

    public Consumer(AtomicInteger totalProducerTasks, MyBlockingQueue myBlockingQueue, MyBlackList blackList,
                    MyBlockingLinkedList resultList, Map<String, ReentrantLock> locks) {
        this.totalProducerTasks = totalProducerTasks;
        this.myBlockingQueue = myBlockingQueue;
        this.blackList = blackList;
        this.resultList = resultList;
        this.locks = locks;
    }

    @Override
    public void run() {
        while (totalProducerTasks.get() != 0 || !myBlockingQueue.isEmpty()) {
            Participant participant = null;
            try {
                participant = myBlockingQueue.take();
            } catch (InterruptedException ignored) {
            }

            if (participant == null) {
                myBlockingQueue.finish();
                continue;
            }

            locks.get(participant.getId()).lock();

            if (!blackList.contains(new Pair(participant.getId(), participant.getCountry()))) {
                if (participant.getScore() == -1) {
                    resultList.delete(participant);
                    blackList.add(new Pair(participant.getId(), participant.getCountry()));
                } else {
                    Node current = resultList.update(participant);

                    if (current == null) {
                        resultList.add(participant);
                    }
                }
            }

            locks.get(participant.getId()).unlock();
        }
    }
}