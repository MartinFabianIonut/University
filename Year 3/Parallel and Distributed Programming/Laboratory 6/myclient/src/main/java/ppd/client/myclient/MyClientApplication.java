package ppd.client.myclient;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import ppd.client.myclient.entity.Participant;
import ppd.client.myclient.util.Client;
import ppd.client.myclient.util.IOHandler;

import java.util.List;

@SpringBootApplication
public class MyClientApplication {

    public static void main(String[] args) {
        List<String> files = IOHandler.generateFileNames();
        List<Participant> participants = IOHandler.readParticipants(files);
        Client client = new Client("http://localhost:8080/contest", participants);
        client.sendBatchSize(participants.size());
        client.startSending();
    }

}
