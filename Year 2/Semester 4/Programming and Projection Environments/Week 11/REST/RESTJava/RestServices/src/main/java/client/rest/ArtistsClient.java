package client.rest;
import rest.domain.Artist;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import music.services.rest.ServiceException;
import java.util.concurrent.Callable;
public class ArtistsClient {
    public static final String URL = "http://localhost:8080/music/artists";
    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Artist[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Artist[].class));
    }

    public Artist getById(Integer id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%d", URL, id), Artist.class));
    }

    public Artist create(Artist artist) {
        return execute(() -> restTemplate.postForObject(URL, artist, Artist.class));
    }

    public void update(Integer id, Artist artist) {
        execute(() -> {
            restTemplate.put(String.format("%s/%d", URL, id), artist);
            return null;
        });
    }

    public void delete(Integer id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%d", URL, id));
            return null;
        });
    }
}
