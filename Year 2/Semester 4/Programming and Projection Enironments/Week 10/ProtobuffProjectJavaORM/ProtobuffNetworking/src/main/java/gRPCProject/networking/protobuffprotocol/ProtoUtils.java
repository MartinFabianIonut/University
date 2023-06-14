package gRPCProject.networking.protobuffprotocol;

import gRPCProject.domain.Artist;
import gRPCProject.domain.Employee;
import gRPCProject.domain.ShowDTO;
import gRPCProject.domain.Ticket;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {
    public static Protobuffs.Request createLoginRequest(Employee employee){
        Protobuffs.Employee myEmployee = Protobuffs.Employee.newBuilder().
                setId(employee.getId()).
                setFirstName(employee.getFirstName()).
                setLastName(employee.getLastName()).
                setUsername(employee.getUsername()).
                setPassword(employee.getPassword()).build();
        return Protobuffs.Request.newBuilder().setType(Protobuffs.Request.Type.Login)
                .setEmployee(myEmployee).build();
    }

    public static Protobuffs.Request createLogoutRequest(Employee employee) {
        Protobuffs.Employee myEmployee = Protobuffs.Employee.newBuilder().
                setId(employee.getId()).
                setFirstName(employee.getFirstName()).
                setLastName(employee.getLastName()).
                setUsername(employee.getUsername()).
                setPassword(employee.getPassword()).build();
        return Protobuffs.Request.newBuilder().setType(Protobuffs.Request.Type.Logout)
                .setEmployee(myEmployee).build();
    }

    public static Protobuffs.Request createAddTicketRequest(Ticket ticket) {
        Protobuffs.Ticket myTicket = Protobuffs.Ticket.newBuilder().
                setId(ticket.getId()).
                setIdShow(ticket.getIdShow()).
                setNameOfCostumer(ticket.getNameOfCostumer()).
                setNoOfSeats(ticket.getNoOfSeats()).build();
        return Protobuffs.Request.newBuilder().setType(Protobuffs.Request.Type.BUY_TICKET)
                .setTicket(myTicket).build();
    }

    public static Protobuffs.Request createGetAllShowsDTORequest() {
        return Protobuffs.Request.newBuilder().setType(Protobuffs.Request.Type.GET_ALL_SHOWS).build();
    }

    public static Protobuffs.Request createGetAllArtistsRequest() {
        return Protobuffs.Request.newBuilder().setType(Protobuffs.Request.Type.GET_ALL_ARTISTS).build();
    }

    public static Protobuffs.Request createFindArtistRequest(Integer id) {
        return Protobuffs.Request.newBuilder().setType(Protobuffs.Request.Type.FIND_ARTIST).setIdArtist(id).build();
    }

    public static String getError(Protobuffs.Response response) {
        return response.getError();
    }

    public static Employee getEmployee(Protobuffs.Response response) {
        return new Employee(response.getEmployee().getId(),response.getEmployee().getFirstName(), response.getEmployee().getLastName(), response.getEmployee().getUsername(), response.getEmployee().getPassword());
    }

    public static Employee getEmployee(Protobuffs.Request request){
        return new Employee(request.getEmployee().getId(), request.getEmployee().getFirstName(), request.getEmployee().getLastName(), request.getEmployee().getUsername(), request.getEmployee().getPassword());
    }

    public static Ticket getTicket(Protobuffs.Response response) {
        return new Ticket(response.getTicket().getId(), response.getTicket().getIdShow(), response.getTicket().getNameOfCostumer(), response.getTicket().getNoOfSeats());
    }

    public static Ticket getTicket(Protobuffs.Request request) {
        return new Ticket(request.getTicket().getId(), request.getTicket().getIdShow(), request.getTicket().getNameOfCostumer(), request.getTicket().getNoOfSeats());
    }

    public static Iterable<ShowDTO> getShows(Protobuffs.Response response) {
        List<ShowDTO> showDTOList = new ArrayList<>();
        response.getShowDTOList().forEach(showDTO -> showDTOList.add(new ShowDTO(showDTO.getId(),
                showDTO.getTitle(), LocalDate.parse(showDTO.getDate()), showDTO.getPlace(),
                showDTO.getAvailable(), showDTO.getUnavailable(), showDTO.getIdArtist())));
        return showDTOList;
    }

    public static Iterable<Artist> getArtists(Protobuffs.Response response) {
        List<Artist> artistList = new ArrayList<>();
        response.getArtistList().forEach(artist -> artistList.add(new Artist(artist.getId(), artist.getFirstName(), artist.getLastName())));
        return artistList;
    }

    public static Artist getArtist(Protobuffs.Response response) {
        return new Artist(response.getArtist(0).getId(), response.getArtist(0).getFirstName(), response.getArtist(0).getLastName());
    }

    public static Protobuffs.Response createErrorResponse(String message) {
        return Protobuffs.Response.newBuilder().setType(Protobuffs.Response.Type.Error).setError(message).build();
    }

    public static Protobuffs.Response createOkResponse() {
        return Protobuffs.Response.newBuilder().setType(Protobuffs.Response.Type.Ok).build();
    }

    public static Protobuffs.Response createTicketAddedResponse(Ticket ticket) {
        Protobuffs.Ticket myTicket = Protobuffs.Ticket.newBuilder().
                setId(ticket.getId()).
                setIdShow(ticket.getIdShow()).
                setNameOfCostumer(ticket.getNameOfCostumer()).
                setNoOfSeats(ticket.getNoOfSeats()).build();
        return Protobuffs.Response.newBuilder().setType(Protobuffs.Response.Type.TICKET_SOLD)
                .setTicket(myTicket).build();
    }

    public static Protobuffs.Response createGetAllShowsResponse(Iterable<ShowDTO> allShowsDTO) {
        List<Protobuffs.ShowDTO> showDTOList = new ArrayList<>();
        allShowsDTO.forEach(showDTO -> showDTOList.add(Protobuffs.ShowDTO.newBuilder().
                setId(showDTO.getId()).
                setTitle(showDTO.getTitle()).
                setDate(showDTO.getDate().toString()).
                setPlace(showDTO.getPlace()).
                setAvailable(showDTO.getAvailable()).
                setUnavailable(showDTO.getUnavailable()).
                setIdArtist(showDTO.getIdArtist()).build()));
        return Protobuffs.Response.newBuilder().setType(Protobuffs.Response.Type.GET_ALL_SHOWS).addAllShowDTO(showDTOList).build();
    }

    public static Protobuffs.Response createGetAllArtistsResponse(Iterable<Artist> allArtists) {
        List<Protobuffs.Artist> artistList = new ArrayList<>();
        allArtists.forEach(artist -> artistList.add(Protobuffs.Artist.newBuilder().
                setId(artist.getId()).
                setFirstName(artist.getFirstName()).
                setLastName(artist.getLastName()).build()));
        return Protobuffs.Response.newBuilder().setType(Protobuffs.Response.Type.GET_ALL_ARTISTS).addAllArtist(artistList).build();
    }

    public static int getIdArtist(Protobuffs.Request request) {
        return request.getIdArtist();
    }

    public static Protobuffs.Response createFindArtistResponse(Artist artist) {
        Protobuffs.Artist myArtist = Protobuffs.Artist.newBuilder().
                setId(artist.getId()).
                setFirstName(artist.getFirstName()).
                setLastName(artist.getLastName()).build();
        return Protobuffs.Response.newBuilder().setType(Protobuffs.Response.Type.FIND_ARTIST).addArtist(myArtist).build();
    }

    public static Protobuffs.Response createLoginResponse(Employee authenticateEmployee) {
        Protobuffs.Employee myEmployee = Protobuffs.Employee.newBuilder().
                setId(authenticateEmployee.getId()).
                setFirstName(authenticateEmployee.getFirstName()).
                setLastName(authenticateEmployee.getLastName()).
                setUsername(authenticateEmployee.getUsername()).
                setPassword(authenticateEmployee.getPassword()).build();
        return Protobuffs.Response.newBuilder().setType(Protobuffs.Response.Type.Ok)
                .setEmployee(myEmployee).build();
    }
}
