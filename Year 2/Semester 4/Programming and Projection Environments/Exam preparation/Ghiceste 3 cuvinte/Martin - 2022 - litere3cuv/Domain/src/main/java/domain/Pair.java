package domain;

import java.io.Serializable;

public class Pair extends Entity<Integer>implements Serializable {
    private Player player;
    private String word;

    public Pair() {
    }

    public Pair(Player player, String word) {
        this.player = player;
        this.word = word;
    }

    public Pair(Integer id, Player player, String word) {
        this.setId(id);
        this.player = player;
        this.word = word;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
