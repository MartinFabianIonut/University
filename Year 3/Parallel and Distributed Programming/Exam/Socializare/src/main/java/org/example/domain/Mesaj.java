package org.example.domain;

public class Mesaj {
    private String nume_persoana;
    private String nume_prieten;

    public Mesaj(String nume_persoana, String nume_prieten) {
        this.nume_persoana = nume_persoana;
        this.nume_prieten = nume_prieten;
    }

    public String getNume_persoana() {
        return nume_persoana;
    }

    public String getNume_prieten() {
        return nume_prieten;
    }

    public void setNume_persoana(String nume_persoana) {
        this.nume_persoana = nume_persoana;
    }

    public void setNume_prieten(String nume_prieten) {
        this.nume_prieten = nume_prieten;
    }
}
