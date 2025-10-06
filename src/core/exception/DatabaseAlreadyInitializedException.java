package core.exception;
/**
 * Exception thrown when an attempt is made to initialize the database
 * after it has already been initialized.
 * This prevents redundant or conflicting configuration.
 * 
 * @author resQ360
 */
public class DatabaseAlreadyInitializedException extends RuntimeException {
    /**
     * Constructs a new exception with a default message.
     */
    public DatabaseAlreadyInitializedException() {
        super("Database has already been initialized");
    }
    /**
     * Constructs a new exception with a custom message.
     *
     * @param message the detail message explaining the error
     */
    public DatabaseAlreadyInitializedException(String message) {
        super(message);
    }
}
