package org.example.util;

import org.example.domain.Mesaj;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
public class IOHandler {

    public synchronized static void writeText(String text) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("fisier.log", true))){
            fileWriter.write(text);
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createRandomFiles(){
        // generate 8 finenames
        List<String> genuri = new LinkedList<>();
        genuri.add("rock");
        genuri.add("pop");
        genuri.add("clasic");
        genuri.add("populara");

          List<String> fileNames  = new LinkedList<>();
        for (int i = 0; i < 8; i++) {
            fileNames.add("Muzica" + i + ".txt");
        }
        for (String fileName : fileNames) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/inputs/" + fileName))) {
                for (int i = 0; i < 100; i++) {
                    String charRandom = "abcdefghijklmnopqrstuvwxyz";
                    Random rand = new Random();
                    String randomChar = String.valueOf(charRandom.charAt(rand.nextInt(charRandom.length())));
                    String piesa = randomChar + "piesa" + i;
                    randomChar = String.valueOf(charRandom.charAt(rand.nextInt(charRandom.length())));
                    String compozitor = randomChar + "compozitor" + i;
                    String gen = genuri.get(i % 4);

                    bw.write(piesa + " " + compozitor + " " + gen + "\n");
                }
                bw.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized static List<Mesaj> read(String filename) {
        List<Mesaj> mesaje = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/inputs" + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                mesaje.add(new Mesaj(parts[0], parts[1], parts[2]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mesaje;
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
