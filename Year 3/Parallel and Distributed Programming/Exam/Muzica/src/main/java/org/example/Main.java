package org.example;

import org.example.datastructure.MyBlockingQueue;
import org.example.datastructure.MyConcurrentDictionary;
import org.example.domain.Mesaj;
import org.example.util.IOHandler;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Main {
    private static int P = 4;
    private static int C = 8;
    private static final AtomicInteger totalAdauga = new AtomicInteger(P);
    private static final AtomicInteger totalConsuma = new AtomicInteger(C);
    private static final AtomicInteger totalMesajeTrimise = new AtomicInteger(0);

    private static Lock myLock = new ReentrantLock();
    private static MyBlockingQueue<Mesaj> mesaje = new MyBlockingQueue<>(totalAdauga, myLock);
    private static MyConcurrentDictionary sortatPiesa = new MyConcurrentDictionary();
    private static MyConcurrentDictionary sortatCompozitor = new MyConcurrentDictionary();

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        //IOHandler.createRandomFiles();

        IOHandler.cleanFile("fisier.log");

        List<String> fileNames = new LinkedList<>();
        for (int i = 0; i < 8; i++) {
            fileNames.add("/Muzica" + i + ".txt");
        }

        // imparte pentru 4 threaduri
        List<ThreadCeCiteste> threads = new ArrayList<>();
        for (int i = 0; i < P; i++) {
            threads.add(new ThreadCeCiteste(mesaje, fileNames.subList(i * 2, i * 2 + 2)));
        }

        for (var thread : threads) {
            thread.start();
        }

        List<ThreadCeConsuma> threadsConsuma = new ArrayList<>();
        for (int i = 0; i < C; i++) {
            threadsConsuma.add(new ThreadCeConsuma(mesaje, sortatPiesa, sortatCompozitor));
        }

        for (var thread : threadsConsuma) {
            thread.start();
        }

        Thread iterator = new Thread(new Iterator(mesaje, sortatPiesa, sortatCompozitor));
        iterator.start();

        // join

        for (var thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (var thread : threadsConsuma) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            iterator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
    }


    static class ThreadCeCiteste extends Thread {
        private final MyBlockingQueue<Mesaj> mesaje;
        private final List<String> fileNames;

        public ThreadCeCiteste(MyBlockingQueue<Mesaj> mesaje, List<String> fileNames) {
            this.mesaje = mesaje;
            this.fileNames = fileNames;
        }

        @Override
        public void run() {
            try {
                System.out.println("Threadul " + Thread.currentThread().getName() + " a inceput sa citeasca");
                for (String fileName : fileNames) {
                    System.out.println("fileName = " + fileName);
                    List<Mesaj> mesajeCitite = IOHandler.read(fileName);
                    System.out.println("mesajeCitite = " + mesajeCitite.size());
                    for (Mesaj mesaj : mesajeCitite) {
                        mesaje.put(mesaj);
                        totalMesajeTrimise.incrementAndGet();

                    }
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (this) {
                if (totalAdauga.decrementAndGet() == 0) {
                    mesaje.finish();
                }
            }
        }
    }

    static class ThreadCeConsuma extends Thread {
        private final MyBlockingQueue<Mesaj> mesaje;
        private final MyConcurrentDictionary sortatDupaNumePiesa;
        private final MyConcurrentDictionary sortatDupaNumeCompozitor;

        public ThreadCeConsuma(MyBlockingQueue<Mesaj> mesaje, MyConcurrentDictionary sortatDupaNumePiesa, MyConcurrentDictionary sortatDupaNumeCompozitor) {
            this.mesaje = mesaje;
            this.sortatDupaNumePiesa = sortatDupaNumePiesa;
            this.sortatDupaNumeCompozitor = sortatDupaNumeCompozitor;
        }

        private void ConsumaMesaje() {
            while (!mesaje.isEmpty() || totalAdauga.get() > 0) {
                try {
                    Mesaj mesaj = mesaje.take();
                    if (mesaj != null) {
                        String prima = mesaj.getPiesa().substring(0, 1);
                        sortatDupaNumePiesa.addToDictionary(prima, mesaj);
                        prima = mesaj.getCompozitor().substring(0, 1);
                        sortatDupaNumeCompozitor.addToDictionary(prima, mesaj);
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            ConsumaMesaje();
            synchronized (this) {
                totalConsuma.decrementAndGet();
            }
        }
    }

    static class Iterator extends Thread {
        private final MyBlockingQueue<Mesaj> mesaje;
        private final MyConcurrentDictionary sortatPiesa;
        private final MyConcurrentDictionary sortatCompozitor;

        public Iterator(MyBlockingQueue<Mesaj> mesaje, MyConcurrentDictionary sortatPiesa, MyConcurrentDictionary sortatCompozitor) {
            this.mesaje = mesaje;
            this.sortatPiesa = sortatPiesa;
            this.sortatCompozitor = sortatCompozitor;
        }

        private boolean verificaDacaPotAfisa() {
            long sizeCoada = mesaje.size();
            long sizeDictPiesa = sortatPiesa.size();
            long sizeDictCompozitor = sortatCompozitor.size();
            System.out.println("total mesaje = " + 2*totalMesajeTrimise.get() + "suma = " + (2*sizeCoada + sizeDictPiesa + sizeDictCompozitor));
            if ( sizeDictPiesa + sizeDictCompozitor + 2*sizeCoada == 2 * totalMesajeTrimise.get()) {
                return true;
            }
            return false;
        }

        private void bilantFinal(){
            String bilant = "\nBilant final: \n";
            bilant += "mesaje citite: " + totalMesajeTrimise.get() + "\n";
            long sizePiease = sortatPiesa.size();
            long sizeCompozitori = sortatCompozitor.size();

            bilant += "mesaje in dictionare: " + (sizePiease + sizeCompozitori) + "\n";
            IOHandler.writeText(bilant);
            System.out.println(bilant);
        }

        @Override
        public void run() {
            while (true) {
                while (totalConsuma.get() != 0 || totalAdauga.get() != 0) {
                    boolean potAfisa = verificaDacaPotAfisa();
                    if (potAfisa) {
                        IOHandler.writeText("Da, pot afisa la timpul " + System.currentTimeMillis() + "\n");
                    }
                    else {
                        IOHandler.writeText("Nu, nu pot afisa la timpul " + System.currentTimeMillis() + "\n");
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (totalConsuma.get() == 0) {
                    bilantFinal();
                    break;
                }
            }
        }
    }
}
