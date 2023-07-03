import rest.domain.Artist;
import rest.domain.Employee;
import rest.domain.Show;
import rest.domain.Ticket;
import rest.network.utils.AbstractServer;
import rest.network.utils.RpcConcurrentServer;
import rest.network.utils.ServerException;
import music.rest.persistance.repository.IArtistRepository;
import music.rest.persistance.repository.IEmployeeRepository;
import music.rest.persistance.repository.IShowRepository;
import music.rest.persistance.repository.ITicketRepository;
import music.rest.persistance.repository.jdbc.ArtistDBIRepository;
import music.rest.persistance.repository.jdbc.EmployeeDBIRepository;
import music.rest.persistance.repository.jdbc.ShowDBIRepository;
import music.rest.persistance.repository.jdbc.TicketDBIRepository;
import rest.server.ServicesImpl;
import rest.service.IService;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;

    public static void main(String[] args) throws IOException {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        IArtistRepository<Integer, Artist> artistRepository = new ArtistDBIRepository(serverProps);
        IEmployeeRepository<Integer, Employee> employeeRepository = new EmployeeDBIRepository(serverProps);
        IShowRepository<Integer, Show> showRepository = new ShowDBIRepository(serverProps);
        ITicketRepository<Integer, Ticket> ticketRepository = new TicketDBIRepository(serverProps);
        IService serverImpl=new ServicesImpl(artistRepository, employeeRepository, showRepository, ticketRepository);

        int serverPort=defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
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
