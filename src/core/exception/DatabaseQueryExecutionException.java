package core.exception;
/**
 * Exception thrown when a database query fails to execute properly.
 * Used to signal SQL-related errors during runtime.
 * 
 * @author resQ360
 */
public class DatabaseQueryExecutionException extends RuntimeException {
    /**
     * Constructs a new DatabaseQueryExecutionException with the specified message.
     *
     * @param message a detailed message explaining the query failure
     */
    public DatabaseQueryExecutionException(String message) {
        super(message);
    }
}
