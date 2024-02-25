package org.example;

import org.example.datastructure.MyBlockingQueue;
import org.example.domain.Inregistrare;
import org.example.domain.Reteta;
import org.example.util.IOHandler;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {
    private static int N = 100; // numarul de pacienti
    private static final AtomicInteger totalPacientTasks = new AtomicInteger(N);
    private static MyBlockingQueue<Reteta> banda = new MyBlockingQueue<>(totalPacientTasks);
    private static int M = 4; // numarul de farmacisti
    private static final AtomicInteger totalFarmacistTasks = new AtomicInteger(M);
    private static MyBlockingQueue<Inregistrare> comenzi = new MyBlockingQueue<>(totalFarmacistTasks);
    protected static int codRetete = 1;

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        List<Thread> pacienti = new ArrayList<>();
        List<Thread> farmacisti = new ArrayList<>();

        //IOHandler.genereazaMedicamente();
        IOHandler.cleanFile("src/main/resources/Inregistrari.txt");

        Pacient pacient = new Pacient(banda);
        Farmacist farmacist = new Farmacist(banda, comenzi);
        Casier casier = new Casier(comenzi);

        Thread casierWorker = new Thread(casier);
        for (int i = 0; i < N; i++) {
            Thread pacientWorker = new Thread(pacient);
            pacienti.add(pacientWorker);
        }

        for (int i = 0; i < M; i++) {
            Thread farmacistWorker = new Thread(farmacist);
            farmacisti.add(farmacistWorker);
        }

        casierWorker.start();
        farmacisti.forEach(Thread::start);
        pacienti.forEach(Thread::start);


        pacienti.forEach(pr -> {
            try {
                pr.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        farmacisti.forEach(cn -> {
            try {
                cn.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        try {
            casierWorker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
    }


    static class Pacient implements Runnable {
        private final MyBlockingQueue<Reteta> banda;

        public Pacient(MyBlockingQueue<Reteta> banda) {
            this.banda = banda;
        }

        public void Pune() throws InterruptedException {
            Random rand = new Random();
            int cod;
            synchronized (this) {
                cod = codRetete++;
            }
            int nrMedicamente = rand.nextInt(5) + 1;
            List<Long> medicamente = new ArrayList<>();
            for (int i = 0; i < nrMedicamente; i++) {
                long codMedicament = rand.nextInt(30) + 1;
                medicamente.add(codMedicament);
            }
            Reteta reteta = new Reteta(cod, nrMedicamente, medicamente);

            banda.put(reteta);
            System.out.println("Reteta nr " + cod + " a fost generata de catre pacientul " + Thread.currentThread().getName());

            synchronized (banda) {
                banda.notify();
            }
        }

        @Override
        public void run() {
            try {
                Pune();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (this) {
                if (totalPacientTasks.decrementAndGet() == 0) {
                    banda.finish();
                    synchronized (banda) {
                        //System.out.println("Pacientii au terminat de generat retete");
                        banda.notifyAll();
                    }
                }
            }
        }
    }


    static class Farmacist extends Thread {
        private final MyBlockingQueue<Reteta> banda;
        private final MyBlockingQueue<Inregistrare> comenzi;

        public Farmacist(MyBlockingQueue<Reteta> newBanda, MyBlockingQueue<Inregistrare> newOperatii) {
            banda = newBanda;
            comenzi = newOperatii;
        }

        public void Preia() throws InterruptedException {
            while (true) {
                while (banda.isEmpty() && totalPacientTasks.get() != 0) {
                    try {
                        synchronized (banda) {
                            banda.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (banda.isEmpty() && totalPacientTasks.get() == 0) {
                    break;
                } else {
                    if (!banda.isEmpty()) {
                        Reteta reteta = banda.take();
                        System.out.println("Reteta nr " + reteta.getCodReteta() + " a fost preluata de catre farmacistul " + Thread.currentThread().getName());
                        if (reteta != null) {
                            int sum = 0;
                            for (int i = 0; i < reteta.getNrMedicamente(); i++) {
                                long codMedicament = reteta.getListaMedicamente().get(i);
                                try {
                                    File myObj = new File("src/main/resources/Medicamente.txt");
                                    Scanner myReader = new Scanner(myObj);
                                    String linie = "";
                                    int j = 0;
                                    while (myReader.hasNext() && j < codMedicament) {
                                        linie = myReader.nextLine();
                                        j++;
                                    }
                                    sum += Integer.parseInt(linie.split(" ")[1]);
                                } catch (FileNotFoundException e) {
                                    System.out.println("An error occurred.");
                                    e.printStackTrace();
                                }
                            }
                            System.out.println("Reteta nr " + reteta.getCodReteta() + " a fost pregatita si pretul este de " + sum);
                            Inregistrare inregistrare = new Inregistrare(comenzi.size() + 1, sum, reteta.getCodReteta());
                            comenzi.put(inregistrare);
                            synchronized (comenzi) {
                                comenzi.notify();
                                System.out.println("Reteta nr " + reteta.getCodReteta() + " a fost adaugata in coada de la casierie ");
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void run() {
            try {
                Preia();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (this) {
                if (totalFarmacistTasks.decrementAndGet() == 0) {
                    banda.finish();
                    synchronized (comenzi) {
                        //System.out.println("Farmacistii au terminat de preluat retete");
                        comenzi.notifyAll();
                    }
                }
            }
        }
    }

    static class Casier implements Runnable {
        private final MyBlockingQueue<Inregistrare> comenzi;
        public Casier(MyBlockingQueue<Inregistrare> banda) {
            this.comenzi = banda;
        }

        @Override
        public void run() {
            while (true) {
                while (comenzi.isEmpty() && totalFarmacistTasks.get() != 0) {
                    try {
                        synchronized (comenzi) {
                            comenzi.wait();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (comenzi.isEmpty() && totalFarmacistTasks.get() == 0) {
                    return;
                } else {
                    Inregistrare inregistrare;
                    try {
                        inregistrare = comenzi.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/Inregistrari.txt", true))) {
                        bw.write(inregistrare.getNr_Factura() + " " + inregistrare.getValoare() + " " + inregistrare.getCodReteta());
                        bw.newLine();
                        System.out.println("Reteta nr " + inregistrare.getCodReteta() + " a fost platita si preluata");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
