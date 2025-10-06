package core.initializers;

import core.FXMLView;
import javafx.stage.Stage;
import model.services.FXMLViewLoader;
import model.services.SceneStackService;
import util.Logger;
import views.AdminSessionView;
import views.GlobalView;
import views.RescuerSessionView;
/**
 * Initializes the entire JavaFX scene stack.
 * Loads all FXML views at application startup, 
 * sets the primary stage, and displays the login screen.
 * 
 * @author resQ360
 */
public class SceneInitializer {
    private final Stage primaryStage;
    /**
     * Constructor that sets the JavaFX primary stage.
     * 
     * @param primaryStage the main JavaFX stage of the application
     */
    public SceneInitializer(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    /**
     * Initializes all scenes for the application.
     * Loads each FXML view and sets the initial scene.
     */
    public void initialize() {
        Logger.info(Logger.Color.BRIGHT_PURPLE + "Loading scenes...");
        try {
            initializeSceneStack();
            preloadAllScenes();
            pushPrimaryScene();
        } catch (Exception e) {
            Logger.error("Error while loading scenes", e);
            throw new RuntimeException("Unable to load scenes", e);
        }
    }
    /**
     * Initializes the scene stack with the application's primary stage.
     */
    private void initializeSceneStack() {
        SceneStackService.init(primaryStage);
    }
    /**
     * Preloads all scenes from Admin, Rescuer, and Global view enums.
     * This improves performance by loading them only once.
     */
    private void preloadAllScenes() {
        for (FXMLView view : RescuerSessionView.values())
            FXMLViewLoader.loadFXML(view);
        for (FXMLView view : GlobalView.values())
            FXMLViewLoader.loadFXML(view);
        for (FXMLView view : AdminSessionView.values())
            FXMLViewLoader.loadFXML(view);
    }
    /**
     * Pushes the first screen to show: the login screen.
     */
    private void pushPrimaryScene() {
        SceneStackService.push(GlobalView.LOGIN);
    }
}
