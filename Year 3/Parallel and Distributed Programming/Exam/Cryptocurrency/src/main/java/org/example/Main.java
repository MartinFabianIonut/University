package org.example;

import org.example.datastructure.MyBlockingQueue;
import org.example.domain.Tranzactie;
import org.example.util.IOHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {

    private static int N = 50;
    private static int M = 4;
    private static final AtomicInteger totalUsersTasks = new AtomicInteger(N);
    private static final AtomicInteger totalSupervisorsTasks = new AtomicInteger(M);
    private static final MyBlockingQueue<Tranzactie> processingQueue = new MyBlockingQueue<>(totalUsersTasks);
    private static final MyBlockingQueue<Tranzactie> acceptedQueue = new MyBlockingQueue<>(totalSupervisorsTasks);


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        IOHandler.createRandomTranzactii();

        List<Thread> utilizatori = new ArrayList<>();
        List<Thread> supervizori = new ArrayList<>();

        for (int i = 0; i < N; ++i) {
            utilizatori.add(new Utilizator(processingQueue));
        }
        for (int i = 0; i < M; ++i) {
            supervizori.add(new Supervisor(processingQueue, acceptedQueue));
        }
        Thread miner = new Miner(acceptedQueue);

        for (var utilizator : utilizatori) {
            utilizator.start();
        }
        for (var supervizor : supervizori) {
            supervizor.start();
        }
        miner.start();
        for (var utilizator : utilizatori) {
            try {
                utilizator.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (var supervizor : supervizori) {
            try {
                supervizor.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            miner.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total time: " + (endTime - startTime) + "ms");

    }

    private static class Utilizator extends Thread
    {
        private final MyBlockingQueue<Tranzactie> processingQueue;

        private void addTransaction() throws InterruptedException {
            Random random = new Random();
            int codWalletUtilizator = random.nextInt(N);
            int valoare = random.nextInt(200) + 1;
            int codWalletDestinatar = random.nextInt(70);
            Tranzactie tranzactie = new Tranzactie(codWalletUtilizator, valoare, codWalletDestinatar);

            processingQueue.push(tranzactie);

        }

        @Override
        public void run() {
            try {
                addTransaction();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this) {
                if (totalUsersTasks.decrementAndGet() == 0) {
                    processingQueue.finish();
                }
            }
        }

        public Utilizator(MyBlockingQueue<Tranzactie> processingQueue) {
            this.processingQueue = processingQueue;
        }
    }

    private static class Supervisor extends Thread {
        private final MyBlockingQueue<Tranzactie> processingQueue;
        private final MyBlockingQueue<Tranzactie> acceptedQueue;

        private boolean validateTranzactie(Tranzactie tranzactie) {
            var tranzactiiHistory = IOHandler.readTranzactii();

            int value = 0;
            for (var tran : tranzactiiHistory) {
                if (tran.getCodWalletDestinatar().equals(tranzactie.getCodWalletUtilizator())) {
                    value += tran.getValoare();
                }
                if (tran.getCodWalletUtilizator().equals(tranzactie.getCodWalletUtilizator())) {
                    value -= tran.getValoare();
                }
            }
            if (value < tranzactie.getValoare() || tranzactie.getCodWalletDestinatar() > N) {
                System.out.println("Tranzactia initia de wallet " + tranzactie.getCodWalletUtilizator() + " catre wallet " + tranzactie.getCodWalletDestinatar() + " a fost respinsa de supervizorul " + Thread.currentThread().getId() + ", sold total inainte de operatie: " + value);
                return false;
            }
            System.out.println("Tranzactia initia de wallet " + tranzactie.getCodWalletUtilizator() + " catre wallet " + tranzactie.getCodWalletDestinatar() + " a fost acceptata de supervizorul " + Thread.currentThread().getId() + ", sold total inainte de operatie: " + value);
            return true;
        }

        private void verifyTransaction() throws InterruptedException {
            while (totalUsersTasks.get() != 0 || !processingQueue.isEmpty()) {
                Tranzactie tranzactie = processingQueue.pop();

                if (tranzactie != null) {
                    System.out.println("Tranzactia initia de wallet " + tranzactie.getCodWalletUtilizator() + " a fost preluata de supervizorul " + Thread.currentThread().getId());
                    if (validateTranzactie(tranzactie)) {
                        tranzactie.setIdSupervizor((int) Thread.currentThread().getId());
                        acceptedQueue.push(tranzactie);
                    }
                }
            }
        }

        @Override
        public void run() {
            try {
                verifyTransaction();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this) {
                if (totalSupervisorsTasks.decrementAndGet() == 0) {
                    acceptedQueue.finish();
                }
            }
        }

        public Supervisor(MyBlockingQueue<Tranzactie> processingQueue, MyBlockingQueue<Tranzactie> acceptedQueue) {
            this.processingQueue = processingQueue;
            this.acceptedQueue = acceptedQueue;
        }
    }

    private static class Miner extends Thread {
        private final MyBlockingQueue<Tranzactie> acceptedQueue;

        public Miner(MyBlockingQueue<Tranzactie> acceptedQueue) {
            this.acceptedQueue = acceptedQueue;
        }

        @Override
        public void run() {
            try {
                while (totalSupervisorsTasks.get() != 0 || !acceptedQueue.isEmpty()) {
                    Tranzactie tranzactie = acceptedQueue.pop();
                    if (tranzactie != null) {
                        IOHandler.appendTranzactie(tranzactie);
                        System.out.println("Minerul " + Thread.currentThread().getId() + " a salvat tranzactia initiata de " + tranzactie.getCodWalletUtilizator() + "cu valoarea " + tranzactie.getValoare() + " catre " + tranzactie.getCodWalletDestinatar());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
