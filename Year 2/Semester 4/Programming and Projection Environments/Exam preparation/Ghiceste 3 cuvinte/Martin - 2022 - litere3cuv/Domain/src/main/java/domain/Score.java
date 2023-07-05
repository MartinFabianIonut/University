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
    private Integer round;
    private Integer positionGuessedWord;
    private String word;
    private Integer points;
    private Integer totalPoints;
    private LocalDateTime time;

    public Score() {
    }

    public Score(Integer id, Game game, Player player, Integer round, Integer positionGuessedWord, String word, Integer points, Integer totalPoints, LocalDateTime time) {
        super(id);
        this.game = game;
        this.player = player;
        this.round = round;
        this.positionGuessedWord = positionGuessedWord;
        this.word = word;
        this.points = points;
        this.totalPoints = totalPoints;
        this.time = time;
    }

    public Score(Game game, Player player, Integer round, Integer positionGuessedWord, String word, Integer points, Integer totalPoints, LocalDateTime time) {
        this.game = game;
        this.player = player;
        this.round = round;
        this.positionGuessedWord = positionGuessedWord;
        this.word = word;
        this.points = points;
        this.totalPoints = totalPoints;
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

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Integer getPositionGuessedWord() {
        return positionGuessedWord;
    }

    public void setPositionGuessedWord(Integer positionGuessedWord) {
        this.positionGuessedWord = positionGuessedWord;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
