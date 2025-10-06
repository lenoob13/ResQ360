package core.exception;

import java.sql.SQLException;
/**
 * Exception thrown when the application fails to establish a connection
 * to the database. Wraps underlying SQL exceptions when applicable.
 * 
 * @author resQ360
 */
public class DatabaseConnectionException extends RuntimeException {
    /**
     * Constructs a new DatabaseConnectionException with a custom message.
     *
     * @param message the detail message explaining the failure
     */
    public DatabaseConnectionException(String message) {
        super(message);
    }
    /**
     * Constructs a new DatabaseConnectionException with a custom message
     * and the original SQLException as the cause.
     *
     * @param message the detail message explaining the failure
     * @param e       the underlying SQLException
     */
    public DatabaseConnectionException(String message, SQLException e) {
        super(message, e);
    }
}
