package model.services;

import core.FXMLView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
/**
 * Utility class to load FXML views along with their controllers.
 *
 * <p>This loader uses a {@link FXMLView} enum to locate the FXML file and return both the root node
 * and the controller in a {@link View} record.</p>
 *
 * <p>Base path for FXML files is {@code /resources/fxml/}.</p>
 *
 * @author ResQ360
 */
public class FXMLViewLoader {

    private static final String BASE_PATH = "/resources/fxml/";
    
    /**
     * A simple container holding both the JavaFX root node and its controller.
     *
     * @param view      the loaded UI root (typically a Pane)
     * @param controller the controller associated with the FXML file
     * @param <T>       the type of the controller
     */
    public record View<T>(Parent view, T controller) {}

    /**
     * Loads the given FXML view and returns both the view and its controller.
     *
     * @param rescuerSessionView the view enum implementing {@link FXMLView}
     * @param <T> the type of the controller associated with the FXML file
     * @return a {@link View} containing the root node and controller
     * @throws RuntimeException if the FXML file fails to load
     */
    public static <T> View<T> loadFXML(FXMLView rescuerSessionView) {
        String path = rescuerSessionView.getPath();
        try {
            FXMLLoader loader = new FXMLLoader(FXMLViewLoader.class.getResource(BASE_PATH + path));
            Parent root = loader.load();
            T controller = loader.getController();
            return new View<>(root, controller);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load FXML: " + path, e);
        }
    }
}
