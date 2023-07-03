package domain;

import java.io.Serializable;
import java.time.LocalTime;

public class DTORest implements Serializable {
    private Game game;
    private Integer totalSum;

    public DTORest() {
    }

    public DTORest(Game game, Integer totalSum) {
        this.game = game;
        this.totalSum = totalSum;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Integer totalSum) {
        this.totalSum = totalSum;
    }
}
