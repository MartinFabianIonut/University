package start;
import rest.domain.Artist;
import music.services.rest.ServiceException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import client.rest.ArtistsClient;

import java.io.IOException;
import java.util.Properties;

public class StartRestCLIENT {

    private static ArtistsClient artistsClient = new ArtistsClient();
    public static void main(String[] args){
        RestTemplate restTemplate = new RestTemplate();
        Artist artistT = new Artist(12, "Test firstname", "Test lastname");
        try{
            Artist newArtist = artistsClient.create(artistT);
            show(() -> System.out.println(newArtist));
            newArtist.setFirstName("Test firstname updated");
            artistsClient.update(newArtist.getId(), newArtist);
            show(() -> System.out.println(artistsClient.getById(newArtist.getId())));
            show(() -> {
                Artist[] res = artistsClient.getAll();
                for (Artist a : res) {
                    System.out.println(a);
                }
            });
            artistsClient.delete(newArtist.getId());
            // get all
            show(() -> {
                Artist[] res = artistsClient.getAll();
                for (Artist a : res) {
                    System.out.println(a);
                }
            });
        } catch (RestClientException ex) {
            System.out.println("Exception ... " + ex.getMessage());
        }

    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception"+ e);
        }
    }
}
