package martin.network.rpcprotocol;

import martin.service.IObserver;
import martin.service.IService;
import martin.service.MyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import domain.*;

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

    private static final Logger logger = LogManager.getLogger();

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcReflectionWorker(IService service, Socket connection) {
        logger.info("Creating worker");
        this.service = service;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
            logger.info("Worker created");
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Error in worker (reading): " + e);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("Error in worker (sleeping): " + e);
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error in worker (closing connection): " + e);
        }
    }

    private static final Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + (request).type();
        logger.traceEntry("method entered: " + handlerName + " with parameters " + request);
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            logger.info("Method invoked: " + handlerName + " with response " + response);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            logger.error("Error in worker (invoking method handleRequest): " + e);
        }
        return response;
    }

    private Response handleLOGIN(Request request) {
        logger.traceEntry("method entered: handleLOGIN with parameters " + request);
        Player game = (Player) request.data();
        try {
            Player foundGame = service.login(game, this);
            logger.info("Game logged in");
            return new Response.Builder().type(ResponseType.OK).data(foundGame).build();
        } catch (MyException e) {
            connected = false;
            logger.error("Error in worker (solving method handleLOGIN): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request) {
        logger.traceEntry("method entered: handleLOGOUT with parameters " + request);
        Player game = (Player) request.data();
        try {
            service.logout(game, this);
            connected = false;
            logger.info("User logged out");
            return okResponse;
        } catch (MyException e) {
            logger.error("Error in worker (solving method handleLOGOUT): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_RANKING(Request request) {
        logger.traceEntry("method entered: handleGET_RANKING with parameters " + request);
        try {
            List<DTO> ranking = (List<DTO>) service.getRanking();
            logger.info("Ranking found");
            return new Response.Builder().type(ResponseType.OK).data(ranking).build();
        } catch (MyException e) {
            logger.error("Error in worker (solving method handleGET_RANKING): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGUESS(Request request) {
        logger.traceEntry("method entered: handleGUESS with parameters " + request);
        Pair player_coordinates = (Pair) request.data();
        try {
            String result = service.guess(player_coordinates);
            logger.info("Guess made");
            return new Response.Builder().type(ResponseType.OK).data(result).build();
        } catch (MyException e) {
            logger.error("Error in worker (solving method handleGUESS): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleSET_CONTROL_GAME_OVER(Request request) {
        logger.traceEntry("method entered: handleSET_CONTROL_GAME_OVER with parameters " + request);
        Player player = (Player) request.data();
        try {
            String control = service.setControlGameOver(player);
            logger.info("Control set");
            return new Response.Builder().type(ResponseType.OK).data(control).build();
        } catch (MyException e) {
            logger.error("Error in worker (solving method handleSET_CONTROL_GAME_OVER): " + e);
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private void sendResponse(Response response) throws IOException {
        logger.traceEntry("method entered: sendResponse with parameters " + response);
        output.writeObject(response);
        output.flush();
        logger.info("Response sent");
    }

    @Override
    public void updateRanking() throws MyException {
        Response resp = new Response.Builder().type(ResponseType.RANKING).data(service.getRanking()).build();
        logger.info("Ranking updated");
        try {
            sendResponse(resp);
            logger.info("Response sent");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error in worker (sending response): " + e);
        }
    }

    @Override
    public void updateControl(Player player) {
        Response resp = new Response.Builder().type(ResponseType.CONTROL).data(player).build();
        logger.info("Control updated");
        try {
            sendResponse(resp);
            logger.info("Response sent");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error in worker (sending response): " + e);
        }
    }
}
