package core.exception;
/**
 * Exception thrown when an invalid scene transition is attempted.
 * Typically used when a scene cannot be loaded or is misconfigured.
 * 
 * @author resQ360
 */
public class InvalideSceneException extends RuntimeException {
    /**
     * Constructs a new InvalideSceneException with the given message.
     *
     * @param message the detail message explaining the error
     */
    public InvalideSceneException(String message) {
        super(message);
    }
}
