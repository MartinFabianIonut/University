package domain;

import java.io.Serializable;

public class DTO implements Serializable {
    private Player player;
    private String guess;
    private Integer score;

    public DTO( Player player, String guess, Integer score) {
        this.player = player;
        this.guess = guess;
        this.score = score;
    }

    public DTO() {
        this.player = null;
        this.guess = null;
        this.score = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getGuess() {
        return guess;
    }


    public void setGuess(String guess) {
        this.guess = guess;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
