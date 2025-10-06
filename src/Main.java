import core.initializers.DatabaseInitializer;
import core.initializers.GlobalConfigInitializer;
import model.managers.ManagerContext;
import core.initializers.SceneInitializer;
import javafx.application.Application;
import javafx.stage.Stage;
import model.managers.*;
import util.Logger;
/**
 * Main class that initializes and launches the JavaFX application.
 * It sets up configuration, database, scene stack, and managers.
 * 
 * @author resQ360
 */
public class Main extends Application {
    private static final String CONFIG_PATH = "/resources/config/config.yml";

    private GlobalConfigInitializer configManager;
    private DatabaseInitializer databaseManager;
    private SceneInitializer sceneManager;
    private DPSManager dpsManager;
    private RescuerManager rescuerManager;
    private SportManager sportManager;
    private DayManager dayManager;
    private SiteManager siteManager;
    private ManagerContext managerInitializer;
    /**
     * Entry point when JavaFX starts. Initializes configuration, database,
     * scenes, and all application managers.
     *
     * @param primaryStage the main window stage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Logger.info("Starting application...");

            configManager = new GlobalConfigInitializer(CONFIG_PATH);
            configManager.initialize();

            databaseManager = new DatabaseInitializer(configManager);
            databaseManager.initialize();

            sceneManager = new SceneInitializer(primaryStage);
            sceneManager.initialize();

            managerInitializer = ManagerContext.get();

            rescuerManager = managerInitializer.getRescuerManager();
            dpsManager = managerInitializer.getDpsManager();
            sportManager = managerInitializer.getSportManager();
            dayManager = managerInitializer.getDayManager();
            siteManager = managerInitializer.getSiteManager();

            Logger.info(Logger.Color.BRIGHT_GREEN + "Application started successfully!");

        } catch (Exception e) {
            Logger.error("Error during application startup", e);
            handleStartupError(e);
        }
    }
    /**
     * Handles unexpected exceptions during startup and exits the application.
     *
     * @param e the exception to handle
     */
    private void handleStartupError(Exception e) {
        Logger.error("The application could not start correctly", e);
        System.exit(1);
    }
    /**
     * Called when the application stops. Ensures database is closed properly.
     */
    @Override
    public void stop() throws Exception {
        Logger.info("Stopping application...");
        try {
            if (databaseManager != null) {
                databaseManager.close();
            }
            Logger.info("Database connection closed.");
        } catch (Exception e) {
            Logger.error("Error while closing the database connection", e);
        } finally {
            super.stop();
        }
        Logger.info("Application stopped.");
    }
    /**
     * Main method to launch the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
