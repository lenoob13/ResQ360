package views;

import core.FXMLView;
/**
 * Enum representing all the available FXML views in the rescuer session.
 *
 * <p>This includes main screens and reusable UI components for a rescuer user.
 * Each enum constant stores a display title and the path to its FXML file.</p>
 *
 * <p>Used to handle navigation and screen loading in a consistent way
 * through the {@link FXMLView} interface.</p>
 *
 * @author ResQ360
 */
public enum RescuerSessionView implements FXMLView {

    // scenes
    
    /**
     * Home screen for the rescuer.
     */
    ACCUEIL(                "Accueil",                  "rescuerSession/homeRescuer.fxml"),

    /**
     * Screen to confirm rescuer availability.
     */
    CONFIRMER_DISPO(        "confirmerDisponibiliter",  "rescuerSession/confirmerDisponibiliter.fxml"),

    /**
     * Screen to upload a diploma.
     */
    DEPOSER_DIPLOME(        "deposerDiplome",           "rescuerSession/deposerDiplome.fxml"),

    /**
     * Screen showing the rescuer's personal information.
     */
    INFO_PERSO(             "InfoPersoSauveteur",       "rescuerSession/InfoPersoSauveteur.fxml"),

    /**
     * Screen displaying details of the rescuer's planning.
     */
    PLANNING_DETAILS(       "planningDetails",          "rescuerSession/planningDetails.fxml"),

    // components

    /**
     * UI card component representing an event for the rescuer.
     */
    RESCUER_EVENT_CARD(     "caseEpreuve",              "rescuerSession/components/rescuerEventCard.fxml"),
    /**
     * Popup to confirm account deletion.
     */
    POPUP_ACCOUNT_DELETE(   "popupSupCompte",           "rescuerSession/components/popupAccountDelete.fxml");

    private final String title;
    private final String path;

    /**
     * Constructor for a rescuer view.
     *
     * @param title the display name of the view
     * @param path  the path to the FXML file
     */
    RescuerSessionView(String title, String path) {
        this.title = title;
        this.path = path;
    }

    /**
     * Returns the view's title.
     *
     * @return the display title
     */
    @Override 
    public String getTitle()  { 
        return title; 
    }
    
    /**
     * Returns the path to the FXML file.
     *
     * @return the FXML path
     */
    @Override 
    public String getPath()   { 
        return path;  
    }
}
