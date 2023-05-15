package music.rest.persistance.repository;

import rest.domain.Entity;
import rest.domain.Ticket;

import java.io.Serializable;

public interface ITicketRepository <ID extends Serializable, E extends Entity<ID>> extends IRepository<ID, E> {
    void addTicket(Ticket ticket);
}
