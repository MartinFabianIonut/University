package rest.service;

import rest.domain.Ticket;

public interface IObserver {
     void ticketAdded(Ticket ticket) throws MyException;
}
