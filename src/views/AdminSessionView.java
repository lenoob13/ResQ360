package views;

import core.FXMLView;
/**
 * Enum representing all the available FXML views in the admin session.
 *
 * <p>Each constant defines a screen or component, with its title and path
 * to the corresponding FXML file.</p>
 *
 * <p>This enum is used to manage navigation and dynamic loading of admin views.</p>
 *
 * @author ResQ360
 */
public enum AdminSessionView implements FXMLView {
    /**
     * Admin home screen.
     */
    HOME("Accueil", "adminSession/homeAdmin.fxml"),
    /**
     * Screen to manage rescuers and events.
     */
    MANAGE_EVENT_RESCUER("Gérer Rescuer & Event", "adminSession/manageRescuerEvent.fxml"),
    /**
     * Form to add or modify a rescuer.
     */
    MODIFY_RESCUER("Add Rescuer", "adminSession/modifyRescuer.fxml"),
    /**
     * Form to modify an event.
     */
    MODIFY_EVENT("Modify Event", "adminSession/modifyEvent.fxml"),

    /**
     * UI component showing a list of event cards.
     */
    LIST_EVENT_CARD("Event Card", "adminSession/components/listEventCard.fxml"),
    /**
     * UI component showing a list of rescuer cards.
     */
    LIST_RESCUER_CARD("Rescuer Card", "adminSession/components/listRescuerCard.fxml"),
    /**
     * Popup to select or view competences for an assignment.
     */
    POPUP_AFFECTATION("Popup Affectation", "adminSession/components/popupCompetence.fxml");

    private final String id;
    private final String path;
    
    /**
     * Constructor for the enum values.
     *
     * @param id   the display title of the view
     * @param path the relative path to the FXML file
     */
    AdminSessionView(String id, String path) {
        this.id = id;
        this.path = path;
    }
    
    /**
     * Returns the display title of the view.
     *
     * @return the view title
     */
    @Override 
    public String getTitle()   { 
        return id; 
    }

    /**
     * Returns the path to the FXML file.
     *
     * @return the view file path
     */
    @Override 
    public String getPath() { 
        return path; 
    }
}
