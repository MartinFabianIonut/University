package app.services.rest;

public class ServiceException extends RuntimeException {
    public ServiceException(Exception e) {
        super(e);
    }
}