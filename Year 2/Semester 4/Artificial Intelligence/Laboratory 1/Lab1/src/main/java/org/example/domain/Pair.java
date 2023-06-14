package org.example.domain;

public class Pair {
    private Integer x,y;
    public Pair(Integer x, Integer y) {
        this.x=x;
        this.y=y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
