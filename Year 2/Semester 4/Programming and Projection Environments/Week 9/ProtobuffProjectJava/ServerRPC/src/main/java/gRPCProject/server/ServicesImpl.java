package gRPCProject.server;

import gRPCProject.domain.*;
import gRPCProject.persistance.repository.IArtistRepository;
import gRPCProject.persistance.repository.IEmployeeRepository;
import gRPCProject.persistance.repository.IShowRepository;
import gRPCProject.persistance.repository.ITicketRepository;
import gRPCProject.service.IObserver;
import gRPCProject.service.IService;
import gRPCProject.service.MyException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ServicesImpl implements IService {

    private final IArtistRepository<Integer, Artist> artistRepository;
    private final IEmployeeRepository<Integer, Employee> employeeRepository;
    private final IShowRepository<Integer, Show> showRepository;
    private final ITicketRepository<Integer, Ticket> ticketRepository;
    private final Map<Integer, IObserver> loggedEmployees;

    public ServicesImpl(IArtistRepository<Integer, Artist> artistRepository,
                        IEmployeeRepository<Integer, Employee> employeeRepository,
                        IShowRepository<Integer, Show> showRepository,
                        ITicketRepository<Integer, Ticket> ticketRepository) {
        this.artistRepository = artistRepository;
        this.employeeRepository = employeeRepository;
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        loggedEmployees = new ConcurrentHashMap<>();
    }


    @Override
    public synchronized void addTickets(Ticket ticket) throws MyException {
        ticketRepository.addTicket(ticket);
        for (IObserver employeeO: loggedEmployees.values()) {
            employeeO.ticketAdded(ticket);
        }
    }

    @Override
    public synchronized Employee authenticateEmployee(Employee employee, IObserver employeeObserver) throws MyException {
        Employee authenticateEmployee = employeeRepository.authenticateEmployee(employee.getUsername(), employee.getPassword());
        if (authenticateEmployee!=null){
            if(loggedEmployees.get(authenticateEmployee.getId())!=null)
                throw new MyException("User already logged in.");
            loggedEmployees.put(authenticateEmployee.getId(), employeeObserver);
        }else
            throw new MyException("Authentication failed.");
        return authenticateEmployee;
    }

    @Override
    public synchronized void logout(Employee employee, IObserver employeeObserver) throws MyException {
        IObserver localClient= loggedEmployees.remove(employee.getId());
        if (localClient==null)
            throw new MyException("User "+employee.getId()+" is not logged in.");
    }

    @Override
    public synchronized Iterable<ShowDTO> getAllShowsDTO() {
        return showRepository.getAllShowsDTO();
    }

    @Override
    public synchronized Iterable<Artist> getAllArtists() throws MyException {
        return artistRepository.getAll();
    }

    @Override
    public synchronized Artist findArtist(Integer id) throws MyException {
        return artistRepository.findById(id);
    }
}
