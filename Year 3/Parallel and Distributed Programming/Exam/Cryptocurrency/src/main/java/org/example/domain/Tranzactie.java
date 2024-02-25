package org.example.domain;

public class Tranzactie {
    private Integer codWalletUtilizator;
    private Integer valoare;
    private Integer codWalletDestinatar;
    private Integer idSupervizor;

    public Tranzactie(Integer codWalletUtilizator, Integer valoare, Integer codWalletDestinatar) {
        this.codWalletUtilizator = codWalletUtilizator;
        this.valoare = valoare;
        this.codWalletDestinatar = codWalletDestinatar;
        this.idSupervizor = -1;
    }

    public Tranzactie(Integer codWalletUtilizator, Integer valoare, Integer codWalletDestinatar, Integer idSupervizor) {
        this.codWalletUtilizator = codWalletUtilizator;
        this.valoare = valoare;
        this.codWalletDestinatar = codWalletDestinatar;
        this.idSupervizor = idSupervizor;
    }
    public Integer getCodWalletUtilizator() {
        return codWalletUtilizator;
    }

    public void setCodWalletUtilizator(Integer codWalletUtilizator) {
        this.codWalletUtilizator = codWalletUtilizator;
    }

    public Integer getValoare() {
        return valoare;
    }

    public void setValoare(Integer valoare) {
        this.valoare = valoare;
    }

    public Integer getCodWalletDestinatar() {
        return codWalletDestinatar;
    }

    public void setCodWalletDestinatar(Integer codWalletDestinatar) {
        this.codWalletDestinatar = codWalletDestinatar;
    }

    public Integer getIdSupervizor() {
        return idSupervizor;
    }

    public void setIdSupervizor(Integer idSupervizor) {
        this.idSupervizor = idSupervizor;
    }

    @Override
    public String toString() {
        return "Tranzactie{" +
                "codWalletUtilizator=" + codWalletUtilizator +
                ", valoare=" + valoare +
                ", codWalletDestinatar=" + codWalletDestinatar +
                ", idSupervizor=" + idSupervizor +
                '}';
    }
}
