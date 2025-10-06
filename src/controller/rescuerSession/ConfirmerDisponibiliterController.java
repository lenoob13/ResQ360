package controller.rescuerSession;

import controller.Controller;
import core.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import model.services.SceneStackService;
import views.RescuerSessionView;
/**
 * Controller for the "Confirm Availability" screen for rescuers.
 * Displays event details and allows navigation.
 * @author resQ30
 */
public class ConfirmerDisponibiliterController extends Controller {

    @FXML
    private ImageView ButtonAction;

    @FXML
    private ImageView buttonLogOut;

    @FXML
    private ImageView buttonReturn;

    @FXML
    private Label coordLabel;

    @FXML
    private Label dateHoraireLabel;

    @FXML
    private Button logOutButton;

    @FXML
    private Button logoButton;

    @FXML
    private WebView mapWebView;

    @FXML
    private Label noteTexte;

    @FXML
    private Button returnButton;

    @FXML
    private Label titreLabel;
    /**
     * Handles the click on the "Return" button.
     * Navigates back to the previous screen.
     *
     * @param event Action event
     */
    @FXML
    void returnToPrevious(ActionEvent event) {
        SceneStackService.pop();
    }
    /**
     * Handles the click on the "Home" button.
     * Navigates to the rescuer home screen.
     *
     * @param event Action event
     */
    @FXML
    void goHome(ActionEvent event) {
        SceneStackService.replaceWith(RescuerSessionView.ACCUEIL);
    }
    /**
     * Handles the logout action.
     * Logs out the current user and resets the session.
     *
     * @param event Action event
     */
    @FXML
    void logOut(ActionEvent event) {
        Session.logout();
    }
}
