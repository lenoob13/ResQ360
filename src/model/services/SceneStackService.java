package model.services;

import core.DataReceiver;
import core.FXMLView;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Logger;

import java.util.Objects;
import java.util.Stack;
import java.util.Optional;

/**
 * Utility class that manages a stack of JavaFX scenes to handle screen navigation.
 *
 * <p>This service supports pushing new views, going back, reloading scenes, and displaying popups.
 * It stores previous scenes in a stack, allowing the user to navigate back through the history.</p>
 *
 * <p>It also supports optional data passing through {@link core.DataReceiver} and debug display of the current stack.</p>
 *
 * @author ResQ360
 */
public final class SceneStackService {
    private static Stage primaryStage;

    /**
     * Represents an entry in the scene stack: a scene and its associated view.
     *
     * @param scene the JavaFX scene
     * @param view  the associated {@link FXMLView}
     */
    private record SceneEntry(Scene scene, FXMLView view) {}
    private static final Stack<SceneEntry> sceneStack = new Stack<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private SceneStackService() {}

    /**
     * Initializes the primary stage used for scene display.
     *
     * @param stage the primary JavaFX stage
     * @throws NullPointerException if the stage is null
     */
    public static void init(Stage stage) {
        primaryStage = Objects.requireNonNull(stage, "Stage cannot be null");
        sceneStack.clear();
    }

    /**
     * Pushes a new scene onto the stack and displays it.
     *
     * @param sessionView the view to load and show
     */
    public static void push(FXMLView sessionView) {
        Objects.requireNonNull(primaryStage, "Primary stage must be initialized");
        Objects.requireNonNull(sessionView, "View cannot be null");

        Scene currentScene = primaryStage.getScene();
        if (currentScene != null)
            sceneStack.push(new SceneEntry(currentScene, sessionView));

        FXMLViewLoader.View<?> view;
        try {
            view = FXMLViewLoader.loadFXML(sessionView);
        } catch (Exception e) {
            System.err.println("Error loading view: " + e.getMessage());
            return;
        }

        Scene newScene = new Scene(view.view());
        primaryStage.setTitle(sessionView.getTitle());
        primaryStage.setScene(newScene);
        primaryStage.show();

        printStack();
    }

    /**
     * Pushes a new scene and passes data to the controller if it implements {@link DataReceiver}.
     *
     * @param sessionView the view to load
     * @param data        the data to inject into the controller
     * @param <T>         the type of the data
     */
    public static <T> void push(FXMLView sessionView, T data) {
        Objects.requireNonNull(primaryStage, "Primary stage must be initialized");
        Objects.requireNonNull(sessionView, "View cannot be null");

        Scene currentScene = primaryStage.getScene();
        if (currentScene != null)
            sceneStack.push(new SceneEntry(currentScene, sessionView));

        FXMLViewLoader.View<?> view;
        try {
            view = FXMLViewLoader.loadFXML(sessionView);
        } catch (Exception e) {
            System.err.println("Error loading view: " + e.getMessage());
            return;
        }

        if (view.controller() instanceof DataReceiver<?>) {
            try {
                DataReceiver<T> receiver = (DataReceiver<T>) view.controller();
                receiver.setData(data);
            } catch (ClassCastException e) {
                System.err.println("Type mismatch for controller data injection: " + e.getMessage());
            }
        }

        Scene newScene = new Scene(view.view());
        primaryStage.setTitle(sessionView.getTitle());
        primaryStage.setScene(newScene);
        primaryStage.show();

        printStack();
    }
    
    /**
     * Pops the current scene and restores the previous one, if available.
     *
     * @return true if a scene was popped and restored, false if the stack was empty
     */
    public static boolean pop() {
        boolean popped = false;
        if (!sceneStack.isEmpty()) {
            SceneEntry entry = sceneStack.pop();
            primaryStage.setScene(entry.scene());
            primaryStage.show();
            popped = true;
        }

        reloadCurrentScene();

        printStack();
        return popped;
    }

