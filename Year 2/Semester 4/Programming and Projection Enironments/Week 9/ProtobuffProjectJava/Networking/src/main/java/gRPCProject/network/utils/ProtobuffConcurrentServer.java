package gRPCProject.network.utils;

import gRPCProject.networking.protobuffprotocol.ProtoWorker;
import gRPCProject.service.IService;

import java.net.Socket;

public class ProtobuffConcurrentServer extends AbsConcurrentServer{
    private IService chatServer;
    public ProtobuffConcurrentServer(int port, IService chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatProtobuffConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ProtoWorker worker=new ProtoWorker(chatServer,client);
        Thread tw=new Thread(worker);
        return tw;
    }
}
