package org.example.domain;

public class Mesaj {
    private String piesa;
    private String compozitor;
    private String departament;

    public Mesaj(String piesa, String compozitor, String departament) {
        this.piesa = piesa;
        this.compozitor = compozitor;
        this.departament = departament;
    }

    public String getPiesa() {
        return piesa;
    }

    public void setPiesa(String piesa) {
        this.piesa = piesa;
    }

    public String getCompozitor() {
        return compozitor;
    }

    public void setCompozitor(String compozitor) {
        this.compozitor = compozitor;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    @Override
    public String toString() {
        return "Mesaj{" +
                "piesa='" + piesa + '\'' +
                ", compozitor='" + compozitor + '\'' +
                ", departament='" + departament + '\'' +
                '}';
    }
}
