package controller.rescuerSession.elements;

import controller.Controller;
import controller.rescuerSession.PlanningDetailsController;
import core.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.managers.ManagerContext;
import model.persistence.DPS;
import model.services.SceneStackService;
import util.Logger;
import views.RescuerSessionView;
/**
 * Controller for a rescuer's event card. Displays the event's name and date,
 * and handles navigation to the detailed view when the button is clicked.
 * @author resQ30
 */
public class RescuerEventCardController extends Controller {

    @FXML
    private Label dateEpreuveTxt;

    @FXML
    private Button epreuveButton;

    @FXML
    private Label nomEpreuveTXt;

    @FXML
    private VBox vTxt;

    private String dateEpreuve;
    private String nomEpreuve;
    private DPS dps;
    /**
     * Sets the data to be displayed on the event card.
     *
     * @param dateEpreuve The event date as a string
     * @param nomEpreuve  The name of the event
     * @param dps         The DPS object associated with the event
     */
    public void setData(String dateEpreuve, String nomEpreuve, DPS dps) {
        this.dateEpreuve = dateEpreuve;
        this.nomEpreuve = nomEpreuve;

        dateEpreuveTxt.setText(dateEpreuve);
        nomEpreuveTXt.setText(nomEpreuve);
        this.dps = dps;
    }
    /**
     * Called when the event button is clicked.
     * Pushes the event's detailed view onto the scene stack.
     *
     * @param event Action event
     */
    @FXML
    void goToEpreuve(ActionEvent event) {
        Logger.info("Accès à l'épreuve : " + nomEpreuve + " du " + dateEpreuve);
        SceneStackService.<DPS>push(RescuerSessionView.PLANNING_DETAILS,dps);
    }
}
