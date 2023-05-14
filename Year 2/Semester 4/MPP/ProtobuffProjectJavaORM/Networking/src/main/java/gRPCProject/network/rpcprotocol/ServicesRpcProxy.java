package gRPCProject.network.rpcprotocol;

import gRPCProject.domain.Artist;
import gRPCProject.domain.Employee;
import gRPCProject.domain.ShowDTO;
import gRPCProject.domain.Ticket;
import gRPCProject.service.IService;
import gRPCProject.service.IObserver;
import gRPCProject.service.MyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServicesRpcProxy implements IService {
    private final String host;
    private final int port;

    private IObserver employeeObserver;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private static final Logger logger = LogManager.getLogger();

    private final BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesRpcProxy(String host, int port) {
        logger.info("Creating proxy");
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    public void logout(Employee employee, IObserver client) throws MyException {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(employee).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            logger.error("Error logging out" + response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        } else {
            logger.info("Logged out");
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
        } catch (IOException e) {
            logger.error("Error closing connection: " + e);
        }
    }

    private void sendRequest(Request request) throws MyException {
        try {
            output.writeObject(request);
            output.flush();
            logger.info("Request sent: " + request);
        } catch (IOException e) {
            logger.error("Error sending object " + e);
            throw new MyException("Error sending object " + e);
        }
    }

    private Response readResponse() throws MyException {
        Response response = null;
        try {
            response = qresponses.take();
            logger.info("Response received: " + response);
        } catch (InterruptedException e) {
            logger.error("Reading response error " + e);
            throw new MyException("Reading response error " + e);
        }
        return response;
    }

    private void initializeConnection() throws MyException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
            logger.info("Connection initialized");
        } catch (IOException e) {
            logger.error("Error connecting to server " + e);
            throw new MyException("Error connecting to server " + e);
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.TICKET_SOLD) {
            Ticket ticket = (Ticket) response.data();
            logger.info("Ticket sold " + ticket);
            try {
                employeeObserver.ticketAdded(ticket);
            } catch (MyException e) {
                logger.error("Error handle update: " + e);
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.TICKET_SOLD;
    }

    @Override
    public void addTickets(Ticket ticket) throws MyException {
        Request req = new Request.Builder().type(RequestType.BUY_TICKET).data(ticket).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            logger.error("Error adding ticket" + response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        } else {
            logger.info("Ticket added");
        }
    }

    @Override
    public Employee authenticateEmployee(Employee employee, IObserver employeeObserver) throws MyException {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(employee).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.employeeObserver = employeeObserver;
            logger.info("Logged in");
            return (Employee) response.data();
        }
        if (response.type() == ResponseType.ERROR) {
            logger.error("Error logging in" + response.data().toString());
            String err = response.data().toString();
            closeConnection();
            throw new MyException(err);
        }
        return null;
    }

    @Override
    public Iterable<ShowDTO> getAllShowsDTO() throws MyException {
        Request req = new Request.Builder().type(RequestType.GET_ALL_SHOWS).data(null).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            logger.error("Error getting shows" + response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        } else {
            logger.info("Got shows");
        }
        return (List<ShowDTO>) response.data();
    }

    @Override
    public Iterable<Artist> getAllArtists() throws MyException {
        Request req = new Request.Builder().type(RequestType.GET_ALL_ARTISTS).data(null).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            logger.error("Error getting artists" + response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        } else {
            logger.info("Got artists");
        }
        return (List<Artist>) response.data();
    }

    @Override
    public Artist findArtist(Integer id) throws MyException {
        Request req = new Request.Builder().type(RequestType.FIND_ARTIST).data(id).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            logger.error("Error finding artist" + response.data().toString());
            String err = response.data().toString();
            throw new MyException(err);
        } else {
            logger.info("Found artist");
        }
        return (Artist) response.data();
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    logger.info("response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            logger.error("Queue putting response error: " + e);
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    if (e instanceof SocketException)
                        logger.info("Socket closed: " + e);
                    else
                        logger.error("Reading error: " + e);
                }
            }
        }
    }
}
