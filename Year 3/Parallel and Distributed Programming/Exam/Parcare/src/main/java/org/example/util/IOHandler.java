package org.example.util;

import org.example.domain.Tranzactie;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
public class IOHandler {

    public static void writeToFile(List<Tranzactie> tranzactii) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/Tranzactii.txt"));
            for (Tranzactie tranzactie : tranzactii) {
                bw.write(tranzactie.toString());
            }
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
