package domain;

import java.io.Serializable;

public class DTO extends Entity<Integer>implements Serializable {
    private Player player;
    private Integer seconds;
    private Integer points;

    public DTO(Integer id, Player player, Integer seconds, Integer points) {
        super(id);
        this.player = player;
        this.seconds = seconds;
        this.points = points;
    }

    public DTO(Player player, Integer seconds, Integer points) {
        this.player = player;
        this.seconds = seconds;
        this.points = points;
    }

    public DTO() {
        this.player = null;
        this.seconds = null;
        this.points = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
