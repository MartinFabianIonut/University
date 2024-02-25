package org.example.domain;

public class Cerere {

    private int cod;

    private int valoare;

    public Cerere(int valoare) {
        this.valoare = valoare;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getValoare() {
        return valoare;
    }

    public int getCod() {
        return cod;
    }
}
