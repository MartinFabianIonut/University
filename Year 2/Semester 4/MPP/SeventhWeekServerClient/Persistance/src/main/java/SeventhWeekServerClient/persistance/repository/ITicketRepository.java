package SeventhWeekServerClient.persistance.repository;

import SeventhWeekServerClient.domain.Entity;
import SeventhWeekServerClient.domain.Ticket;

public interface ITicketRepository <ID, E extends Entity<ID>> extends IRepository<ID, E> {
    void addTicket(Ticket ticket);
}
