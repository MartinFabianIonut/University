import SeventhWeekServerClient.domain.Artist;
import SeventhWeekServerClient.domain.Employee;
import SeventhWeekServerClient.domain.Show;
import SeventhWeekServerClient.domain.Ticket;
import SeventhWeekServerClient.network.utils.AbstractServer;
import SeventhWeekServerClient.network.utils.RpcConcurrentServer;
import SeventhWeekServerClient.network.utils.ServerException;
import SeventhWeekServerClient.persistance.repository.IArtistRepository;
import SeventhWeekServerClient.persistance.repository.IEmployeeRepository;
import SeventhWeekServerClient.persistance.repository.IShowRepository;
import SeventhWeekServerClient.persistance.repository.ITicketRepository;
import SeventhWeekServerClient.persistance.repository.jdbc.ArtistDBIRepository;
import SeventhWeekServerClient.persistance.repository.jdbc.EmployeeDBIRepository;
import SeventhWeekServerClient.persistance.repository.jdbc.ShowDBIRepository;
import SeventhWeekServerClient.persistance.repository.jdbc.TicketDBIRepository;
import SeventhWeekServerClient.server.ServicesImpl;
import SeventhWeekServerClient.service.IService;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;

    public static void main(String[] args) {
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
        IService chatServerImpl=new ServicesImpl(artistRepository, employeeRepository, showRepository, ticketRepository);

        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new RpcConcurrentServer(chatServerPort, chatServerImpl);
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
