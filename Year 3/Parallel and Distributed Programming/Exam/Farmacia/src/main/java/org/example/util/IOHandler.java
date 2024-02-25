package org.example.util;

import org.example.datastructure.MyBlockingLinkedList;
import org.example.domain.Inregistrare;
import org.example.domain.Node;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
public class IOHandler {

    public static void genereazaMedicamente() {
        Random rand = new Random();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/Medicamente.txt"))) {
            for (int i = 1; i <= 30; i++) {
                int cod = rand.nextInt(100) + 1;
                bw.write(i + " " + cod);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Eroare la scrierea in fisier");
        }
    }

    public static void printFactura(Inregistrare inregistrare){
        System.out.println("Reteta nr " + inregistrare.getCodReteta() + " a fost preluata");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Inregistrari.txt"))) {
            bw.write(inregistrare.getNr_Factura() + " " + inregistrare.getValoare() + " " + inregistrare.getCodReteta());
            bw.newLine();
            System.out.println("Reteta nr " + inregistrare.getCodReteta() + " a fost platita");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cleanFile(String s) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(s))) {
            bw.write("");
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
