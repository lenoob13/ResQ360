package controller.adminSession.elements;

import model.services.SceneStackService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.persistence.DPS;
import views.AdminSessionView;
/**
 * Controller for a card displaying an event (DPS) in the admin interface.
 * Allows displaying basic info and accessing the event modification view.
 * 
 * @author resQ30
 */
public class ListEventCardController {

    private DPS dps;

    @FXML
    private Label dateLabel;

    @FXML
    private Button modifyButton;

    @FXML
    private Label titreLabel;
    /**
     * Opens the event modification view with the current DPS data.
     */
    @FXML
    void modify(ActionEvent event) {
        SceneStackService.push(AdminSessionView.MODIFY_EVENT, dps);
    }
    /**
     * Sets the data of the card with the given DPS (event).
     *
     * @param dps the event to display
     */
    public void setData(DPS dps) {
        this.dps = dps;
        this.dateLabel.setText("date: " + dps.getIdJournee());
        this.titreLabel.setText("sport: " + dps.getIdSport());
    }
}
