package org.example.domain;

import java.util.List;

public class Inregistrare {
    private long nr_Factura;
    private int valoare;
    private long codReteta;

    public Inregistrare(long nr_Factura, int valoare, long codReteta) {
        this.nr_Factura = nr_Factura;
        this.valoare = valoare;
        this.codReteta = codReteta;
    }

    @Override
    public String toString() {
        return "Inregistrare{" +
                "nr_Factura=" + nr_Factura +
                ", valoare=" + valoare +
                ", codReteta=" + codReteta +
                '}';
    }

    public long getNr_Factura() {
        return nr_Factura;
    }

    public void setNr_Factura(long nr_Factura) {
        this.nr_Factura = nr_Factura;
    }

    public int getValoare() {
        return valoare;
    }

    public void setValoare(int valoare) {
        this.valoare = valoare;
    }

    public long getCodReteta() {
        return codReteta;
    }

    public void setCodReteta(long codReteta) {
        this.codReteta = codReteta;
    }
}

