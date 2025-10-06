package core.records;
/**
 * Holds configuration parameters for the database connection.
 * Used to centralize DB connection info like URL, credentials, and host details.
 * 
 * @param url the JDBC connection URL
 * @param name the name of the database
 * @param user the username used for authentication
 * @param password the password used for authentication
 * @param host the database server host
 * @param port the database server port
 * 
 * @author resQ360
 */
public record DatabaseConfig(String url, String name, String user, String password, String host, String port) {}
