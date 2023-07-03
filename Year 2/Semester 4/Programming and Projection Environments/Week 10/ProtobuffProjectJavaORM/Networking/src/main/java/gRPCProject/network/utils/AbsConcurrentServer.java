package gRPCProject.network.utils;

import java.net.Socket;


public abstract class AbsConcurrentServer extends AbstractServer {

    public AbsConcurrentServer(int port) {
        super(port);
    }

    protected void processRequest(Socket client) {
        Thread tw=createWorker(client);
        tw.start();
    }

    protected abstract Thread createWorker(Socket client) ;


}
