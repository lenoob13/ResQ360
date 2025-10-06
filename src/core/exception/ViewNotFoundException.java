package core.exception;
/**
 * Exception thrown when a requested view cannot be found.
 * Typically used when loading FXML views fails due to an invalid path.
 * 
 * @author resQ360
 */
public class ViewNotFoundException extends RuntimeException {
     /**
     * Constructs a new ViewNotFoundException with the given message.
     *
     * @param message the detail message explaining the error
     */
    public ViewNotFoundException(String message) {
        super(message);
    }
}
