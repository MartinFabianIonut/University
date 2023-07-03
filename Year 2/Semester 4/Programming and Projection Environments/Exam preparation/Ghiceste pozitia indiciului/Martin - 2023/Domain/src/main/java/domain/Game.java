package domain;

import org.hibernate.annotations.LazyCollection;

import javax.persistence.*;
import java.io.Serializable;

@javax.persistence.Entity
@Table(name = "game")
@AttributeOverride(name = "id", column = @Column(name = "game_id"))
public class Game extends Entity<Integer>implements Serializable {

    @Column(name = "row")
    private Integer row;
    @Column(name = "col")
    private Integer col;
    @Column(name = "clue")
    private String clue;

    public Game(Integer integer, Integer row, Integer col, String clue) {
        super(integer);
        this.row = row;
        this.col = col;
        this.clue = clue;
    }

    public Game(Integer row, Integer col, String clue) {
        this.row = row;
        this.col = col;
        this.clue = clue;
    }

    public Game() {
        this.row = null;
        this.col = null;
        this.clue = null;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }
}
