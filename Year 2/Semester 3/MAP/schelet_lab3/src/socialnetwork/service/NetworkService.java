package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.exceptions.RepoException;
import socialnetwork.domain.exceptions.ValidationException;
import socialnetwork.domain.graph.UndirectedGraph;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository0;

import java.util.*;

public class NetworkService {
    private Repository0<Long, Utilizator> repoUsers;
    private Repository0<Long, Friendship> repoFriendships;

    private Validator<Utilizator> validatorUtilizator;
    private Validator<Friendship> validatorFriendship;

    public NetworkService(Repository0<Long, Utilizator> repoUsers,
                          Repository0<Long, Friendship> repoFriendships,
                          Validator<Utilizator> validatorUtilizator,
                          Validator<Friendship> validatorFriendship) {
        this.repoUsers = repoUsers;
        this.repoUsers.loadData();
        this.repoFriendships = repoFriendships;
        this.repoFriendships.loadData();
        this.validatorUtilizator = validatorUtilizator;
        this.validatorFriendship = validatorFriendship;
    }

    /**
     *
     * @return all existing users
     */
    public Iterable<Utilizator> getAllUsers() {
        return repoUsers.findAll();
    }

    /**
     *
     * @param lastName is the last name of a new user to add to memory, String type,
     *                 if "", validator will throw exception, which will go in cascade
     * @param firstName is the first name of a new user to add to memory, String type,
     *                 if "", validator will throw exception, which will go in cascade
     */
    public void addUser(String lastName, String firstName){
        Utilizator user = new Utilizator(lastName,firstName);
        Iterable<Utilizator> users = repoUsers.findAll();
        Long maxi = 0L;
        for (Utilizator u:users) {
            if(u.getId() > maxi)
                maxi = u.getId();
        }
        user.setId(maxi+1);
        validatorUtilizator.validate(user);
        repoUsers.save(user);
    }

    /**
     *
     * @param id id type Long is the id of a user we want to delete
     */
    public void deleteUser(Long id){
        Utilizator user = repoUsers.delete(id);
        Collection<Friendship> toBeErased = new ArrayList<>();
        for (Friendship friendship : repoFriendships.findAll())
            if (user.getId() == friendship.getFirstFriendId() || user.getId() == friendship.getSecondFriendId())
                toBeErased.add(friendship);
        for (Friendship friendship : toBeErased)
            repoFriendships.delete(friendship.getId());
        for (Utilizator userSearch : repoUsers.findAll())
            userSearch.getFriends().remove(user);
    }

    /**
     *
     * @param u1 first user, I want to see if he is friend with u2
     * @param u2 second user, I want to see if he is friend with u1
     * @return true, if friends, false, else
     */
    public boolean alreadyFriends(Utilizator u1, Utilizator u2){
        List<Utilizator> l1 = u1.getFriends();
        for(Utilizator f: l1) {
            if (f == u2)
                return true;
        }
        return false;
    }


    /**
     *
     * @param id1 id of first user, I want to add friendship between him and second user, identified by id2
     * @param id2 id of second user, I want to add friendship between him and first user, identified by id1
     * @throws RepoException if they were already friends or if same person
     */
    public void addFriendship(Long id1, Long id2) throws RepoException, ValidationException {
        Utilizator [] users = getTwoUsersById(id1,id2);
        Utilizator u1 = users[0];
        Utilizator u2 = users[1];
        if(u1 != u2){
            if(alreadyFriends(u1,u2))
                throw new RepoException(u1+" is already a friend of "+u2);
            Friendship friendship = new Friendship(u1, u2);
            validatorFriendship.validate(friendship);
            Iterable<Friendship> friendships = repoFriendships.findAll();
            Long maxi = 0L;
            for (Friendship f:friendships) {
                if(f.getId() > maxi)
                    maxi = f.getId();
            }
            friendship.setId(maxi+1);
            repoFriendships.save(friendship);
            u1.getFriends().add(u2);
            u2.getFriends().add(u1);
            repoUsers.update(u1);
            repoUsers.update(u2);
        }
        else {
            throw new RepoException("Same user, no efect");
        }
    }

    /**
     *
     * @param id1 id of first user
     * @param id2 id of second user
     * @return a vector of two users if both of them exists
     */
    private Utilizator[] getTwoUsersById(Long id1, Long id2){
        Utilizator u1 = repoUsers.findOne(id1);
        Utilizator u2 = repoUsers.findOne(id2);
        if(u1 == null && u2 == null)
            throw new RepoException("Non existing users!");
        else if (u1==null) throw new RepoException("First user does not exist!");
        else if (u2==null) throw new RepoException("Second user does not exist!");
        return new Utilizator[]{u1,u2};
    }

    /**
     *
     * @param id1 id of first user, I want to delete friendship between him and second user, identified by id2
     * @param id2 id of second user, I want to delete friendship between him and first user, identified by id1
     *            if not friends at all, exception
     *            if same person, exception
     */
    public void deleteFriendship(Long id1, Long id2){
        Utilizator [] users = getTwoUsersById(id1,id2);
        Utilizator u1 = users[0];
        Utilizator u2 = users[1];
        if(u1!=u2) {
            if (alreadyFriends(u1,u2)) {
                u1.getFriends().remove(u2);
                u2.getFriends().remove(u1);
                repoUsers.update(u1);
                repoUsers.update(u2);
                for(Friendship f: repoFriendships.findAll()){
                    Long idf1 = f.getFirstFriendId();
                    Long idf2 = f.getSecondFriendId();
                    if((idf1 == id1 && idf2 == id2) ||(idf1 == id2 && idf2 == id1)){
                        repoFriendships.delete(f.getId());
                        break;
                    }
                }
            } else throw new RepoException(u1 + " nu a fost prieten cu " + u2);
        }
        else {
            throw new RepoException("Same user, no efect");
        }
    }

    /**
     *
     * @return un UndirectedGraph based on users and their friendships
     */
    private UndirectedGraph createGraph() {
        Map<Long, HashSet<Long>> adjMap = new HashMap<>();
        for (Utilizator user : getAllUsers()) {
            adjMap.putIfAbsent(user.getId(), new HashSet<>());
            for (Long friendId : user.getFriendsIds()) {
                Long userId = user.getId();
                adjMap.putIfAbsent(userId, new HashSet<>());
                adjMap.putIfAbsent(friendId, new HashSet<>());
                adjMap.get(userId).add(friendId);
                adjMap.get(friendId).add(userId);
            }
        }
        return new UndirectedGraph(adjMap);
    }

    /**
     *
     * @return the most sociable community as an Iterable<Utilizator>
     */
    public Iterable<Utilizator> getMostSociableCommunity() {
        UndirectedGraph graph = createGraph();
        Collection<Utilizator> mostSociableNetwork = new ArrayList<>();
        Iterable<Long> connectedComponent = graph.getConnectedComponentWithLongestRoad();
        for (Long vertex : connectedComponent) {
            mostSociableNetwork.add(repoUsers.findOne(vertex));
        }
        return mostSociableNetwork;
    }

    /**
     *
     * @return number of communities
     */
    public int numberOfCommunities(){
        UndirectedGraph graph = createGraph();
        return graph.getConnectedComponentsCount();
    }

}

