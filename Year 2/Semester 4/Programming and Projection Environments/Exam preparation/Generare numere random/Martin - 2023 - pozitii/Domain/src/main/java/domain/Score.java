package domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@javax.persistence.Entity
@Table(name = "score")
@AttributeOverride(name = "id", column = @Column(name = "score_id"))
public class Score extends Entity<Integer>implements Serializable {
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    private Integer position;
    private Integer sum;
    private Integer totalSum;

    private Integer gameOver;
    private LocalDateTime time;

    public Score() {
    }

    public Score(Integer id, Game game, Player player, Integer position, Integer sum, Integer totalSum, Integer gameOver, LocalDateTime time) {
        super.setId(id);
        this.game = game;
        this.player = player;
        this.position = position;
        this.sum = sum;
        this.totalSum = totalSum;
        this.gameOver = gameOver;
        this.time = time;
    }

    public Score(Game game, Player player, Integer position, Integer sum, Integer totalSum,Integer gameOver, LocalDateTime time) {
        this.game = game;
        this.player = player;
        this.position = position;
        this.sum = sum;
        this.totalSum = totalSum;
        this.gameOver = gameOver;
        this.time = time;
    }

    public Score(Game game, Player player, Integer position, Integer sum, Integer totalSum,Integer gameOver) {
        this.game = game;
        this.player = player;
        this.position = position;
        this.sum = sum;
        this.totalSum = totalSum;
        this.gameOver = gameOver;
        this.time = LocalDateTime.now();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
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

    public Integer getGameOver() {
        return gameOver;
    }

    public void setGameOver(Integer gameOver) {
        this.gameOver = gameOver;
    }
}
