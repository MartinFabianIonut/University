package gRPCProject.networking.protobuffprotocol;

import gRPCProject.domain.Artist;
import gRPCProject.domain.Employee;
import gRPCProject.domain.ShowDTO;
import gRPCProject.domain.Ticket;
import gRPCProject.service.IService;
import gRPCProject.service.IObserver;
import gRPCProject.service.MyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoProxy implements IService {
    private final String host;
    private final int port;
    private IObserver employeeObserver;
    private InputStream input;
    private OutputStream output;
    private Socket connection;
    private final BlockingQueue<Protobuffs.Response> qresponses;
    private volatile boolean finished;
    private static final Logger logger = LogManager.getLogger();

    public ProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
        logger.info("Creating proxy");
    }

    public Employee authenticateEmployee(Employee employee, IObserver client) throws MyException {
        initializeConnection();
        System.out.println("Login request ...");
        logger.info("Login request ...");
        sendRequest(ProtoUtils.createLoginRequest(employee));
        Protobuffs.Response response = readResponse();
        if (response.getType() == Protobuffs.Response.Type.Ok) {
            this.employeeObserver = client;
            return ProtoUtils.getEmployee(response);
        }
        if (response.getType() == Protobuffs.Response.Type.Error) {
            String errorText = ProtoUtils.getError(response);
            closeConnection();
            logger.error("Login error ..." + errorText);
            throw new MyException(errorText);
        }
        return null;
    }

    public void addTickets(Ticket ticket) throws MyException {
        sendRequest(ProtoUtils.createAddTicketRequest(ticket));
        Protobuffs.Response response = readResponse();
        if (response.getType() == Protobuffs.Response.Type.Error) {
            String err = ProtoUtils.getError(response);
            logger.error("Error adding ticket" + err);
            System.out.println("Error adding ticket" + err);
            throw new MyException(err);
        } else {
            logger.info("Ticket added");
            System.out.println("Ticket added");
        }
    }

    public Iterable<ShowDTO> getAllShowsDTO() throws MyException {
        sendRequest(ProtoUtils.createGetAllShowsDTORequest());
        Protobuffs.Response response = readResponse();
        if (response.getType() == Protobuffs.Response.Type.Error) {
            String err = ProtoUtils.getError(response);
            logger.error("Error getting all shows" + err);
            System.out.println("Error getting all shows" + err);
            throw new MyException(err);
        } else {
            logger.info("Got shows");
            System.out.println("Got shows");
        }
        return ProtoUtils.getShows(response);
    }

    public Iterable<Artist> getAllArtists() throws MyException {
        sendRequest(ProtoUtils.createGetAllArtistsRequest());
        Protobuffs.Response response = readResponse();
        if (response.getType() == Protobuffs.Response.Type.Error) {
            String err = ProtoUtils.getError(response);
            logger.error("Error getting artists" + err);
            System.out.println("Error getting artists" + err);
            throw new MyException(err);
        } else {
            logger.info("Got artists");
            System.out.println("Got artists");
        }
        return ProtoUtils.getArtists(response);
    }

    public Artist findArtist(Integer id) throws MyException {
        sendRequest(ProtoUtils.createFindArtistRequest(id));
        Protobuffs.Response response = readResponse();
        if (response.getType() == Protobuffs.Response.Type.Error) {
            String err = ProtoUtils.getError(response);
            logger.error("Error finding artist" + err);
            System.out.println("Error finding artist" + err);
            throw new MyException(err);
        } else {
            logger.info("Found artist");
            System.out.println("Found artist");
        }
        return ProtoUtils.getArtist(response);
    }

    public void logout(Employee employee, IObserver client) throws MyException {
        sendRequest(ProtoUtils.createLogoutRequest(employee));
        Protobuffs.Response response = readResponse();
        closeConnection();
        if (response.getType() == Protobuffs.Response.Type.Error) {
            String err = ProtoUtils.getError(response);
            logger.error("Error logging out" + err);
            System.out.println("Error logging out" + err);
            throw new MyException(err);
        } else {
            logger.info("Logged out");
            System.out.println("Logged out");
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            employeeObserver = null;
            logger.info("Closed connection");
            System.out.println("Closed connection");
        } catch (IOException e) {
            logger.error("Error closing connection" + e);
            System.out.println("Error closing connection" + e);
        }

    }

    private void sendRequest(Protobuffs.Request request) throws MyException {
        try {
            System.out.println("Sending request ..." + request);
            logger.info("Sending request ..." + request);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
            logger.info("Request sent.");
        } catch (IOException e) {
            logger.error("Error sending request " + e);
            throw new MyException("Error sending object " + e);
        }

    }

    private Protobuffs.Response readResponse() throws MyException {
        Protobuffs.Response response = null;
        try {
            response = qresponses.take();
            logger.info("Read response " + response);
        } catch (InterruptedException e) {
            logger.error("Error reading response " + e);
            throw new MyException("Error reading response " + e);
        }
        return response;
    }

    private void initializeConnection() throws MyException {
        try {
            connection = new Socket(host, port);
            output = connection.getOutputStream();
            input = connection.getInputStream();
            finished = false;
            startReader();
            logger.info("Connection initialized");
        } catch (IOException e) {
            logger.error("Error initializing connection " + e);
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private boolean isUpdateResponse(Protobuffs.Response.Type type) {
        return type == Protobuffs.Response.Type.TICKET_SOLD;
    }

    private void handleUpdate(Protobuffs.Response updateResponse) {
        if (updateResponse.getType() == Protobuffs.Response.Type.TICKET_SOLD) {
            Ticket ticket = ProtoUtils.getTicket(updateResponse);
            logger.info("Ticket sold " + ticket);
            System.out.println("Ticket sold " + ticket);
            try {
                employeeObserver.ticketAdded(ticket);
            } catch (MyException e) {
                logger.error("Error handle update: " + e);
                System.out.println("Error handle update: " + e);
            }
        }
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Protobuffs.Response response = Protobuffs.Response.parseDelimitedFrom(input);
                    System.out.println("response received " + response);
                    if (response != null) {
                        if (isUpdateResponse(response.getType())) {
                            handleUpdate(response);
                        } else {
                            try {
                                qresponses.put(response);
                            } catch (InterruptedException e) {
                                logger.error(e);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
