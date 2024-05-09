/**
 * This class extends the RuntimeException class and represents exceptions that are related to city persistence.
 * It includes two constructors: one for specifying the error message and another for specifying the error message and the cause of the exception.
 *
 * @author Kevin Forter
 * @version 1.0
 */

package ch.hslu.informatik.swde.wda.persister.exception;

public class CityPersistenceException extends RuntimeException {

    /**
     * Constructor for the CityPersistenceException class.
     * <p>
     * This constructor calls the superclass constructor with a message parameter.
     * This message is used to provide a description of the exception.
     *
     * @param message the detail message, saved for later retrieval by the Throwable.getMessage() method
     */
    public CityPersistenceException(String message) {
        super(message);
    }

    /**
     * Constructor for the CityPersistenceException class.
     * <p>
     * This constructor calls the superclass constructor with a message and cause parameters.
     * The message is used to provide a description of the exception and the cause is used to represent the underlying reason for the exception.
     *
     * @param message the detail message, saved for later retrieval by the Throwable.getMessage() method
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public CityPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}