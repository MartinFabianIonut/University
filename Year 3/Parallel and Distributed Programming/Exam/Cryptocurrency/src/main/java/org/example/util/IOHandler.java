package org.example.util;

import org.example.datastructure.MyBlockingLinkedList;
import org.example.domain.Tranzactie;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
public class IOHandler {
    public static void createRandomTranzactii(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/blockchain.txt"))) {
            for (int i = 1; i <= 50; ++i) {
                int codWalletUtilizator = -1;
                int valoare = 100;
                int codWalletDestinatar = i;
                bw.write(codWalletUtilizator + "," + valoare + "," + codWalletDestinatar + ",-1\n");
            }
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static List<Tranzactie> readTranzactii() {
        List<Tranzactie> tranzactii = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/blockchain.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                tranzactii.add(new Tranzactie(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tranzactii;
    }

    public synchronized static void appendTranzactie(Tranzactie tranzactie){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/blockchain.txt", true))) {
            bw.write(tranzactie.getCodWalletUtilizator() + "," + tranzactie.getValoare() + "," + tranzactie.getCodWalletDestinatar() + ","+tranzactie.getIdSupervizor()+"\n");
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
