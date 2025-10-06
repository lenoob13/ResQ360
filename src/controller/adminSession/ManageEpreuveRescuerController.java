package controller.adminSession;

import controller.Controller;
import controller.adminSession.elements.ListEventCardController;
import controller.adminSession.elements.ListRescuerCardController;
import model.managers.DPSManager;
import model.managers.RescuerManager;
import model.services.FXMLViewLoader;
import model.services.SceneStackService;
import core.Session;
import model.managers.ManagerContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import views.AdminSessionView;

import java.net.URL;
import java.util.ResourceBundle;
/**
 * Controller for managing events and rescuers.
 * Loads all existing events and rescuers into their containers.
 * Allows adding new ones.
 *
 * @author resQ30
 */
public class ManageEpreuveRescuerController extends Controller {

    DPSManager dpsManager;
    RescuerManager rescuerManager;
    /**
     * Initializes the page by loading all events and rescuers into their containers.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation
        dpsManager = ManagerContext.get().getDpsManager();
        rescuerManager = ManagerContext.get().getRescuerManager();

        // Chargement des epreuves
        dpsManager.getAll().forEach(event -> {
            FXMLViewLoader.View<ListEventCardController> card = FXMLViewLoader.loadFXML(AdminSessionView.LIST_EVENT_CARD);
            card.controller().setData(event);
            eventContainer.getChildren().add(card.view());
        });

        // Chargement des sauveteurs
        rescuerManager.getAll().forEach(rescuer -> {
            FXMLViewLoader.View<ListRescuerCardController> card = FXMLViewLoader.loadFXML(AdminSessionView.LIST_RESCUER_CARD);
            card.controller().setData(rescuer);
            sauveteurContainer.getChildren().add(card.view());
        });

    }

    @FXML
    private Button addAccountButton;

    @FXML
    private Button addEventButton;

    @FXML
    private VBox eventContainer;

    @FXML
    private Button logOutButton;

    @FXML
    private Button logoButton;

    @FXML
    private Button returnButton;

    @FXML
    private VBox sauveteurContainer;

    @FXML
    private TextField searchAccountField;

    @FXML
    private TextField searchEventField;
    /**
     * Opens the add rescuer form.
     */
    @FXML
    void addRescuer(ActionEvent event) {
        SceneStackService.push(AdminSessionView.MODIFY_RESCUER, null);
    }
    /**
     * Opens the add event form.
     */
    @FXML
    void addEvent(ActionEvent event) {
        SceneStackService.push(AdminSessionView.MODIFY_EVENT, null);
    }
    /**
     * Navigates to the admin home page.
     */
    @FXML
    void goHome(ActionEvent event) {
        SceneStackService.replaceWith(AdminSessionView.HOME);
    }
    /**
     * Logs out the current user.
     */
    @FXML
    void logOut(ActionEvent event) {
        Session.logout();
    }
    /**
     * Goes back to the previous screen.
     */
    @FXML
    void returnToPrevious(ActionEvent event) {
        SceneStackService.pop();
    }
}
