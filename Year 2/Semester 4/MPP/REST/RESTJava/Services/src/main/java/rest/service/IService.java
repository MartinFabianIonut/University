package rest.service;


import rest.domain.Artist;
import rest.domain.Employee;
import rest.domain.ShowDTO;
import rest.domain.Ticket;

public interface IService {
     void addTickets(Ticket ticket) throws MyException;
     Employee authenticateEmployee(Employee employee, IObserver employeeObserver) throws MyException;
     void logout(Employee employee, IObserver employeeObserver) throws MyException;
     Iterable<ShowDTO> getAllShowsDTO() throws MyException;
     Iterable<Artist> getAllArtists() throws MyException;
     Artist findArtist(Integer id) throws MyException;
}
