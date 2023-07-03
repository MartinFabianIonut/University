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
import martin.service.MyException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/app")
public class EntityController {

        @Autowired
        private IScoreRepository<Integer, Score> scoreRepository;
        @Autowired
        private IGameRepository<Integer, Game> gameRepository;

        @RequestMapping(value = "players/{id}", method=RequestMethod.GET)
        public DTORest[] getAll2(@PathVariable Integer id){
            System.out.println("Get finished games for player with id ..." + id);
            List<Score> scores = (List<Score>) scoreRepository.getAll();
            // get all scores for player with id stream
            scores = scores.stream()
                    .filter(score -> score.getPlayer() != null)
                    .filter(score -> score.getPlayer().getId().equals(id))
                    .filter(score -> score.getAttempts() == 10 || score.getText() != null)
                    .collect(java.util.stream.Collectors.toList());
            DTORest[] dtos = new DTORest[scores.size()];
            int i = 0;
            for (Score score : scores) {
                DTORest dto = new DTORest(score.getAttempts(), score.getPositions(), score.getText() != null ? score.getText() : "");
                dtos[i] = dto;
                i++;
            }
            return dtos;
        }

        @RequestMapping(method = RequestMethod.POST)
        public ResponseEntity<?> create(@RequestBody Game game){
            if (game.getCol() < 0 || game.getCol() > 3) {
                return new ResponseEntity<>("Invalid column!", HttpStatus.BAD_REQUEST);
            }
            if (game.getRow() < 0 || game.getRow() > 3) {
                return new ResponseEntity<>("Invalid row!", HttpStatus.BAD_REQUEST);
            }
            gameRepository.add(game);
            game.setId(gameRepository.getMaxId());
            System.out.println("Creating artist ->" + game);
            return new ResponseEntity<>(game, HttpStatus.OK);
        }
}
