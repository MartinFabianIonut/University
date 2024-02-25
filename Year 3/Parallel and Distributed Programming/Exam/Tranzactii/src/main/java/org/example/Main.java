package org.example;

import org.example.datastructure.MyDictionary;
import org.example.util.IOHandler;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {


    private static int N = 100;
    private static int producersNumber = 4;
    private static MyDictionary bancaDictionary;
    private static AtomicInteger stillRunning = new AtomicInteger(producersNumber);

    public static void main(String[] args) {

        IOHandler.cleanFile("src/main/resources/tranzactii.log");
        bancaDictionary = new MyDictionary();
        ArrayList<ProducerThread> producerThreads = new ArrayList<>();
        for (int i = 0; i < producersNumber / 2; i++) {
            ProducerThread producerThread = new ProducerThread("debit", "client_" + i);
            producerThreads.add(producerThread);
        }
        for (int i = 0; i < producersNumber / 2; i++) {
            ProducerThread producerThread = new ProducerThread("credit", "client_" + i);
            producerThreads.add(producerThread);
        }

        IteratorThread iteratorThread = new IteratorThread();
        for (ProducerThread producerThread : producerThreads) {
            producerThread.start();
        }
        iteratorThread.start();
        for (ProducerThread producerThread : producerThreads) {
            try {
                producerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            iteratorThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class IteratorThread extends Thread {


        @Override
        public void run() {
            while (stillRunning.get() > 0) {
                bancaDictionary.lock.lock();


                while (bancaDictionary.transactionsAdded.get() % 50 != 0 && stillRunning.get()>0 || bancaDictionary.transactionsAdded.get() == 0){
                    try {
                        bancaDictionary.conditionReader.await();

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (stillRunning.get() == 0) {
                    if (bancaDictionary.transactionsAdded.get() % 50 == 0) {
                        Iterator<Map.Entry<String, HashMap<String, Integer>>> iterator = bancaDictionary.iterator();
                        IOHandler.writeText("\n");
                        while (iterator.hasNext()) {
                            Map.Entry<String, HashMap<String, Integer>> entry = iterator.next();
                            IOHandler.writeText(entry.getKey() + " : " + entry.getValue()+"\n");

                        }
                        IOHandler.writeText("\n");
                    }
                    bancaDictionary.lock.unlock();
                    break;
                }

                IOHandler.writeText("\n");
                Iterator<Map.Entry<String, HashMap<String, Integer>>> iterator = bancaDictionary.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, HashMap<String, Integer>> entry = iterator.next();
                    IOHandler.writeText(entry.getKey() + " : " + entry.getValue()+"\n");
                }
                IOHandler.writeText("\n");



                bancaDictionary.transactionsAdded.incrementAndGet();
                bancaDictionary.conditionWorker.signalAll();
                bancaDictionary.lock.unlock();
            }
        }
    }

    private static class ProducerThread extends Thread {
        private String transactionType;
        private String clientId;

        public ProducerThread(String transactionType, String clientId) {
            this.transactionType = transactionType;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            for (int i = 0; i < N; i++) {
                var random = new Random().nextInt(500) + 1;
                if (!bancaDictionary.verifyKey(clientId)) {
                    bancaDictionary.insertKey(clientId);
                }
                bancaDictionary.pushValue(clientId, transactionType, random);

            }
            stillRunning.decrementAndGet();
            bancaDictionary.lock.lock();
            bancaDictionary.conditionReader.signal();
            bancaDictionary.lock.unlock();

        }
    }
}
