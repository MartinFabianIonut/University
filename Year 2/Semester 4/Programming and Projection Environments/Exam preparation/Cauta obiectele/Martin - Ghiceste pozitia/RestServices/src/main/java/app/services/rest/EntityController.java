package app.services.rest;

import app.rest.persistance.repository.IGameRepository;
import app.rest.persistance.repository.IScoreRepository;
import domain.DTO;
import domain.Game;
import domain.Player;
import domain.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import martin.service.MyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/app/games")
public class EntityController {

    @Autowired
    private IScoreRepository<Integer, Score> moveRepository;
    @Autowired
    private IGameRepository<Integer, Game> gameRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Pair[] getAll2(@PathVariable Integer id) {
        System.out.println("Get pozitii pentru jocul ..." + id);
        Pair[] participant_pozitii = new Pair[3];
        Game game = gameRepository.findById(id);
        String word1 = game.getWord1();
        String word2 = game.getWord2();
        String word3 = game.getWord3();
        // make a list of integers of 3 elements with values coresponing to the positions in words where there is 1
        List<Integer> pozitii1 = new ArrayList<>();
        List<Integer> pozitii2 = new ArrayList<>();
        List<Integer> pozitii3 = new ArrayList<>();
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) == '1') {
                pozitii1.add(i + 1);
            }
        }
        for (int i = 0; i < word2.length(); i++) {
            if (word2.charAt(i) == '1') {
                pozitii2.add(i + 1);
            }
        }
        for (int i = 0; i < word3.length(); i++) {
            if (word3.charAt(i) == '1') {
                pozitii3.add(i + 1);
            }
        }
        participant_pozitii[0] = new Pair(game.getPlayer1().toString(), pozitii1);
        participant_pozitii[1] = new Pair(game.getPlayer2().toString(), pozitii2);
        participant_pozitii[2] = new Pair(game.getPlayer3().toString(), pozitii3);
        return participant_pozitii;
    }

    @RequestMapping(value = "/{id1}/players/{id2}", method = RequestMethod.GET)
    public DTO[] getREST2(@PathVariable Integer id1, @PathVariable Integer id2) {
        System.out.println("Get for game " + id1 + " and player " + id2 + " ...");
        DTO[] dtos = new DTO[3];
        List<Score> scores = (List<Score>) moveRepository.getAll();
        int i = 0;
        for (Score score : scores) {
            if (Objects.equals(score.getGame().getId(), id1) &&
                    Objects.equals(score.getPlayer_who_guessed().getId(), id2)
                    && score.getGuess()!= -1) {
                DTO dto = new DTO(score.getPlayer_who_proposed(), score.getGuess().toString(), score.getPoints());
                dtos[i] = dto;
                i++;
            }
        }
        return dtos;
    }

}
