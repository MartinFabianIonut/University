package domain;

import java.io.Serializable;
import java.time.LocalTime;

public class DTO extends Entity<Integer>implements Serializable {
    private Player player;
    private LocalTime time;
    private Integer guessings;
    private String clue;

    public DTO(Integer id, Player player, LocalTime time, Integer guessings, String clue) {
        super(id);
        this.player = player;
        this.time = time;
        this.guessings = guessings;
        this.clue = clue;
    }

    public DTO(Player player, LocalTime time, Integer guessings, String clue) {
        this.player = player;
        this.time = time;
        this.guessings = guessings;
        this.clue = clue;
    }

    public DTO() {
        this.player = null;
        this.time = null;
        this.guessings = null;
        this.clue = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Integer getGuessings() {
        return guessings;
    }

    public void setGuessings(Integer guessings) {
        this.guessings = guessings;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }
}
