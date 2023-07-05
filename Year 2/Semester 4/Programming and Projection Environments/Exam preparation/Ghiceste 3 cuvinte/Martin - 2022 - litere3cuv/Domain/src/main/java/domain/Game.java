package domain;

import org.hibernate.annotations.LazyCollection;

import javax.persistence.*;
import java.io.Serializable;

@javax.persistence.Entity
@Table(name = "game")
@AttributeOverride(name = "id", column = @Column(name = "game_id"))
public class Game extends Entity<Integer>implements Serializable {

    @Column(name = "letters")
    private String letters;

    @Column(name = "word1")
    private String word1;

    @Column(name = "word2")
    private String word2;

    @Column(name = "word3")
    private String word3;

    public Game() {
    }

    public Game(Integer id, String letters, String word1, String word2, String word3) {
        super(id);
        this.letters = letters;
        this.word1 = word1;
        this.word2 = word2;
        this.word3 = word3;
    }

    public Game(String letters, String word1, String word2, String word3) {
        this.letters = letters;
        this.word1 = word1;
        this.word2 = word2;
        this.word3 = word3;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    public String getWord3() {
        return word3;
    }

    public void setWord3(String word3) {
        this.word3 = word3;
    }
}
