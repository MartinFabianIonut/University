package domain;

import javax.persistence.*;
import java.io.Serializable;

@javax.persistence.Entity
@Table(name = "score")
@AttributeOverride(name = "id", column = @Column(name = "score_id"))
public class Score extends Entity<Integer>implements Serializable {

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "player_who_proposed_id")
    private Player player_who_proposed;

    @ManyToOne
    @JoinColumn(name = "player_who_guessed_id")
    private Player player_who_guessed;
    private Integer round;
    private Integer points;
    private Integer guess;

    public Score() {
    }

    public Score(Integer id, Game game, Player player_who_proposed, Player player_who_guessed, Integer round, Integer points, Integer guess) {
        super(id);
        this.game = game;
        this.player_who_proposed = player_who_proposed;
        this.player_who_guessed = player_who_guessed;
        this.round = round;
        this.points = points;
        this.guess = guess;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer_who_proposed() {
        return player_who_proposed;
    }

    public void setPlayer_who_proposed(Player player_who_proposed) {
        this.player_who_proposed = player_who_proposed;
    }

    public Player getPlayer_who_guessed() {
        return player_who_guessed;
    }

    public void setPlayer_who_guessed(Player player_who_guessed) {
        this.player_who_guessed = player_who_guessed;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getGuess() {
        return guess;
    }

    public void setGuess(Integer guess) {
        this.guess = guess;
    }
}
