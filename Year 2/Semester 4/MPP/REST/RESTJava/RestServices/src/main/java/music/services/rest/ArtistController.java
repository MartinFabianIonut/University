package music.services.rest;

import music.rest.persistance.repository.IArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rest.domain.Artist;
import rest.service.MyException;

@CrossOrigin
@RestController
@RequestMapping("/music/artists")
public class ArtistController {

        private static final String template = "Hello, %s!";

        @Autowired
        private IArtistRepository<Integer,Artist> artistRepository;

        @RequestMapping("/greeting")
        public  String greeting(@RequestParam(value="name", defaultValue="World") String name) {
            return String.format(template, name);
        }

        @RequestMapping( method=RequestMethod.GET)
        public Artist[] getAll(){
            System.out.println("Get all artists ...");
            Iterable<Artist> artistIterable = artistRepository.getAll();
            int size = 0;
            for (Artist ignored : artistIterable) {
                size++;
            }
            Artist[] artists = new Artist[size];
            int i = 0;
            for (Artist a : artistIterable) {
                artists[i] = a;
                i++;
            }
            return artists;
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.GET)
        public ResponseEntity<?> getById(@PathVariable Integer id){
            System.out.println("Get by id -> "+id);
            Artist artist=artistRepository.findById(id);
            if (artist==null)
                return new ResponseEntity<String>("Artist not found",HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<Artist>(artist, HttpStatus.OK);
        }

        @RequestMapping(method = RequestMethod.POST)
        public Artist create(@RequestBody Artist artist){
            artistRepository.add(artist);
            artist.setId(artistRepository.getMaxId());
            System.out.println("Creating artist ->" + artist);
            return artist;
        }

        @CrossOrigin
        @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
        public Artist update(@PathVariable("id") Integer id, @RequestBody Artist artist) {
             System.out.println("Updating artist to -> " + artist);
             //artist.setId(id);
             artistRepository.update(artist);
             return artist;
        }

        @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
        public ResponseEntity<?> delete(@PathVariable Integer id){
            System.out.println("Deleting artist with id -> "+id);
            artistRepository.delete(id);
            return new ResponseEntity<Artist>(HttpStatus.OK);
        }

        @ExceptionHandler(MyException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public String artistError(MyException e) {
            return e.getMessage();
        }
}
