package controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
/**
 * Abstract base class for JavaFX controllers.
 * 
 * Provides a default implementation of the initialize method.
 * All controllers should extend this class.
 * @author resQ360
 */
public abstract class Controller implements Initializable {
    /**
     * Called automatically after the FXML file is loaded.
     * Override this method in subclasses if initialization is needed.
     *
     * @param location  the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}