    /**
     * Reloads the current scene from its associated FXML file.
     */
    public static void reloadCurrentScene() {
        Objects.requireNonNull(primaryStage, "Primary stage must be initialized");

        Scene currentScene = primaryStage.getScene();
        if (currentScene == null) {
            System.err.println("No scene currently displayed to reload.");
            return;
        }

        // Trouver la dernière vue poussée (celle actuellement affichée)
        if (sceneStack.isEmpty()) {
            System.err.println("Scene stack is empty, cannot reload current scene.");
            return;
        }

        SceneEntry currentEntry = sceneStack.peek();
        FXMLView currentView = currentEntry.view();

        try {
            FXMLViewLoader.View<?> view = FXMLViewLoader.loadFXML(currentView);
            Scene newScene = new Scene(view.view());
            primaryStage.setTitle(currentView.getTitle());
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Failed to reload scene: " + e.getMessage());
        }
    }

    /**
     * Clears the scene stack and replaces the current view.
     *
     * @param sessionView the new view to display
     */
    public static void replaceWith(FXMLView sessionView) {
        sceneStack.clear();
        push(sessionView);
    }

    /**
     * Displays a modal popup using the given view.
     *
     * @param sessionView the view to show in a popup
     */
    public static void showPopup(FXMLView sessionView) {
        Objects.requireNonNull(sessionView, "Popup view cannot be null");

        FXMLViewLoader.View<?> view;
        try {
            view = FXMLViewLoader.loadFXML(sessionView);
        } catch (Exception e) {
            System.err.println("Error loading popup: " + e.getMessage());
            return;
        }

        Stage popupStage = new Stage();
        popupStage.setTitle(sessionView.getTitle());
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(primaryStage);
        popupStage.setScene(new Scene(view.view()));
        popupStage.showAndWait();
    }

    /**
     * Prints the current scene stack to the console for debugging.
     */
    public static void printStack() {
        Logger.info(Logger.Color.BRIGHT_YELLOW + "*----------[ SCENE STACK ]----------*");

        if (sceneStack.isEmpty()) {
            Logger.info(Logger.Color.WHITE + "  [empty]");
            Logger.info(Logger.Color.BRIGHT_YELLOW + "*----------------------------------*");
            return;
        }

        for (int i = 0; i < sceneStack.size(); i++) {
            SceneEntry entry = sceneStack.get(i);
            boolean isTop = (i == sceneStack.size() - 1);

            String prefix = isTop
                    ? Logger.Color.BRIGHT_GREEN + "→ "
                    : "  ";

            String indexStr = String.format("%02d", i);
            String coloredIndex = Logger.Color.CYAN + "[" + indexStr + "]";
            String className = Logger.Color.BLUE + entry.view().getClass().getSimpleName();
            String title = Logger.Color.PURPLE + "\"" + entry.view().getTitle() + "\"";

            Logger.info(prefix + " " + coloredIndex + " " + className + " - " + title);
        }

        Logger.info(Logger.Color.BRIGHT_YELLOW + "*----------------------------------*");
    }


    /**
     * Returns the primary stage wrapped in an {@link Optional}.
     *
     * @return the primary stage if initialized, otherwise empty
     */
    public static Optional<Stage> getPrimaryStage() {
        return Optional.ofNullable(primaryStage);
    }

    /**
     * Clears the scene stack without modifying the current scene.
     */
    public static void clearStack() {
        sceneStack.clear();
    }

    /**
     * Gets the current scene from the top of the stack, if any.
     *
     * @return the current scene wrapped in an Optional
     */
    public static Optional<Scene> getCurrentScene() {
        return sceneStack.isEmpty() ? Optional.empty() : Optional.of(sceneStack.peek().scene());
    }

    /**
     * Returns the number of scenes currently stored in the stack.
     *
     * @return the size of the scene stack
     */
    public static int getStackSize() {
        return sceneStack.size();
    }
}
