package domain;

import org.hibernate.annotations.LazyCollection;

import javax.persistence.*;
import java.io.Serializable;

@javax.persistence.Entity
@Table(name = "game")
@AttributeOverride(name = "id", column = @Column(name = "game_id"))
public class Game extends Entity<Integer>implements Serializable {

    private Integer value1;
    private Integer value2;
    private Integer value3;
    private Integer value4;
    private Integer value5;

    public Game() {
    }

    public Game(Integer id, Integer value1, Integer value2, Integer value3, Integer value4, Integer value5) {
        super.setId(id);
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
    }

    public Game(Integer value1, Integer value2, Integer value3, Integer value4, Integer value5) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
    }

    public Integer getValue1() {
        return value1;
    }

    public void setValue1(Integer value1) {
        this.value1 = value1;
    }

    public Integer getValue2() {
        return value2;
    }

    public void setValue2(Integer value2) {
        this.value2 = value2;
    }

    public Integer getValue3() {
        return value3;
    }

    public void setValue3(Integer value3) {
        this.value3 = value3;
    }

    public Integer getValue4() {
        return value4;
    }

    public void setValue4(Integer value4) {
        this.value4 = value4;
    }

    public Integer getValue5() {
        return value5;
    }

    public void setValue5(Integer value5) {
        this.value5 = value5;
    }

    @Override
    public String toString() {
        return "Game{" +
                "value1=" + value1 +
                ", value2=" + value2 +
                ", value3=" + value3 +
                ", value4=" + value4 +
                ", value5=" + value5 +
                '}';
    }
}
