import domain.Artist;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import repository.ArtistDBRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractSet;
import java.util.List;
import java.util.Properties;

public class TestDB {

    @Test
    public void runAllTests(){
        Properties props=new Properties();
        try {
            props.load(new FileReader("db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find db.config "+e);
        }
        Assertions.assertEquals(props.size(),1);

        ArtistDBRepository artistDBRepository = new ArtistDBRepository(props);
        for(int i=1;i<=5;i++)
            artistDBRepository.add(new Artist(i,"Artist"+i,"Last"+i));
        List<Artist> artists = (List<Artist>) artistDBRepository.getAll();
        Assertions.assertEquals(artists.size(),5);
        Assertions.assertEquals(artistDBRepository.findById(artists.get(4).getId()).getFirstName(),"Artist5");
        artistDBRepository.delete(artists.get(4).getId());
        artistDBRepository.delete(artists.get(3).getId());
        Assertions.assertFalse(artistDBRepository.delete(artists.get(4).getId()));

        artists = (List<Artist>) artistDBRepository.getAll();
        Assertions.assertEquals(artists.size(),3);

        artistDBRepository.update(new Artist(artists.get(1).getId(),"Artist nou","Last name"));
        Assertions.assertEquals(artistDBRepository.findById(artists.get(1).getId()).getLastName(),"Last name");
        Assertions.assertFalse(artistDBRepository.update(new Artist(-7,"Artist nou","Last name")));
        artists.forEach(artist -> artistDBRepository.delete(artist.getId()));
    }
}
