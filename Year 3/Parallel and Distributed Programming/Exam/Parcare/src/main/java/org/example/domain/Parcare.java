package org.example.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Parcare {
    private Integer n;
    private Integer p;
    private List<Tranzactie> tranzactii;


    public Parcare(Integer n, Integer p) {
        this.n = n;
        this.p = p;
        this.tranzactii = new ArrayList<>();

    }

    public void Intrare(){
        p++;
        tranzactii.add(new Tranzactie("Intrare", p));
        System.out.println("Intrare");
    }
    public void Iesire(){
        p--;
        tranzactii.add(new Tranzactie("Iesire", p));
        System.out.println("Iesire");

    }
    public List<Tranzactie> getTranzactii() {
        return tranzactii;
    }
    public Integer getN() {
        return n;
    }
    public Integer getP() {
        return p;
    }
}