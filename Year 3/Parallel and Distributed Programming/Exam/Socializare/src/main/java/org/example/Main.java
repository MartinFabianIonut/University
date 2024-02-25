package org.example;

import org.example.datastructure.MyConcurrentDictionary;
import org.example.domain.Mesaj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {
    private static int N = 15;
    private static final AtomicInteger totalAdauga = new AtomicInteger(2);
    private static MyConcurrentDictionary prieteni = new MyConcurrentDictionary();

    private static Boolean potAfisa = false;

    private List<Mesaj> generateMessages() {
        List<Mesaj> mesaje = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int prieten = new Random().nextInt(100);
            char literaRandom = (char) (new Random().nextInt(26) + 'a');
            mesaje.add(new Mesaj(literaRandom + "_nume", "prieten_" + prieten));
        }
        return mesaje;
    }

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        List<Mesaj> mesaje = new Main().generateMessages();
        List<Mesaj> jumatate = mesaje.subList(0, mesaje.size() / 2);
        List<Mesaj> cealaltaJumatate = mesaje.subList(mesaje.size() / 2, mesaje.size());

        ThreadCeAdauga threadCeAdauga = new ThreadCeAdauga(prieteni, jumatate);
        ThreadCeAdauga threadCeAdauga2 = new ThreadCeAdauga(prieteni, cealaltaJumatate);
        Iterator iterator = new Iterator(prieteni);

        Thread t1 = new Thread(threadCeAdauga);
        Thread t2 = new Thread(threadCeAdauga2);
        t1.start();
        t2.start();
        Thread t3 = new Thread(iterator);
        t3.start();
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
    }


    static class ThreadCeAdauga implements Runnable {
        private final MyConcurrentDictionary prieteni;
        private final List<Mesaj> mesaje;

        public ThreadCeAdauga(MyConcurrentDictionary prieteni, List<Mesaj> mesaje) {
            this.prieteni = prieteni;
            this.mesaje = mesaje;
        }

        public int Pune(Mesaj mesaj) {
            return prieteni.addToDictionary(mesaj.getNume_persoana(), mesaj.getNume_prieten());
        }


        @Override
        public void run() {
            try {
                for (Mesaj mesaj : mesaje) {
                    int size = Pune(mesaj);
                    synchronized (prieteni) {
                        if (size % N == 0) {
                            System.out.println("Am adaugat " + size + " prieteni");
                            potAfisa = true;
                            prieteni.notify();
                            prieteni.wait();
                            System.out.println("Am terminat de ASTEPTAT");
                        }
                    }
                    long random = new Random().nextInt(10);
                    Thread.sleep(random);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (this) {
                if (totalAdauga.decrementAndGet() == 0) {

                    synchronized (prieteni) {System.out.println("Threadul " + Thread.currentThread().getName() + " a terminat");
                        potAfisa = true;
                        prieteni.notifyAll();
                    }
                }
            }
        }
    }

    static class Iterator implements Runnable {
        private final MyConcurrentDictionary prieteni;

        public Iterator(MyConcurrentDictionary prieteni) {
            this.prieteni = prieteni;
        }

        @Override
        public void run() {
            while (true) {
                while (!potAfisa) {
                    try {
                        synchronized (prieteni) {
                            prieteni.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (totalAdauga.get() == 0) {
                    break;
                }
                this.afiseazaPrieteni();
            }
        }

        private void afiseazaPrieteni() {
            synchronized (prieteni) {
                System.out.println("AFISARE");
                prieteni.afisare();
                potAfisa = false;
                prieteni.notify();
            }
        }
    }
}
