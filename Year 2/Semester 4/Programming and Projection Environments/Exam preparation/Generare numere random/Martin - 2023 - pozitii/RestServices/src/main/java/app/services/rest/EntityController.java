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
            System.out.println("Get games for player ..." + id);
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
                DTORest dto = new DTORest(score.getGame(), score.getTotalSum());
                dtos[i] = dto;
                i++;
            }
            return dtos;
        }

        @RequestMapping(method = RequestMethod.POST)
        public ResponseEntity<?> create(@RequestBody Game game){
            gameRepository.add(game);
            game.setId(gameRepository.getMaxId());
            System.out.println("Creating game ->" + game);
            return new ResponseEntity<>(game, HttpStatus.OK);
        }
}
