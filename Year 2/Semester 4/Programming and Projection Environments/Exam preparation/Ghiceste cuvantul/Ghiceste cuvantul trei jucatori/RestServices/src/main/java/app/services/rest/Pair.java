package app.services.rest;

public class Pair {
    private String letter;
    private Integer points;

    public Pair(String letter, Integer points) {
        this.letter = letter;
        this.points = points;
    }

    public String getLetter() {
        return letter;
    }

    public Integer getPoints() {
        return points;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
