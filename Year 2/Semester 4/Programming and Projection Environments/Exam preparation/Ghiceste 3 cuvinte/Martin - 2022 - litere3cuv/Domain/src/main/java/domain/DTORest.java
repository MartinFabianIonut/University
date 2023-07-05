package domain;

import java.io.Serializable;
import java.time.LocalTime;

public class DTORest implements Serializable {
    private Game game;
    private Integer totalPoints;
    private String letters;

    public DTORest() {
    }

    public DTORest(Game game, Integer totalPoints, String letters) {
        this.game = game;
        this.totalPoints = totalPoints;
        this.letters = letters;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }
}
