package domain;

import javax.persistence.*;
import java.io.Serializable;
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
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position guess;
    private Integer points;
    private Integer totalPoints;
    private Integer round;
    private Integer gameOver;
    private LocalTime time;

    public Score() {
    }

    public Score(Integer id, Game game, Player player, Position guess, Integer points, Integer totalPoints, Integer round, Integer gameOver, LocalTime time) {
        super(id);
        this.game = game;
        this.player = player;
        this.guess = guess;
        this.points = points;
        this.totalPoints = totalPoints;
        this.round = round;
        this.gameOver = gameOver;
        this.time = time;
    }

    public Integer getGameOver() {
        return gameOver;
    }

    public void setGameOver(Integer gameOver) {
        this.gameOver = gameOver;
    }

    public Score(Game game, Player player, Position guess, Integer points, Integer totalPoints, Integer round, Integer gameOver, LocalTime time) {
        this.game = game;
        this.player = player;
        this.guess = guess;
        this.points = points;
        this.totalPoints = totalPoints;
        this.round = round;
        this.gameOver = gameOver;
        this.time = time;
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

    public Position getGuess() {
        return guess;
    }

    public void setGuess(Position guess) {
        this.guess = guess;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
