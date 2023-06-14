package music.services.rest;

public class ServiceException extends RuntimeException {
    public ServiceException(Exception e) {
        super(e);
    }
}