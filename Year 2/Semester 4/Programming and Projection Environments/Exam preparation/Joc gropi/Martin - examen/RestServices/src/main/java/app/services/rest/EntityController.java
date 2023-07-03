package app.services.rest;

import app.rest.persistance.repository.IGameRepository;
import app.rest.persistance.repository.IPositionRepository;
import app.rest.persistance.repository.IScoreRepository;
import domain.DTORest;
import domain.Game;
import domain.Position;
import domain.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import martin.service.MyException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/app")
public class EntityController {

    @Autowired
    private IScoreRepository<Integer, Score> scoreRepository;
    @Autowired
    private IPositionRepository<Integer, Position> positionRepository;
    @Autowired
    private IGameRepository<Integer, Game> gameRepository;

    @RequestMapping(value = "players/{id}", method = RequestMethod.GET)
    public DTORest[] getAllGamesForPlayer(@PathVariable Integer id) {
        System.out.println("Get games for player with id = " + id);
        List<Score> allScores = (List<Score>) scoreRepository.getAll();
        List<Score> scores = (List<Score>) scoreRepository.getAll();
        // get all scores for player with id stream
        scores = scores.stream()
                .filter(score -> score.getPlayer() != null)
                .filter(score -> score.getPlayer().getId().equals(id))
                .filter(score -> score.getGameOver() == 1)
                .collect(java.util.stream.Collectors.toList());
        DTORest[] dtos = new DTORest[scores.size()];
        int i = 0;
        for (Score score : scores) {
            int seconds = 0;
            List<Position> positions = new ArrayList<>();
            for (Score score1 : allScores) {
                if (score1.getPlayer().getId().equals(id) && score1.getRound() == 0
                        && score1.getGame().getId().equals(score.getGame().getId())) {
                    Duration duration = Duration.between(score1.getTime(), score.getTime());
                    long milliseconds = duration.toMillis();
                    seconds += milliseconds;
                }
                if (score1.getPlayer().getId().equals(id) && score1.getRound() != 0
                        && score1.getGame().getId().equals(score.getGame().getId())) {
                    positions.add(positionRepository.findById(score1.getGuess().getId()));
                }
            }
            DTORest dto = new DTORest(score.getGame(), positions, score.getTotalPoints(), seconds);
            dtos[i] = dto;
            i++;
        }
        return dtos;
    }

    @CrossOrigin
    @RequestMapping(value = "/games/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> changeOnePositionAtGame(@PathVariable Integer id, @RequestBody Position position) {
        if (position.getRow() < 0 || position.getRow() > 3)
            return new ResponseEntity<>("Row must be between 0 and 3", HttpStatus.BAD_REQUEST);
        if (position.getCol() < 0 || position.getCol() > 3)
            return new ResponseEntity<>("Col must be between 0 and 3", HttpStatus.BAD_REQUEST);
        Position currentPosition = positionRepository.findByRowCol(position.getRow(), position.getCol());
        if (currentPosition == null) {
            positionRepository.add(position);
            position.setId(positionRepository.getMaxId());
            currentPosition = position;
        }
        int random = (int) (Math.random() * 5);
        Game game = gameRepository.findById(id);
        if (game == null)
            return new ResponseEntity<>("Game not found", HttpStatus.BAD_REQUEST);
        if (random == 0)
            game.setPosition1(currentPosition);
        if (random == 1)
            game.setPosition2(currentPosition);
        if (random == 2)
            game.setPosition3(currentPosition);
        if (random == 3)
            game.setPosition4(currentPosition);
        if (random == 4)
            game.setPosition5(currentPosition);
        gameRepository.update(game);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }
}
