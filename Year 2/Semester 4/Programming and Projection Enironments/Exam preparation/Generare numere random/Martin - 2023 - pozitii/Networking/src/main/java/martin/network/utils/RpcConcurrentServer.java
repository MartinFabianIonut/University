package martin.network.utils;

import martin.network.rpcprotocol.ClientRpcReflectionWorker;
import martin.service.IService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;


public class RpcConcurrentServer extends AbsConcurrentServer {
    private final IService chatServer;
    private static final Logger logger= LogManager.getLogger();
    public RpcConcurrentServer(int port, IService chatServer) {
        super(port);
        this.chatServer = chatServer;
        logger.info("RpcConcurrentServer created");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRpcReflectionWorker worker=new ClientRpcReflectionWorker(chatServer, client);
        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        logger.info("Stopping services ...");
    }
}
