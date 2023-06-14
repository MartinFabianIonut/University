package com.example.temasaptamana5.utils.utils;

import com.example.temasaptamana5.domain.Artist;
import com.example.temasaptamana5.domain.Employee;
import com.example.temasaptamana5.domain.Show;
import com.example.temasaptamana5.domain.Ticket;

public class ActualEvent implements Event{
    private final ChangeEventType type;
    private final Artist artist;
    private final Employee employee;
    private final Show show;
    private final Ticket ticket;

    public ActualEvent(ChangeEventType type, Artist artist, Employee employee, Show show, Ticket ticket) {
        this.type = type;
        this.artist = artist;
        this.employee = employee;
        this.show = show;
        this.ticket = ticket;
    }

    public ChangeEventType getType() {
        return type;
    }
}
