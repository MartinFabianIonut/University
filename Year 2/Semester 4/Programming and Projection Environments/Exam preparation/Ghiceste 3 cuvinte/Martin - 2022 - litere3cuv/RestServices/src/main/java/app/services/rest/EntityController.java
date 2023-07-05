package app.services.rest;

import app.rest.persistance.repository.IGameRepository;
import app.rest.persistance.repository.IScoreRepository;
import domain.DTORest;
import domain.Game;
import domain.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/app")
public class EntityController {

    @Autowired
    private IScoreRepository<Integer, Score> scoreRepository;
    @Autowired
    private IGameRepository<Integer, Game> gameRepository;

    @RequestMapping(value = "players/{id}", method = RequestMethod.GET)
    public DTORest[] getAllFinishedGames(@PathVariable Integer id) {
        System.out.println("Get all finished games for player with id " + id + " ...");
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        // get all scores for player with id stream
        List<Score> scoresFinished = scores.stream()
                .filter(score -> score.getPlayer() != null)
                .filter(score -> score.getPlayer().getId().equals(id))
                .filter(score -> score.getRound() == 3)
                .toList();
        // group scores by game
        DTORest[] dtos = new DTORest[scoresFinished.size()];
        int i = 0;
        // for each game, get the playerLetters
        scores.sort(Comparator.comparing(Score::getId));
        for (Score score : scoresFinished) {
            dtos[i] = new DTORest(score.getGame(), score.getTotalPoints(), "");
            String guessedWords = "You guessed: ";
            int nr = 0;
            for (Score score2 : scores) {
                if (score.getTime().equals(score2.getTime()) && score2.getPositionGuessedWord()!=0) {
                    guessedWords += score2.getWord() + ", ";
                    nr++;
                }
            }
            guessedWords += "."; // add new line
            guessedWords += "\n";
            guessedWords += "You guessed " + nr + " words.";
            dtos[i].setLetters(guessedWords);
            i++;
        }
        return dtos;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody Game game) {
        if (game.getWord1().length() < 2) {
            return new ResponseEntity<String>("Invalid word1 - bad length", HttpStatus.BAD_REQUEST);
        }
        if (game.getWord2().length() < 2) {
            return new ResponseEntity<String>("Invalid word2 - bad length", HttpStatus.BAD_REQUEST);
        }
        if (game.getWord3().length() < 2) {
            return new ResponseEntity<String>("Invalid word3 - bad length", HttpStatus.BAD_REQUEST);
        }
        String letters = game.getLetters();
        // verify in word if there are letters not in letters
        for (int i = 0; i < game.getWord1().length(); i++) {
            if (!letters.contains(game.getWord1().charAt(i) + "")) {
                return new ResponseEntity<String>("Invalid - bad letters", HttpStatus.BAD_REQUEST);
            }
        }
        for (int i = 0; i < game.getWord2().length(); i++) {
            if (!letters.contains(game.getWord2().charAt(i) + "")) {
                return new ResponseEntity<String>("Invalid - bad letters", HttpStatus.BAD_REQUEST);
            }
        }
        for (int i = 0; i < game.getWord3().length(); i++) {
            if (!letters.contains(game.getWord3().charAt(i) + "")) {
                return new ResponseEntity<String>("Invalid - bad letters", HttpStatus.BAD_REQUEST);
            }
        }
        // verify if game already exists
        List<Game> games = (List<Game>) gameRepository.getAll();
        // if exists
        for (Game g : games) {
            if (g.getLetters().equals(letters) && g.getWord1().equals(game.getWord1()) && g.getWord2().equals(game.getWord2()) && g.getWord3().equals(game.getWord3())) {
                return new ResponseEntity<String>("Invalid - game already exists", HttpStatus.BAD_REQUEST);
            }
        }
        gameRepository.add(game);
        game.setId(gameRepository.getMaxId());
        System.out.println("Creating game ->" + game);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }
}
