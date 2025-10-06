package views;

import core.FXMLView;
/**
 * Enum representing global views shared across the entire application.
 *
 * <p>Each value contains a title and the path to its corresponding FXML file.
 * Currently, this enum only includes the login screen.</p>
 *
 * <p>Implements {@link FXMLView} to allow consistent navigation handling.</p>
 *
 * @author ResQ360
 */
public enum GlobalView implements FXMLView {
    /**
     * Login screen view.
     */
    LOGIN("Connexion", "connexion.fxml");

    private final String id;
    private final String path;
    
    /**
     * Constructor for a global view.
     *
     * @param id   the title of the view
     * @param path the FXML file path
     */
    GlobalView(String id, String path) {
        this.id = id;
        this.path = path;
    }
    /**
     * Returns the view title.
     *
     * @return the display title
     */
    @Override 
    public String getTitle() { 
        return id; 
    }
    
    /**
     * Returns the path to the FXML file.
     *
     * @return the FXML file path
     */
    @Override 
    public String getPath() { 
        return path; 
    }
}
