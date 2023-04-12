package com.example.temasaptamana5.domain;

import java.time.LocalDate;

public class Show extends Entity<Integer>{
    private String title;
    private LocalDate date;
    private String place;
    private Integer idArtist;

    public Show(Integer id, String title, LocalDate date, String place, Integer idArtist) {
        super(id);
        this.title = title;
        this.date = date;
        this.place = place;
        this.idArtist = idArtist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getIdArtist() {
        return idArtist;
    }

    public void setIdArtist(Integer idArtist) {
        this.idArtist = idArtist;
    }
}
