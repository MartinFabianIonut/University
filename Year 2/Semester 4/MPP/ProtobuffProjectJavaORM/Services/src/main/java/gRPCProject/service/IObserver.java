package gRPCProject.service;

import gRPCProject.domain.Ticket;

public interface IObserver {
     void ticketAdded(Ticket ticket) throws MyException;
}
