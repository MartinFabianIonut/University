import app.rest.persistance.repository.IGameRepository;
import app.rest.persistance.repository.IPositionRepository;
import app.rest.persistance.repository.IScoreRepository;
import app.rest.persistance.repository.IPlayerRepository;
import app.rest.persistance.repository.jdbc.GameORMDBIRepository;
import app.rest.persistance.repository.jdbc.PositionORMDBIRepository;
import app.rest.persistance.repository.jdbc.ScoreDBIRepository;
import app.rest.persistance.repository.jdbc.PlayerDBIRepository;
import domain.*;
import martin.network.utils.AbstractServer;
import martin.network.utils.RpcConcurrentServer;
import martin.network.utils.ServerException;
import martin.server.ServicesImpl;
import martin.service.IService;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {

    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        IPlayerRepository<Integer, Player> playerRepository = new PlayerDBIRepository(serverProps);
        IGameRepository<Integer, Game> gameRepository = new GameORMDBIRepository();
        IPositionRepository<Integer, Position> positionRepository = new PositionORMDBIRepository();
        IScoreRepository<Integer, Score> scoreRepository = new ScoreDBIRepository(serverProps,playerRepository, gameRepository, positionRepository);
        IService serverImpl=new ServicesImpl(playerRepository, gameRepository, scoreRepository, positionRepository);

        int defaultPort = 55555;
        int serverPort= defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+ defaultPort);
        }
        System.out.println("Starting server on port: "+serverPort);
        AbstractServer server = new RpcConcurrentServer(serverPort, serverImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
