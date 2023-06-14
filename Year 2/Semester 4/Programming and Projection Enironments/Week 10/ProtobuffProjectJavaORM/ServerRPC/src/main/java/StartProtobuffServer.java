import gRPCProject.domain.Artist;
import gRPCProject.domain.Employee;
import gRPCProject.domain.Show;
import gRPCProject.domain.Ticket;
import gRPCProject.network.utils.AbstractServer;
import gRPCProject.network.utils.ProtobuffConcurrentServer;
import gRPCProject.network.utils.RpcConcurrentServer;
import gRPCProject.network.utils.ServerException;
import gRPCProject.persistance.repository.IArtistRepository;
import gRPCProject.persistance.repository.IEmployeeRepository;
import gRPCProject.persistance.repository.IShowRepository;
import gRPCProject.persistance.repository.ITicketRepository;
import gRPCProject.persistance.repository.jdbc.*;
import gRPCProject.server.ServicesImpl;
import gRPCProject.service.IService;

import java.io.IOException;
import java.util.Properties;

public class StartProtobuffServer {
    private static int defaultPort=55555;

    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartProtobuffServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        IArtistRepository<Integer, Artist> artistRepository = new ArtistDBIRepository(serverProps);
        IEmployeeRepository<Integer, Employee> employeeRepository = new EmployeeORMDBIRepository();
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
        AbstractServer server = new ProtobuffConcurrentServer(serverPort, serverImpl);
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
