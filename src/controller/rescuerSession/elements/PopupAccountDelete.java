package controller.rescuerSession.elements;

import controller.Controller;
import model.managers.ManagerContext;
import core.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import util.Logger;
/**
 * Controller for the popup that confirms rescuer account deletion.
 * @author resQ30
 */
public class PopupAccountDelete extends Controller {

    @FXML
    private Button deleteAccountSauvButton;

    @FXML
    private Button quitButton;

    private String userId;
    /**
     * Sets the user ID and updates the button label.
     *
     * @param userId the identifier of the user
     */
    public void setUserId(String userId) {
        this.userId = userId;
        deleteAccountSauvButton.setText("Delete account (" + userId + ")");
    }
    /**
     * Called when the user confirms account deletion.
     *
     * @param event Action event
     */
    @FXML
    void deleteAccountSauv(ActionEvent event) {
        Logger.info("Suppression du compte pour l'utilisateur " + userId);
        ManagerContext.get().getRescuerManager().remove(Session.getCurrentUser().getId());
        Session.logout();
    }
    /**
     * Called when the user clicks "Cancel".
     *
     * @param event Action event
     */
    @FXML
    void quitAction(ActionEvent event) {
        quit();
    }
    /**
     * Closes the popup window.
     */
    void quit() {
        quitButton.getScene().getWindow().hide();
    }
}
