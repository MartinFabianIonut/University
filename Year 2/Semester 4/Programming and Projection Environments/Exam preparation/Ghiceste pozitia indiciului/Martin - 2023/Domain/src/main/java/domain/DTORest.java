package domain;

import java.io.Serializable;
import java.time.LocalTime;

public class DTORest implements Serializable {
    private Integer attempts;
    private String positions;
    private String clue;

    public DTORest(Integer attempts, String positions, String clue) {
        this.attempts = attempts;
        this.positions = positions;
        this.clue = clue;
    }

    public DTORest() {
        this.attempts = null;
        this.positions = null;
        this.clue = null;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public String getPositions() {
        return positions;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }
}
