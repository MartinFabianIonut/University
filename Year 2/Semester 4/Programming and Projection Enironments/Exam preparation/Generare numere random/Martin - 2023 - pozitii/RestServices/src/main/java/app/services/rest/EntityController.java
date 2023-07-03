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

        @RequestMapping(value = "player/{id}", method=RequestMethod.GET)
        public DTORest[] getAll2(@PathVariable Integer id){
            System.out.println("Get cuvinte pentru jocul ..." + id);
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

//        @RequestMapping(value = "/{id}/word", method=RequestMethod.GET)
//        public String getPlayerForWord(@PathVariable Integer id, @RequestParam String word){
//            System.out.println("Get cuvinte pentru jocul ..." + id);
//
//            Game game = gameRepository.findById(id);
//            if (game.getWord1().equals(word)) {
//                return game.getPlayer1().toString();
//            }
//            if (game.getWord2().equals(word)) {
//                return game.getPlayer2().toString();
//            }
//            if (game.getWord3().equals(word)) {
//                return game.getPlayer3().toString();
//            }
//            return null;
//        }

        // dau cuvantul in body, simplu
//        @RequestMapping(value = "/{id}", method=RequestMethod.POST)
//        public String getPlayerForWordPOST(@PathVariable Integer id, @RequestBody String word){
//            System.out.println("Get cuvinte pentru jocul ..." + id + " si cuvantul " + word);
//
//            Game game = gameRepository.findById(id);
//            if (game.getWord1().equals(word)) {
//                return game.getPlayer1().toString();
//            }
//            if (game.getWord2().equals(word)) {
//                return game.getPlayer2().toString();
//            }
//            if (game.getWord3().equals(word)) {
//                return game.getPlayer3().toString();
//            }
//            return null;
//        }


        @RequestMapping(method = RequestMethod.POST)
        public ResponseEntity<?> create(@RequestBody Game game){
            gameRepository.add(game);
            game.setId(gameRepository.getMaxId());
            System.out.println("Creating artist ->" + game);
            return new ResponseEntity<>(game, HttpStatus.OK);
        }

        @CrossOrigin
        @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
        public Score update(@PathVariable("id") Integer id, @RequestBody Score artist) {
             System.out.println("Updating artist to -> " + artist);
             //artist.setId(id);
             scoreRepository.update(artist);
             return artist;
        }

        @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
        public ResponseEntity<?> delete(@PathVariable Integer id){
            System.out.println("Deleting artist with id -> "+id);
            scoreRepository.delete(id);
            return new ResponseEntity<Score>(HttpStatus.OK);
        }

        @ExceptionHandler(MyException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public String artistError(MyException e) {
            return e.getMessage();
        }
}
