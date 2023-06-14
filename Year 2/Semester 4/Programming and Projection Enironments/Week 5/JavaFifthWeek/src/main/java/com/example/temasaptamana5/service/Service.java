package com.example.temasaptamana5.service;

import com.example.temasaptamana5.domain.*;
import com.example.temasaptamana5.repository.*;
import com.example.temasaptamana5.repository.interfaces.IArtistRepository;
import com.example.temasaptamana5.utils.observer.Observer;
import com.example.temasaptamana5.utils.utils.ActualEvent;

public class Service {
    private final ArtistDBIRepository artistDBRepository;
    private final EmployeeDBIRepository employeeDBRepository;
    private final ShowDBIRepository showDBRepository;
    private final TicketDBIRepository ticketDBRepository;

    public Service(IRepository<Integer, Artist> artistDBRepository,
                   IRepository<Integer, Employee> employeeDBRepository,
                   IRepository<Integer, Show> showDBRepository,
                   IRepository<Integer, Ticket> ticketDBRepository) {
        this.artistDBRepository = (ArtistDBIRepository) artistDBRepository;
        this.employeeDBRepository = (EmployeeDBIRepository) employeeDBRepository;
        this.showDBRepository = (ShowDBIRepository) showDBRepository;
        this.ticketDBRepository = (TicketDBIRepository) ticketDBRepository;
    }

    public void addObservers(Observer<ActualEvent> observer){
        artistDBRepository.addObserver(observer);
        employeeDBRepository.addObserver(observer);
        showDBRepository.addObserver(observer);
        ticketDBRepository.addObserver(observer);
    }

    /* ---------------------------------- */

    public Artist findArtist(Integer id){
        return artistDBRepository.findById(id);
    }

    public Employee findEmployee(Integer id){
        return employeeDBRepository.findById(id);
    }

    public Show findShow(Integer id){
        return showDBRepository.findById(id);
    }

    public Ticket findTicket(Integer id){
        return ticketDBRepository.findById(id);
    }

    /* ---------------------------------- */

    public Iterable<Artist> getAllArtists(){
        return artistDBRepository.getAll();
    }

    public Iterable<Employee> getAllEmployees(){
        return employeeDBRepository.getAll();
    }

    public Iterable<Show> getAllShows(){
        return showDBRepository.getAll();
    }

    public Iterable<Ticket> getAllTickets(){
        return ticketDBRepository.getAll();
    }

    /* ---------------------------------- */

    public boolean addArtist(Artist artist){
        return artistDBRepository.add(artist);
    }

    public boolean addEmployee(Employee employee){
        return employeeDBRepository.add(employee);
    }

    public boolean addShow(Show show){
        return showDBRepository.add(show);
    }

    public boolean addTicket(Ticket ticket){
        return ticketDBRepository.add(ticket);
    }
    public void addTickets(Ticket ticket){
        ticketDBRepository.addTicket(ticket);
    }

    /* ---------------------------------- */

    public boolean deleteArtist(Integer id){
        return artistDBRepository.delete(id);
    }

    public boolean deleteEmployee(Integer id){
        return employeeDBRepository.delete(id);
    }

    public boolean deleteShow(Integer id){
        return showDBRepository.delete(id);
    }

    public boolean deleteTicket(Integer id){
        return ticketDBRepository.delete(id);
    }

    /* ---------------------------------- */

    public boolean updateArtist(Artist artist){
        return artistDBRepository.update(artist);
    }

    public boolean updateEmployee(Employee employee){
        return employeeDBRepository.update(employee);
    }

    public boolean updateShow(Show show){
        return showDBRepository.update(show);
    }

    public boolean updateTicket(Ticket ticket){
        return ticketDBRepository.update(ticket);
    }

    /* ---------------------------------- */

    public Employee authenticateEmployee(String username, String password){
        return employeeDBRepository.authenticateEmployee(username,password);
    }

    public Iterable<ShowDTO> getAllShowsDTO(){
        return showDBRepository.getAllShows();
    }
}
