package domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DTO extends Entity<Integer>implements Serializable {
    private Player player;
    private LocalDateTime time;
    private Integer totalPoints;

    public DTO() {
    }

    public DTO(Player player, LocalDateTime time, Integer totalPoints) {
        this.player = player;
        this.time = time;
        this.totalPoints = totalPoints;
    }

    public DTO(Integer id, Player player, LocalDateTime time, Integer totalPoints) {
        this.setId(id);
        this.player = player;
        this.time = time;
        this.totalPoints = totalPoints;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }
}
