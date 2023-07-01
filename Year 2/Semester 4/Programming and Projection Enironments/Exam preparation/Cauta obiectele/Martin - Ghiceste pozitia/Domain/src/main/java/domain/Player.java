package domain;

import javax.persistence.Table;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import java.io.Serializable;

@javax.persistence.Entity
@Table(name = "player")
@AttributeOverride(name = "id", column = @Column(name = "player_id"))
public class Player extends Entity<Integer> implements Serializable {
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    public Player(Integer id, String firstName, String lastName, String username, String password) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public Player() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}
