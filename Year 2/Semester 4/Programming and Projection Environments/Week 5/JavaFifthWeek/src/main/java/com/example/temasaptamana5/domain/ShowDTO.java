package com.example.temasaptamana5.domain;

import java.time.LocalDate;

public class ShowDTO extends Entity<Integer>{
    private final String title;
    private final LocalDate date;
    private final String place;
    private final Integer available;
    private final Integer unavailable;
    private final Integer idArtist;

    public ShowDTO(Integer id, String title, LocalDate date, String place,
                   Integer available, Integer unavailable, Integer idArtist) {
        super(id);
        this.title = title;
        this.date = date;
        this.place = place;
        this.available = available;
        this.unavailable = unavailable;
        this.idArtist = idArtist;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public Integer getAvailable() {
        return available;
    }

    public Integer getUnavailable() {
        return unavailable;
    }

    public Integer getIdArtist() {
        return idArtist;
    }
}
