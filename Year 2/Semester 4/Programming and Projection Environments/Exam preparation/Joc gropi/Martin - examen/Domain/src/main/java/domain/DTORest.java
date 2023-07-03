package domain;

import java.io.Serializable;
import java.util.List;

public class DTORest extends Entity<Integer>implements Serializable {
    private Game game;
    private List<Position> positions;
    private Integer points;
    private Integer seconds;

    public DTORest(Integer id, Game game, List<Position> positions, Integer points, Integer seconds) {
        super(id);
        this.game = game;
        this.positions = positions;
        this.points = points;
        this.seconds = seconds;
    }

    public DTORest(Game game, List<Position> positions, Integer points, Integer seconds) {
        this.game = game;
        this.positions = positions;
        this.points = points;
        this.seconds = seconds;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }
}
