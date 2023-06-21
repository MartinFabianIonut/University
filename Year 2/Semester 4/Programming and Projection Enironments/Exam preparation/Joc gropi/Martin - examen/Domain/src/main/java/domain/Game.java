package domain;

import javax.persistence.*;
import java.io.Serializable;

@javax.persistence.Entity
@Table(name = "game")
@AttributeOverride(name = "id", column = @Column(name = "game_id"))
public class Game extends Entity<Integer>implements Serializable {
    @ManyToOne
    @JoinColumn(name = "position_id1")
    private Position position1;
    @ManyToOne
    @JoinColumn(name = "position_id2")
    private Position position2;
    @ManyToOne
    @JoinColumn(name = "position_id3")
    private Position position3;
    @ManyToOne
    @JoinColumn(name = "position_id4")
    private Position position4;
    @ManyToOne
    @JoinColumn(name = "position_id5")
    private  Position position5;

    public Game() {
    }

    public Game(Integer id, Position position1, Position position2, Position position3, Position position4, Position position5) {
        super(id);
        this.position1 = position1;
        this.position2 = position2;
        this.position3 = position3;
        this.position4 = position4;
        this.position5 = position5;
    }

public Game(Position position1, Position position2, Position position3, Position position4, Position position5) {
        this.position1 = position1;
        this.position2 = position2;
        this.position3 = position3;
        this.position4 = position4;
        this.position5 = position5;
    }

    public Position getPosition1() {
        return position1;
    }

    public void setPosition1(Position position1) {
        this.position1 = position1;
    }

    public Position getPosition2() {
        return position2;
    }

    public void setPosition2(Position position2) {
        this.position2 = position2;
    }

    public Position getPosition3() {
        return position3;
    }

    public void setPosition3(Position position3) {
        this.position3 = position3;
    }

    public Position getPosition4() {
        return position4;
    }

    public void setPosition4(Position position4) {
        this.position4 = position4;
    }

    public Position getPosition5() {
        return position5;
    }

    public void setPosition5(Position position5) {
        this.position5 = position5;
    }

    @Override
    public String toString() {
        return "Game{" +
                "position1=" + position1 +
                ", position2=" + position2 +
                ", position3=" + position3 +
                ", position4=" + position4 +
                ", position5=" + position5 +
                '}';
    }
}
