package gRPCProject.network.rpcprotocol;

import gRPCProject.domain.*;
import gRPCProject.service.IObserver;
import gRPCProject.service.IService;
import gRPCProject.service.MyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;


public class ClientRpcReflectionWorker implements Runnable, IObserver {
    private final IService service;
    private final Socket connection;

    private static final Logger logger= LogManager.getLogger();

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ClientRpcReflectionWorker(IService service, Socket connection) {
        logger.info("Creating worker");
        this.service = service;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
            logger.info("Worker created");
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Error in worker (reading): "+e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Error in worker (sleeping): "+e);
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error in worker (closing connection): "+e);
        }
    }

    private static final Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    private Response handleRequest(Request request){
        Response response=null;
        String handlerName="handle"+(request).type();
        logger.traceEntry("method entered: "+handlerName+" with parameters "+request);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            logger.info("Method invoked: "+handlerName+" with response "+response);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            logger.error("Error in worker (invoking method handleRequest): "+e);
        }
        return response;
    }

    private Response handleLOGIN(Request request){
        logger.traceEntry("method entered: handleLOGIN with parameters "+request);
        Employee employee = (Employee) request.data();
        try {
            Employee foundEmployee = service.authenticateEmployee(employee, this);
            logger.info("Employee logged in");
            return new Response.Builder().type(ResponseType.OK).data(foundEmployee).build();
        } catch (MyException e) {
            connected=false;
            logger.error("Error in worker (solving method handleLOGIN): "+e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleBUY_TICKET(Request request){
        logger.traceEntry("method entered: handleBUY_TICKET with parameters "+request);
        Ticket ticket=(Ticket) request.data();
        try {
            service.addTickets(ticket);
            logger.info("Ticket bought");
            return new Response.Builder().type(ResponseType.OK).data(ticket).build();
        } catch (MyException e) {
            logger.error("Error in worker (solving method handleBUY_TICKET): "+e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleLOGOUT(Request request){
        logger.traceEntry("method entered: handleLOGOUT with parameters "+request);
        Employee employee=(Employee) request.data();
        try {
            service.logout(employee, this);
            connected=false;
            logger.info("User logged out");
            return okResponse;
        } catch (MyException e) {
            logger.error("Error in worker (solving method handleLOGOUT): "+e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_ALL_SHOWS(Request request){
        logger.traceEntry("method entered: handleGET_ALL_SHOWS with parameters "+request);
        try {
            List<ShowDTO> showsDTO= (List<ShowDTO>) service.getAllShowsDTO();
            logger.info("Shows found "+showsDTO);
            return new Response.Builder().type(ResponseType.GET_ALL_SHOWS).data(showsDTO).build();
        } catch (MyException e) {
            logger.error("Error in worker (solving method handleGET_ALL_SHOWS): "+e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_ALL_ARTISTS(Request request){
        logger.traceEntry("method entered: handleGET_ALL_ARTISTS with parameters "+request);
        try {
            List<Artist> artists= (List<Artist>) service.getAllArtists();
            logger.info("Artists found "+artists);
            return new Response.Builder().type(ResponseType.GET_ALL_ARTISTS).data(artists).build();
        } catch (MyException e) {
            logger.error("Error in worker (solving method handleGET_ALL_ARTISTS): "+e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleFIND_ARTIST(Request request){
        logger.traceEntry("method entered: handleFIND_ARTIST with parameters "+request);
        Integer idArtist=(Integer) request.data();
        try {
            Artist artist=service.findArtist(idArtist);
            logger.info("Artist found "+artist);
            return new Response.Builder().type(ResponseType.FIND_ARTIST).data(artist).build();
        } catch (MyException e) {
            logger.error("Error in worker (solving method handleFIND_ARTIST): "+e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private void sendResponse(Response response) throws IOException{
        logger.traceEntry("method entered: sendResponse with parameters "+response);
        output.writeObject(response);
        output.flush();
        logger.info("Response sent");
    }

    @Override
    public void ticketAdded(Ticket ticket) throws MyException {
        Response resp=new Response.Builder().type(ResponseType.TICKET_SOLD).data(ticket).build();
        logger.info("Ticket sold "+ticket);
        try {
            sendResponse(resp);
            logger.info("Response sent");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error in worker (sending response): "+e);
        }
    }
}
