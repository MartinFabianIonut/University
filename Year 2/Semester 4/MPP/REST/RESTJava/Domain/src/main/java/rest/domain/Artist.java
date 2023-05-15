package rest.domain;

import java.io.Serializable;

public class Artist extends Entity<Integer> implements Serializable {
    private String firstName;
    private String lastName;

    public Artist() {
        super(0);
        firstName = "";
        lastName = "";
    }

    public Artist(Integer id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
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

    @Override
    public String toString() {
        return getId() + " " + firstName + " " + lastName;
    }
}
