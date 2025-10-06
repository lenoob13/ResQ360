package controller.rescuerSession;

import controller.rescuerSession.PlanningDetailsController;
import controller.Controller;
import controller.rescuerSession.elements.RescuerEventCardController;
import core.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.managers.ManagerContext;
import model.services.FXMLViewLoader;
import model.services.SceneStackService;
import util.Logger;
import views.RescuerSessionView;

import java.net.URL;
import java.util.ResourceBundle;
/**
 * Controller for the rescuer's home page.
 * Displays upcoming assignments and allows access to personal info, logout, and account deletion.
 * @author resQ30
 */
public class HomeRescuerController extends Controller {
    /**
     * Initializes the rescuer home view.
     * Sets the welcome message and loads all assigned events for the rescuer.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Set user name
        if (Session.getCurrentUser() != null) {
            String identifier = Session.getCurrentUser().getIdentifier();
        bjrUser.setText("Bonjour, " + identifier);
        }

        // Set text
        try {
            for (int i : Session.getCurrentUser().getAssignments()) {
                FXMLViewLoader.View<RescuerEventCardController> card = FXMLViewLoader.loadFXML(RescuerSessionView.RESCUER_EVENT_CARD);

                String nomEpreuve = ManagerContext.get().getSportManager().getSport(ManagerContext.get().getDpsManager().getDPS(i).getIdSport()).getName();
                String day=ManagerContext.get().getDayManager().getDayById(ManagerContext.get().getDpsManager().getDPS(i).getIdJournee()).toString();

                card.controller().setData(day, nomEpreuve,ManagerContext.get().getDpsManager().getDPS(i));

                listEvent.getChildren().add(card.view());
            }
        }catch (NullPointerException e){
            Logger.error("SQL and POO are different or session without user");
        }
    }

    @FXML
    private ImageView ButtonAction;

    @FXML
    private Label bjrUser;

    @FXML
    private ImageView buttonLogOut;

    @FXML
    private Button deleteAccountButton;

    @FXML
    private Button infoPersoSauvButton;

    @FXML
    private Label infoPersoTxt;

    @FXML
    private VBox listEvent;

    @FXML
    private Button logOutButton;

    @FXML
    private Button logoButton;

    @FXML
    private ScrollPane scrollEvent;

    @FXML
    private SplitPane split;

    @FXML
    private Label supTxt;

    @FXML
    private Label txtBienvenue;
    /**
     * Handles the click on "Delete account" button.
     * Shows a confirmation popup.
     */
    @FXML
    void deleteAccount(ActionEvent event) {
        SceneStackService.showPopup(RescuerSessionView.POPUP_ACCOUNT_DELETE);
    }
    /**
     * Handles the click on "Personal info" button.
     * Navigates to the rescuer's personal information page.
     */
    @FXML
    void infoPersoSauv(ActionEvent event) {
        SceneStackService.push(RescuerSessionView.INFO_PERSO);
    }
    /**
     * Handles the click on the home button.
     * Navigates back to the home (accueil) page.
     */
    @FXML
    void goHome(ActionEvent event) {
        SceneStackService.replaceWith(RescuerSessionView.ACCUEIL);
    }
    /**
     * Logs out the current user.
     */
    @FXML
    void logOut(ActionEvent event) {
        Session.logout();
    }
}
