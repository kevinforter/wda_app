package ch.hslu.informatik.swde.wda.persister.exception;

public class CityPersistenceException extends RuntimeException {

    public CityPersistenceException(String message) {
        super(message);
    }

    public CityPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
