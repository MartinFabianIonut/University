package SeventhWeekServerClient.service;


import SeventhWeekServerClient.domain.Employee;
import SeventhWeekServerClient.domain.Ticket;

public interface IObserver {
     void ticketAdded(Ticket ticket) throws MyException;
}
