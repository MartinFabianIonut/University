package app.services.rest;

import java.util.List;

public class Pair {
    private String player;
    private List<Integer> positions;


    public Pair(String player, List<Integer> positions) {
        this.player = player;
        this.positions = positions;
    }

    public Pair() {
        this.player = null;
        this.positions = null;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

}
