package org.example.util;

import org.example.domain.Cursant;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
public class GenerateClass {
    public static List<Cursant> generateRandomCursanti(int n){
        List<Cursant> cursanti = new LinkedList<>();
        for (int i = 0; i < n; ++i) {
            cursanti.add(new Cursant(i, new Random().nextFloat() * 10));
        }
        return cursanti;
    }
}
