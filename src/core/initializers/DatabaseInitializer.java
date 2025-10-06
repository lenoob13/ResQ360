package core.initializers;

import core.records.DatabaseConfig;
import model.services.DatabaseConnector;
import util.Logger;
/**
 * Initializes and configures the database connection at startup.
 * Uses configuration provided by GlobalConfigInitializer.
 * 
 * @author resQ360
 */
public class DatabaseInitializer {
    private final GlobalConfigInitializer configManager;
    private DatabaseConfig dbConfig;
    /**
     * Constructor that takes the global config manager.
     *
     * @param configManager the configuration manager used to retrieve database settings
     */
    public DatabaseInitializer(GlobalConfigInitializer configManager) {
        this.configManager = configManager;
    }
    /**
     * Loads the database configuration and establishes the connection.
     * If the connection fails, a RuntimeException is thrown.
     */
    public void initialize() {
        Logger.info(Logger.Color.BRIGHT_PURPLE + "Initializing database...");
        dbConfig = configManager.getDatabaseConfig();
        logDatabaseInfo();
        try {
            DatabaseConnector.initialize(
                    dbConfig.url(),
                    dbConfig.name(),
                    dbConfig.user(),
                    dbConfig.password()
            );
        } catch (Exception e) {
            Logger.error("Database initialization failed", e);
            throw new RuntimeException("Unable to connect to the database", e);
        }
    }
    /**
     * Closes the current database connection if it exists.
     */
    public void close() {
        DatabaseConnector.get().close();
    }
    /**
     * Logs all database configuration details (URL, host, port, user, etc.).
     */
    private void logDatabaseInfo() {
        Logger.info("*----------[ DATABASE CONFIG ]----------*");
        Logger.info(Logger.Color.BLUE + "URL:      \t" + Logger.Color.GREEN + dbConfig.url());
        Logger.info(Logger.Color.BLUE + "Host:     \t" + Logger.Color.GREEN + dbConfig.host());
        Logger.info(Logger.Color.BLUE + "Port:     \t" + Logger.Color.GREEN + dbConfig.port());
        Logger.info(Logger.Color.BLUE + "Name:     \t" + Logger.Color.GREEN + dbConfig.name());
        Logger.info(Logger.Color.BLUE + "User:     \t" + Logger.Color.GREEN + dbConfig.user());
        Logger.info(Logger.Color.BLUE + "Password: \t" + Logger.Color.GREEN + dbConfig.password());
        Logger.info("*---------------------------------------*");
    }
}
