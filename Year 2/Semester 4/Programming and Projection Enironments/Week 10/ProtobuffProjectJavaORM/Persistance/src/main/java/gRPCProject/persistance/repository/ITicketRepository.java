package gRPCProject.persistance.repository;

import gRPCProject.domain.Entity;
import gRPCProject.domain.Ticket;

import java.io.Serializable;

public interface ITicketRepository <ID extends Serializable, E extends Entity<ID>> extends IRepository<ID, E> {
    void addTicket(Ticket ticket);
}
