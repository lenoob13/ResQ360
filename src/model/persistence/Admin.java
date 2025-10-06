package model.persistence;

/**
 * Represents an administrator account in the system.
 * 
 * <p>This class implements the {@link User} interface and always returns true for {@code isAdmin()}.</p>
 * 
 * <p>Used to manage secure admin logins with identifier and password.</p>
 * 
 * @author ResQ360
 */
public class Admin implements User {
    private final String identifier;
    private final String password;

    /**
     * Creates a new Admin with the given identifier and password.
     *
     * @param identifier the unique login name
     * @param password   the admin password
     */
    public Admin(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    /**
     * Returns the identifier (login) of the admin.
     *
     * @return the identifier
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the password of the admin.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

     /**
     * Always returns true since this is an admin.
     *
     * @return true
     */
    @Override
    public boolean isAdmin() {
        return true;
    }
}
