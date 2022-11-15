package socialnetwork.domain;
/*
public class Friendship <ID, E extends Entity<ID>>{
    int [][] matriceAdiacenta = new int[1000][1000];

    public void addFriendship(E friend1, E friend2) throws Exception {
        int id1, id2;
        id1 = (int) friend1.getId();
        id2 = (int) friend2.getId();
        if(matriceAdiacenta[id1][id2] != 0)
            throw new Exception("Prieten existent! Adica utilizatorul" + friend1 + " il are prieten deja pe "+ friend2);
        else matriceAdiacenta[id1][id2] = matriceAdiacenta[id2][id1] = 1;
    }
}
*/

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private final LocalDateTime date;
    private final Utilizator firstFriend;
    private final Utilizator secondFriend;

    public Friendship(Utilizator firstFriend, Utilizator secondFriend) {
        date = LocalDateTime.now();
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
    }

    public Friendship(LocalDateTime date, Utilizator firstFriend, Utilizator secondFriend) {
        this.date = date;
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
    }

    public Utilizator getFirstFriend() {
        return firstFriend;
    }

    public Long getFirstFriendId() {
        return getFirstFriend().getId();
    }

    public Utilizator getSecondFriend() {
        return secondFriend;
    }

    public Long getSecondFriendId() {
        return getSecondFriend().getId();
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id='" + getId() + '\'' +
                ", date='" + getDate() + '\'' +
                ", firstFriend='" + getFirstFriendId() + '\'' +
                ", secondFriend='" + getSecondFriendId() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship)) return false;
        Friendship that = (Friendship) o;
        return getDate().equals(that.getDate()) &&
                getFirstFriendId() == that.getFirstFriendId() &&
                getSecondFriendId() == that.getSecondFriendId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getFirstFriendId(), getSecondFriendId());
    }
}