package domain;

import java.io.Serializable;

public class Pair extends Entity<Integer>implements Serializable {
    private Player player;
    private int row, col;

    public Pair(Player player, int row, int col) {
        this.player = player;
        this.row = row;
        this.col = col;
    }

    public Pair() {

    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
