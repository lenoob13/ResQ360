package core.exception;
/**
 * Exception thrown when there is an issue accessing the database,
 * such as permission errors or failed queries.
 * 
 * @author resQ360
 */
public class DatabaseAccessException extends RuntimeException {
    /**
     * Constructs a new exception with a detailed message.
     *
     * @param message the detail message describing the error
     */
    public DatabaseAccessException(String message) {
        super(message);
    }
    /**
     * Constructs a new exception with a detailed message and a cause.
     *
     * @param message the detail message describing the error
     * @param cause   the cause of the exception (e.g., another exception)
     */
    public DatabaseAccessException(String message, Throwable cause) {
      super(message, cause);
    }
}
