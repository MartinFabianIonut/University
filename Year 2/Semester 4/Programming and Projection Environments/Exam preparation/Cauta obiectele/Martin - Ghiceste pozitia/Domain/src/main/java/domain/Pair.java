package domain;

import java.io.Serializable;

public class Pair extends Entity<Integer>implements Serializable {
    private Player player;
    private Integer guess;

    public Pair(Integer id, Player player, Integer guess) {
        super(id);
        this.player = player;
        this.guess = guess;
    }

    public Pair() {
        super(0);
        this.player = null;
        this.guess = 0;
    }

    public Pair(Player player, Integer guess) {
        this.player = player;
        this.guess = guess;
    }

    public Integer getGuess() {
        return guess;
    }

    public void setGuess(Integer guess) {
        this.guess = guess;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
