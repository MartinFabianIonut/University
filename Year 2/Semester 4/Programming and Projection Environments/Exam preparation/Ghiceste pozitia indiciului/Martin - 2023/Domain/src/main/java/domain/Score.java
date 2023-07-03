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
    private Integer attempts;
    private String positions;
    private String text;
    private LocalTime time;

    public Score() {
    }

    public Score(Integer id, Game game, Player player, Integer attempts, String positions, String text, LocalTime time) {
        super(id);
        this.game = game;
        this.player = player;
        this.attempts = attempts;
        this.positions = positions;
        this.text = text;
        this.time = time;
    }

    public Score(Game game, Player player, Integer attempts,String positions, String text, LocalTime time) {
        this.game = game;
        this.player = player;
        this.attempts = attempts;
        this.positions = positions;
        this.text = text;
        this.time = time;
    }

    public Score(Game game, Player player, Integer attempts,String positions) {
        this.game = game;
        this.player = player;
        this.attempts = attempts;
        this.positions = positions;
        this.text = null;
        this.time = LocalTime.now();
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

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPositions() {
        return positions;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
