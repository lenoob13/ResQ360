package model.persistence;
/**
 * Basic interface for a user in the system.
 * 
 * <p>Used for login and role checking (admin or not).</p>
 * 
 * @author ResQ360
 */
public interface User {
    String getIdentifier();
    String getPassword();
    boolean isAdmin();
}
