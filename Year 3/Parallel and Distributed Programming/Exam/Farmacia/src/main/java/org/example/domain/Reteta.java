package org.example.domain;

import java.util.List;

public class Reteta {
    private long codReteta;
    private int nrMedicamente;
    private List<Long> listaMedicamente;

    public Reteta(long codReteta, int nrMedicamente, List<Long> listaMedicamente) {
        this.codReteta = codReteta;
        this.nrMedicamente = nrMedicamente;
        this.listaMedicamente = listaMedicamente;
    }

    @Override
    public String toString() {
        return "Reteta{" +
                "codReteta=" + codReteta +
                ", nrMedicamente=" + nrMedicamente +
                ", listaMedicamente=" + listaMedicamente +
                '}';
    }

    public long getCodReteta() {
        return codReteta;
    }

    public void setCodReteta(long codReteta) {
        this.codReteta = codReteta;
    }

    public int getNrMedicamente() {
        return nrMedicamente;
    }

    public void setNrMedicamente(int nrMedicamente) {
        this.nrMedicamente = nrMedicamente;
    }

    public List<Long> getListaMedicamente() {
        return listaMedicamente;
    }

    public void setListaMedicamente(List<Long> listaMedicamente) {
        this.listaMedicamente = listaMedicamente;
    }
}
