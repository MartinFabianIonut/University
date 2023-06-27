package domain;

import java.io.Serializable;

public class DTO extends Entity<Integer>implements Serializable {
    private Player player;
    private String word;
    private Integer score;

    public DTO(Integer id, Player player, String word, Integer score) {
        super(id);
        this.player = player;
        this.word = word;
        this.score = score;
    }

    public DTO() {
        super(0);
        this.player = null;
        this.word = null;
        this.score = 0;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
