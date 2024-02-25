package org.example.domain;

public class Cursant {
    private Integer id;
    private Float medie;

    public Cursant(Integer id, Float medie) {
        this.id = id;
        this.medie = medie;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getMedie() {
        return medie;
    }

    public void setMedie(Float medie) {
        this.medie = medie;
    }
}
