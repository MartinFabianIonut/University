package domain;

import java.io.Serializable;

public class Pair extends Entity<Integer>implements Serializable {
    private Player player;
    private String letter;

    public Pair(Integer id, Player player, String letter) {
        super(id);
        this.player = player;
        this.letter = letter;
    }

    public Pair() {
        super(0);
        this.player = null;
        this.letter = null;
    }

    public Pair(Player playerWhoProposed, String guess) {
        super(0);
        this.player = playerWhoProposed;
        this.letter = guess;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
