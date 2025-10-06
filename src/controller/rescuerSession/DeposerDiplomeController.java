package controller.rescuerSession;

import controller.Controller;
import core.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.services.SceneStackService;
import views.RescuerSessionView;
/**
 * Controller for the "Upload Diploma" screen for rescuers.
 * Handles file selection, validation, and navigation actions.
 * @author resQ30
 */
public class DeposerDiplomeController extends Controller {
    
    @FXML
    private ImageView buttonHomeLogo;

    @FXML
    private VBox handleFileUploadButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button logoButton;

    @FXML
    private Button returnButton;

    @FXML
    private Button uploadDiplomaButton;

    @FXML
    private Button validateDiplomaSauvButton;
    /**
     * Handles the mouse click on the diploma file upload area.
     * Intended to trigger the file chooser dialog (not yet implemented).
     *
     * @param event Mouse click event
     */
    @FXML
    void handleFileUpload(MouseEvent event) {
        // TODO : gérer la sélection du fichier
    }
     /**
     * Handles the action of clicking the "Upload" button after selecting a diploma file.
     * Logic for uploading the diploma file is not yet implemented.
     *
     * @param event Button click event
     */
    @FXML
    void handleFileUploadDiploma(ActionEvent event) {
        // TODO : gérer le upload du diplôme
    }
    /**
     * Handles the click on the "Validate" button after uploading a diploma.
     * Returns to the previous screen.
     *
     * @param event Button click event
     */
    @FXML
    void validateDiplomaSauv(ActionEvent event) {
        SceneStackService.pop();
    }
    /**
     * Handles the click on the "Return" button.
     * Navigates back to the previous screen in the stack.
     *
     * @param event Button click event
     */
    @FXML
    void returnToPrevious(ActionEvent event) {
        SceneStackService.pop();
    }
     /**
     * Handles the click on the "Home" logo or button.
     * Navigates back to the rescuer's main home page.
     *
     * @param event Button click event
     */
    @FXML
    void goHome(ActionEvent event) {
        SceneStackService.replaceWith(RescuerSessionView.ACCUEIL);
    }
    /**
     * Handles the logout action.
     * Logs out the current user and resets the session.
     *
     * @param event Button click event
     */
    @FXML
    void logOut(ActionEvent event) {
        Session.logout();
    }
}
