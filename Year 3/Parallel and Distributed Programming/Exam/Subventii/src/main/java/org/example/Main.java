package org.example;

import org.example.datastructure.MyBlockingQueue;
import org.example.domain.Cerere;
import org.example.util.IOHandler;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final MyBlockingQueue<Cerere> cereri = new MyBlockingQueue<>();
    private static final int N = 100;
    private static final int M = 4;

    private static final AtomicInteger cetateniDone = new AtomicInteger(N);
    private static final AtomicInteger angajatiDone = new AtomicInteger(M);
    private static final AtomicInteger fonduriDisponibile = new AtomicInteger(0);
    private static final AtomicInteger sumaAlocata = new AtomicInteger(0);


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        List<Thread> cetateni = new ArrayList<>();
        List<Thread> angajati = new ArrayList<>();

        IOHandler.cleanFile("src/main/resources/Cereri.txt");
        Thread supervizor = new Supervizor();

        for (int i = 0; i < N; i++) {
            Thread cetatean = new Thread(new Cetatean());
            cetateni.add(cetatean);
        }

        for (int i = 0; i < M; i++) {
            Thread angajat = new Thread(new Angajat());
            angajati.add(angajat);
        }

        supervizor.start();
        cetateni.forEach(Thread::start);
        angajati.forEach(Thread::start);

        cetateni.forEach(pr -> {
            try {
                pr.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        angajati.forEach(cn -> {
            try {
                cn.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        try {
            supervizor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
    }

    static class Cetatean extends Thread {

        @Override
        public void run() {
            try {
                adaugaCerere();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void adaugaCerere() throws InterruptedException {
            Cerere cerere = new Cerere((int) (Math.random() * 30 + 1));
            cereri.put(cerere);
            cetateniDone.decrementAndGet();
        }
    }

    static class Angajat extends Thread {

        @Override
        public void run() {
            try {
                proceseazaCereri();
                angajatiDone.decrementAndGet();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        private void proceseazaCereri() throws InterruptedException {

            while (!cereri.isEmpty() || cetateniDone.get() > 0) {
                Cerere cerere = cereri.take();
                if (cerere != null) {
                    int valoarea = cerere.getValoare();
                    synchronized (fonduriDisponibile) {
                        synchronized (sumaAlocata) {
                            if (fonduriDisponibile.get() >= valoarea) {
                                sumaAlocata.set(sumaAlocata.get() + valoarea);
                                System.out.printf("Cererea cu nr %d a fost acceptata de catre angajatul " +
                                                "%s deoarece valoarea ceruta %d este mai mica decat fondurile " +
                                                "disponibile %d\n", cerere.getCod(), Thread.currentThread().getName(),
                                        cerere.getValoare(), fonduriDisponibile.get());
                                fonduriDisponibile.set(fonduriDisponibile.get() - valoarea);
                                IOHandler.addCerereToFile("src/main/resources/Cereri.txt", cerere,
                                        "Acceptata", fonduriDisponibile);
                            } else {
                                IOHandler.addCerereToFile("src/main/resources/Cereri.txt", cerere,
                                        "Respinsa", fonduriDisponibile);
                                System.out.printf("Cererea cu nr %d NUUU a fost acceptata de catre angajatul " +
                                        "%s deoacere valoarea ceruta %d este mai mare decat fondurile " +
                                        "disponibile %d\n", cerere.getCod(), Thread.currentThread().getName(),
                                        cerere.getValoare(), fonduriDisponibile.get());
                            }
                        }
                    }
                }
            }
        }
    }


    static class Supervizor extends Thread {

        @Override
        public void run() {
            while (cetateniDone.get() > 0 || angajatiDone.get() > 0) {
                int valoriCereri = IOHandler.checkFonduriAlocate("src/main/resources/Cereri.txt");
                //random intre 100 si 200
                int valoare = (int) (Math.random() * 100 + 100);
                synchronized (fonduriDisponibile) {
                    synchronized (sumaAlocata) {
                        if (valoriCereri <= sumaAlocata.get() + fonduriDisponibile.get()) {
                            System.out.printf("VERIFICARE VALIDA: Supervizorul a verificat ca valoarea " +
                                            "fondurilor alocate %d, este mai mare decat suma tuturor cererilor acceptate %d si a " +
                                            "ajustat fondurile adaugand %d RON\n", sumaAlocata.get() + fonduriDisponibile.get(),
                                    valoriCereri, valoare);
                        } else {
                            System.out.printf("VERIFICARE INVALIDA: Supervizorul a verificat ca valoarea fondurilor " +
                                            "alocate %d, este mai mica decat suma tuturor cererilor acceptate %d si a ajustat " +
                                            "fondurile adaugand %d RON\n", sumaAlocata.get() + fonduriDisponibile.get(),
                                    valoriCereri, valoare);
                        }
                        fonduriDisponibile.set(valoare + fonduriDisponibile.get());
                    }
                }
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
