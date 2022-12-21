package socialnetwork.domain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Network {

    private ArrayList<Utilizator> networkUsers ;

    private Map<Utilizator, ArrayList<Utilizator>> networkFriendships = new HashMap<Utilizator, ArrayList<Utilizator>>();

    private static final Network INSTANCE = new Network();
    private Network() {
    }

    public static Network getInstance() {
        return INSTANCE;
    }


}