package gRPCProject.service;


import gRPCProject.domain.Artist;
import gRPCProject.domain.Employee;
import gRPCProject.domain.ShowDTO;
import gRPCProject.domain.Ticket;

public interface IService {
     void addTickets(Ticket ticket) throws MyException;
     Employee authenticateEmployee(Employee employee, IObserver employeeObserver) throws MyException;
     void logout(Employee employee, IObserver employeeObserver) throws MyException;
     Iterable<ShowDTO> getAllShowsDTO() throws MyException;
     Iterable<Artist> getAllArtists() throws MyException;
     Artist findArtist(Integer id) throws MyException;
}
