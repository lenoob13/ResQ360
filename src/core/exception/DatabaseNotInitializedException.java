package core.exception;
/**
 * Exception thrown when the database is accessed before being initialized.
 * Used to prevent illegal operations on an uninitialized database connection.
 * 
 * @author resQ360
 */
public class DatabaseNotInitializedException extends RuntimeException {
    /**
     * Constructs a new DatabaseNotInitializedException with a default message.
     */
    public DatabaseNotInitializedException() {
        super("La base de données n'a pas encore été initialisée.");
    }
}
