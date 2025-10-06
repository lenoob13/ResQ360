package core;
/**
 * Represents a generic FXML view with a title and path.
 * Intended to be implemented by enums or classes defining scenes.
 * 
 * @author resQ360
 */
public interface FXMLView {
    /**
     * Returns the title of the view (used for window title, etc.).
     *
     * @return the title of the FXML view
     */
    String getTitle();
    /**
     * Returns the path to the FXML file.
     *
     * @return the path of the FXML view file
     */
    String getPath();
}
