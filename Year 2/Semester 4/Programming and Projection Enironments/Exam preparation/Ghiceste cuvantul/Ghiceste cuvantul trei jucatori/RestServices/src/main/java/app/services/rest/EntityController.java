package app.services.rest;

import app.rest.persistance.repository.IGameRepository;
import app.rest.persistance.repository.IScoreRepository;
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

@CrossOrigin
@RestController
@RequestMapping("/app/game")
public class EntityController {

        @Autowired
        private IScoreRepository<Integer, Score> moveRepository;
        @Autowired
        private IGameRepository<Integer, Game> gameRepository;

        @RequestMapping(value = "/{id1}/player/{id2}", method=RequestMethod.GET)
        public Pair[] getAll(@PathVariable Integer id1, @PathVariable Integer id2){
            System.out.println("Get propuneri pt joc " + id1 + " si player " + id2 + " ...");
            Pair[] pairs = new Pair[3];
            List<Score> scores = (List<Score>) moveRepository.getAll();
            int i = -1;
            for (Score score : scores) {
                if (score.getPlayer_who_guessed() != null)
                    if(score.getPlayer_who_guessed().getId().equals(id2)
                            && score.getGame().getId().equals(id1)) {
                        Pair pair = new Pair(score.getGuess(), score.getPoints());
                        if (i > -1) {
                            pairs[i] = pair;
                        }
                        i++;
                    }
            }
            return pairs;
        }

        @RequestMapping(value = "/{id}", method=RequestMethod.GET)
        public String[] getAll2(@PathVariable Integer id){
            System.out.println("Get cuvinte pentru jocul ..." + id);
            String[] cuvinte = new String[3];
            Game game = gameRepository.findById(id);
            cuvinte[0] = game.getWord1();
            cuvinte[1] = game.getWord2();
            cuvinte[2] = game.getWord3();
            return cuvinte;
        }

        @RequestMapping(value = "/{id}/word", method=RequestMethod.GET)
        public String getPlayerForWord(@PathVariable Integer id, @RequestParam String word){
            System.out.println("Get cuvinte pentru jocul ..." + id);

            Game game = gameRepository.findById(id);
            if (game.getWord1().equals(word)) {
                return game.getPlayer1().toString();
            }
            if (game.getWord2().equals(word)) {
                return game.getPlayer2().toString();
            }
            if (game.getWord3().equals(word)) {
                return game.getPlayer3().toString();
            }
            return null;
        }

        // dau cuvantul in body, simplu
        @RequestMapping(value = "/{id}", method=RequestMethod.POST)
        public String getPlayerForWordPOST(@PathVariable Integer id, @RequestBody String word){
            System.out.println("Get cuvinte pentru jocul ..." + id + " si cuvantul " + word);

            Game game = gameRepository.findById(id);
            if (game.getWord1().equals(word)) {
                return game.getPlayer1().toString();
            }
            if (game.getWord2().equals(word)) {
                return game.getPlayer2().toString();
            }
            if (game.getWord3().equals(word)) {
                return game.getPlayer3().toString();
            }
            return null;
        }


        @RequestMapping(method = RequestMethod.POST)
        public Score create(@RequestBody Score artist){
            moveRepository.add(artist);
            artist.setId(moveRepository.getMaxId());
            System.out.println("Creating artist ->" + artist);
            return artist;
        }

        @CrossOrigin
        @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
        public Score update(@PathVariable("id") Integer id, @RequestBody Score artist) {
             System.out.println("Updating artist to -> " + artist);
             //artist.setId(id);
             moveRepository.update(artist);
             return artist;
        }

        @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
        public ResponseEntity<?> delete(@PathVariable Integer id){
            System.out.println("Deleting artist with id -> "+id);
            moveRepository.delete(id);
            return new ResponseEntity<Score>(HttpStatus.OK);
        }

        @ExceptionHandler(MyException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public String artistError(MyException e) {
            return e.getMessage();
        }
}
