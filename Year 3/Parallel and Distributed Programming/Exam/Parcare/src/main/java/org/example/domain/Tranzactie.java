package org.example.domain;

public class Tranzactie {
    private String type;
    private Integer availableSpaces;

    public Tranzactie(String type, Integer availableSpaces) {
        this.type = type;
        this.availableSpaces = availableSpaces;
    }
    public String getType() {
        return type;
    }
    public Integer getAvailableSpaces() {
        return availableSpaces;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setAvailableSpaces(Integer availableSpaces) {
        this.availableSpaces = availableSpaces;
    }
    @Override
    public String toString() {
        return type + " " + availableSpaces+"\n";
    }
}
