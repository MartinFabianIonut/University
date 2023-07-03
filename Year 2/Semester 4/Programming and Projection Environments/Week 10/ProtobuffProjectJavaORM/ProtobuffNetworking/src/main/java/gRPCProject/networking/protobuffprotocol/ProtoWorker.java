package gRPCProject.networking.protobuffprotocol;

import gRPCProject.domain.Employee;
import gRPCProject.domain.Ticket;
import gRPCProject.service.IObserver;
import gRPCProject.service.IService;
import gRPCProject.service.MyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class ProtoWorker implements Runnable, IObserver {
    private final IService server;
    private final Socket connection;
    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;
    private static final Logger logger = LogManager.getLogger();

    public ProtoWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = connection.getOutputStream();
            input = connection.getInputStream();
            connected = true;
            logger.info("Worker created");
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void run() {
        while (connected) {
            try {
                System.out.println("Waiting requests ...");
                logger.info("Waiting requests ...");
                Protobuffs.Request request = Protobuffs.Request.parseDelimitedFrom(input);
                System.out.println("Request received: " + request);
                logger.info("Request received: " + request);
                Protobuffs.Response response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                logger.error(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
            logger.info("Worker closed");
        } catch (IOException e) {
            logger.error(e);
            System.out.println("Error " + e);
        }
    }

    public void ticketAdded(Ticket ticket) {
        System.out.println("Ticket sold " + ticket);
        logger.info("Ticket sold " + ticket);
        try {
            sendResponse(ProtoUtils.createTicketAddedResponse(ticket));
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private Protobuffs.Response handleRequest(Protobuffs.Request request) {
        Protobuffs.Response response = null;
        switch (request.getType()) {
            case Login -> {
                System.out.println("Login request ...");
                logger.info("Login request ...");
                Employee employee = ProtoUtils.getEmployee(request);
                try {
                    logger.info("Employee authenticated");
                    return ProtoUtils.createLoginResponse(server.authenticateEmployee(employee, this));
                } catch (MyException e) {
                    connected = false;
                    logger.error(e);
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case Logout -> {
                System.out.println("Logout request");
                logger.info("Logout request");
                Employee employee = ProtoUtils.getEmployee(request);
                try {
                    server.logout(employee, this);
                    connected = false;
                    logger.info("Employee logged out");
                    return ProtoUtils.createOkResponse();

                } catch (MyException e) {
                    logger.error(e);
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case BUY_TICKET -> {
                System.out.println("Buy ticket request");
                logger.info("Buy ticket request");
                Ticket ticket = ProtoUtils.getTicket(request);
                try {
                    server.addTickets(ticket);
                    logger.info("Ticket added");
                    return ProtoUtils.createOkResponse();
                } catch (MyException e) {
                    logger.error(e);
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case GET_ALL_SHOWS -> {
                System.out.println("Get all shows request");
                logger.info("Get all shows request");
                try {
                    return ProtoUtils.createGetAllShowsResponse(server.getAllShowsDTO());
                } catch (MyException e) {
                    logger.error(e);
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case GET_ALL_ARTISTS -> {
                System.out.println("Get all artists request");
                logger.info("Get all artists request");
                try {
                    return ProtoUtils.createGetAllArtistsResponse(server.getAllArtists());
                } catch (MyException e) {
                    logger.error(e);
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case FIND_ARTIST -> {
                System.out.println("Find artist request");
                logger.info("Find artist request");
                int id = ProtoUtils.getIdArtist(request);
                try {
                    return ProtoUtils.createFindArtistResponse(server.findArtist(id));
                } catch (MyException e) {
                    logger.error(e);
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
        }
        return response;
    }

    private void sendResponse(Protobuffs.Response response) throws IOException {
        System.out.println("sending response " + response);
        logger.info("sending response " + response);
        response.writeDelimitedTo(output);
        output.flush();
    }
}
