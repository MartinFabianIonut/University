package domain;

import java.io.Serializable;

public class GamePlayer extends Entity<Integer> implements Serializable {

    private Game game;
    private Player player;

    public GamePlayer(Integer id, Game game, Player player) {
        super(id);
        this.game = game;
        this.player = player;
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public GamePlayer() {
        this.game = null;
        this.player = null;
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
}
