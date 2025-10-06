package controller.adminSession.elements;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.persistence.DPS;
import model.persistence.Rescuer;
import model.services.SceneStackService;
import views.AdminSessionView;
/**
 * Controller for a card displaying a rescuer in the admin interface.
 * Allows viewing the name and skills, and editing the rescuer.
 * 
 * @author resQ30
 */
public class ListRescuerCardController {

    private Rescuer rescuer;

    @FXML
    private Label competencesLabel;

    @FXML
    private Button modifyButton;

    @FXML
    private Label nameLabel;
    /**
     * Opens the modification view for the selected rescuer.
     */
    @FXML
    void modify(ActionEvent event) {
        SceneStackService.push(AdminSessionView.MODIFY_RESCUER, rescuer);
    }
    /**
     * Sets the data of the card with the given rescuer.
     *
     * @param rescuer the rescuer to display
     */
    public void setData(Rescuer rescuer) {
        this.rescuer = rescuer;
        nameLabel.setText(rescuer.getName());
        competencesLabel.setText(rescuer.getSkills().toString());
    }
}
