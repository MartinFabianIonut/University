package domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DTO extends Entity<Integer>implements Serializable {
    private Player player;
    private LocalDateTime time;
    private Integer totalSum;

    public DTO(Integer id, Player player, LocalDateTime time, Integer totalSum) {
        super(id);
        this.player = player;
        this.time = time;
        this.totalSum = totalSum;
    }

    public DTO() {
        super(0);
        this.player = null;
        this.time = null;
        this.totalSum = 0;
    }

    public DTO(Player player, LocalDateTime time, Integer totalSum) {
        this.player = player;
        this.time = time;
        this.totalSum = totalSum;
    }

    public Integer getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Integer totalSum) {
        this.totalSum = totalSum;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
