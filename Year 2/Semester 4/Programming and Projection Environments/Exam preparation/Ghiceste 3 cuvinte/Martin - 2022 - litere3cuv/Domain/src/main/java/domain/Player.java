package domain;

import javax.persistence.Table;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import java.io.Serializable;

@javax.persistence.Entity
@Table(name = "player")
@AttributeOverride(name = "id", column = @Column(name = "player_id"))
public class Player extends Entity<Integer> implements Serializable {
    private String alias;

    public Player(Integer id, String alias) {
        super(id);
        this.alias = alias;
    }

    public Player(String alias) {
        this.alias = alias;
    }

    public Player() {
        this.alias = null;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return  alias;
    }

}
