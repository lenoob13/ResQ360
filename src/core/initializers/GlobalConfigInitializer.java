package core.initializers;

import core.records.DatabaseConfig;
import model.loaders.YamlFileLoader;
import util.Logger;
/**
 * Initializes and loads the global configuration of the application.
 * Reads configuration from a YAML file and provides access to values like database settings.
 * 
 * @author resQ360
 */
public class GlobalConfigInitializer {
    private final String configPath;
    private YamlFileLoader configLoader;
    /**
     * Constructor that sets the path to the configuration file.
     *
     * @param configPath path to the YAML configuration file
     */
    public GlobalConfigInitializer(String configPath) {
        this.configPath = configPath;
    }
     /**
     * Loads the configuration file and logs basic app info.
     */
    public void initialize() {
        Logger.info(Logger.Color.BRIGHT_PURPLE + "Initializing configuration...");
        configLoader = new YamlFileLoader();
        try {
            configLoader.loadFrom(configPath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration file.", e);
        }
        logApplicationInfo();
    }
    /**
     * Returns a configuration value for the given key.
     *
     * @param key the configuration key (e.g., "app.name")
     * @return the associated value as a String
     */
    public String get(String key) {
        return configLoader.get(key);
    }
    /**
     * Constructs and returns the full database configuration object.
     *
     * @return a DatabaseConfig object with all database parameters
     */
    public DatabaseConfig getDatabaseConfig() {
        String host = get("database.host");
        String port = get("database.port");
        String name = get("database.name");
        String user = get("database.user");
        String password = get("database.password");
        String url = "jdbc:mysql://" + host + ":" + port;
        return new DatabaseConfig(url, name, user, password, host, port);
    }
    /**
     * Logs the basic application info (name and version) to the console.
     */
    private void logApplicationInfo() {
        Logger.info("*----------[ APPLICATION INFO ]----------*");
        Logger.info(Logger.Color.BLUE + "Name:    \t" + Logger.Color.GREEN + get("app.name"));
        Logger.info(Logger.Color.BLUE + "Version: \t" + Logger.Color.GREEN + get("app.version"));
        Logger.info("*----------------------------------------*");
    }
}
