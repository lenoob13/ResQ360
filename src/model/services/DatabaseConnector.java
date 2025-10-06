package model.services;

import core.exception.DatabaseAccessException;
import core.exception.DatabaseAlreadyInitializedException;
import core.exception.DatabaseConnectionException;
import core.exception.DatabaseNotInitializedException;
import util.Logger;

import java.sql.*;

/**
 * Singleton class that manages the connection to a MySQL database.
 *
 * <p>This class handles the full process: connecting to the server, creating the database if needed,
 * connecting to the database, and closing the connection. It uses custom exceptions to handle
 * initialization and connection errors clearly.</p>
 *
 * <p>Use {@code DatabaseConnector.initialize(...)} once to set up the connection,
 * then access the instance via {@code DatabaseConnector.get()}.</p>
 *
 * @author ResQ360
 */
public class DatabaseConnector {
     /**
     * Private constructor to enforce singleton pattern.
     */
    private static DatabaseConnector instance;

    private String urlServer;      // URL serveur (sans base)
    private String databaseName;   // Nom de la base
    private String user;
    private String password;

    private Connection connection;

    private DatabaseConnector() {}

    /**
     * Initializes the singleton instance and connects to the server.
     * Must be called once before using {@link #get()}.
     *
     * @param urlServer     server URL without database name
     * @param databaseName  name of the database
     * @param user          username for the connection
     * @param password      password for the connection
     * @throws DatabaseAlreadyInitializedException if already initialized
     */
    public static synchronized void initialize(String urlServer, String databaseName, String user, String password) {
        if (instance != null) throw new DatabaseAlreadyInitializedException();

        instance = new DatabaseConnector();
        instance.urlServer = urlServer;
        instance.databaseName = databaseName;
        instance.user = user;
        instance.password = password;

        instance.connect(false);

        Logger.info(Logger.Color.BRIGHT_GREEN + "initialized.");
    }

    /**
     * Returns the current database connector instance.
     *
     * @return the singleton instance
     * @throws DatabaseNotInitializedException if not yet initialized
     */
    public static synchronized DatabaseConnector get() {
        if (instance == null)
            throw new DatabaseNotInitializedException();
        return instance;
    }

    /**
     * Connects to the MySQL server (not to a specific database).
     *
     * @return true if the connection is successful
     * @throws DatabaseConnectionException if connection fails
     */
    public boolean connectServer() {
        try {
            connection = DriverManager.getConnection(urlServer, user, password);
            Logger.info("Connecté au serveur MySQL.");
            return true;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Échec de connexion au serveur MySQL.", e);
        }
    }

    /**
     * Checks if the database already exists on the server.
     *
     * @return true if the database exists, false otherwise
     * @throws DatabaseConnectionException if metadata access fails
     */
    public boolean databaseExists() {
        try (ResultSet rs = connection.getMetaData().getCatalogs()) {
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(databaseName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Erreur lors de la vérification des bases existantes.", e);
        }
        return false;
    }

    /**
     * Creates the database if it doesn't already exist.
     *
     * @return true if the database exists or was created
     * @throws DatabaseAccessException if creation fails
     */
    public boolean createDatabaseIfNotExists() {
        if (databaseExists()) {
            Logger.info("La base '" + databaseName + "' existe déjà.");
            return true;
        }

        String sql = "CREATE DATABASE " + databaseName;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            Logger.info("Base '" + databaseName + "' créée.");
            return true;
        } catch (SQLException e) {
            throw new DatabaseAccessException("Erreur lors de la création de la base '" + databaseName + "'", e);
        }
    }

    /**
     * Connects directly to the target database (after connecting to the server).
     *
     * @return true if connection succeeds
     * @throws DatabaseAccessException if connection fails
     */
    public boolean connectDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }

            String separator = urlServer.endsWith("/") ? "" : "/";
            String urlDB = urlServer + separator + databaseName;
            connection = DriverManager.getConnection(urlDB, user, password);
            Logger.info("Connecté à la base '" + databaseName + "'");
            return true;
        } catch (SQLException e) {
            throw new DatabaseAccessException("Échec de connexion à la base '" + databaseName + "'", e);
        }
    }

    /**
     * Returns the current SQL connection object.
     *
     * @return the JDBC connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the current SQL connection if open.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                Logger.info("Connexion fermée.");
            }
        } catch (SQLException e) {
            Logger.error("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    /**
     * Connects to the server and optionally creates the database if missing,
     * then connects to the database.
     *
     * @param createIfMissing if true, create the database if it does not exist
     * @return true if the connection to the database is successful
     */
    public boolean connect(boolean createIfMissing) {
        connectServer();
        if (createIfMissing) createDatabaseIfNotExists();
        return connectDatabase();
    }
}
