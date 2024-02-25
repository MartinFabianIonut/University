package org.example.util;

import org.example.domain.Cerere;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class IOHandler {
    public static void cleanFile(String s) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(s))) {
            bw.write("");
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static void addCerereToFile(String path, Cerere cerere, String status,
                                                    AtomicInteger fonduriDisponibile) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.append(String.valueOf(cerere.getCod())).append(",")
                .append(String.valueOf(cerere.getValoare())).append(",")
                .append(status).append(",")
                .append(String.valueOf(fonduriDisponibile)).append("\n");
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static int checkFonduriAlocate(String s) {
        //citeste fiecare linie din fisierul s
        //daca valoarea cererii este mai mare decat fonduriDisponibile
        int valoriCereri = 0;
        try(BufferedReader br = new BufferedReader(new java.io.FileReader(s)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if(values[2].equals("Acceptata"))
                    valoriCereri += Integer.parseInt(values[1]);
            }
            return valoriCereri;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
