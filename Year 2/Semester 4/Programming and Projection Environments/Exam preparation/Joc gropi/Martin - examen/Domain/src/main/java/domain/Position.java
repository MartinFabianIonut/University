package domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@javax.persistence.Entity
@Table(name = "position")
@AttributeOverride(name = "id", column = @Column(name = "position_id"))
public class Position extends Entity<Integer>implements Serializable {

    @Column(name = "row")
    private Integer row;
    @Column(name = "col")
    private Integer col;

    public Position(Integer integer, Integer row, Integer col) {
        super(integer);
        this.row = row;
        this.col = col;
    }

    public Position(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }

    public Position() {
        this.row = null;
        this.col = null;
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

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
