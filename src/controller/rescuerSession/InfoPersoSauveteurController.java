package controller.rescuerSession;

import controller.Controller;
import core.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.managers.ManagerContext;
import model.services.SceneStackService;
import util.Logger;
import views.RescuerSessionView;

import java.net.URL;
import java.util.ResourceBundle;
/**
 * Controller for the rescuer personal information screen.
 * Allows the rescuer to view and update their information, and manage diploma checkboxes.
 * @author resQ30
 */
public class InfoPersoSauveteurController extends Controller {
    /**
     * Initializes the view with current user information.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Session.getCurrentUser() == null) return;
        emailSauvField.setText(Session.getCurrentUser().getEmail());
        idSauvField.setText(Session.getCurrentUser().getIdentifier());
        passwordSauvField.setText(Session.getCurrentUser().getPassword());
        nomSauvField.setText(Session.getCurrentUser().getName());
        prenomSauvField.setText(Session.getCurrentUser().getName());
    }

    @FXML
    private ImageView ButtonAction;

    @FXML
    private CheckBox CESauvCheckBox;

    @FXML
    private CheckBox COSauvCheckBox;

    @FXML
    private CheckBox CPSauvCheckBox;

    @FXML
    private CheckBox PBCSauvCheckBox;

    @FXML
    private CheckBox PBFSauvCheckBox;

    @FXML
    private CheckBox PSE1SauvCheckBox;

    @FXML
    private CheckBox PSE2SauvCheckBox;

    @FXML
    private CheckBox SSASauvCheckBox;

    @FXML
    private CheckBox VPSPSauvCheckBox;

    @FXML
    private ImageView buttonLogOut;

    @FXML
    private Button buttonPersonalInfoValidate;

    @FXML
    private ImageView buttonReturn;

    @FXML
    private TextField emailSauvField;

    @FXML
    private TextField idSauvField;

    @FXML
    private Button logOutButton;

    @FXML
    private Button logoButton;

    @FXML
    private TextField nomSauvField;

    @FXML
    private PasswordField passwordSauvField;

    @FXML
    private TextField prenomSauvField;

    @FXML
    private Button returnButton;
    /**
     * Opens diploma upload page if CE checkbox is selected.
     */
    @FXML
    void CESauv(ActionEvent event) {
        Logger.info("Action: CESauv checkbox toggled to " + CESauvCheckBox.isSelected());
        if (CESauvCheckBox.isSelected()) SceneStackService.push(RescuerSessionView.DEPOSER_DIPLOME);
    }

    @FXML
    void COSauv(ActionEvent event) {
        Logger.info("Action: COSauv checkbox toggled to " + COSauvCheckBox.isSelected());
        if (COSauvCheckBox.isSelected()) SceneStackService.push(RescuerSessionView.DEPOSER_DIPLOME);
    }

    @FXML
    void CPSauv(ActionEvent event) {
        Logger.info("Action: CPSauv checkbox toggled to " + CPSauvCheckBox.isSelected());
        if (CPSauvCheckBox.isSelected()) SceneStackService.push(RescuerSessionView.DEPOSER_DIPLOME);
    }

    @FXML
    void PBCSauv(ActionEvent event) {
        Logger.info("Action: PBCSauv checkbox toggled to " + PBCSauvCheckBox.isSelected());
        if (PBCSauvCheckBox.isSelected()) SceneStackService.push(RescuerSessionView.DEPOSER_DIPLOME);
    }

    @FXML
    void PBFSauv(ActionEvent event) {
        Logger.info("Action: PBFSauv checkbox toggled to " + PBFSauvCheckBox.isSelected());
        if (PBFSauvCheckBox.isSelected()) SceneStackService.push(RescuerSessionView.DEPOSER_DIPLOME);
    }

    @FXML
    void PSE1Sauv(ActionEvent event) {
        Logger.info("Action: PSE1Sauv checkbox toggled to " + PSE1SauvCheckBox.isSelected());
        if (PSE1SauvCheckBox.isSelected()) SceneStackService.push(RescuerSessionView.DEPOSER_DIPLOME);
    }

    @FXML
    void PSE2Sauv(ActionEvent event) {
        Logger.info("Action: PSE2Sauv checkbox toggled to " + PSE2SauvCheckBox.isSelected());
        if (PSE2SauvCheckBox.isSelected()) SceneStackService.push(RescuerSessionView.DEPOSER_DIPLOME);
    }

    @FXML
    void SSASauv(ActionEvent event) {
        Logger.info("Action: SSASauv checkbox toggled to " + SSASauvCheckBox.isSelected());
        if (SSASauvCheckBox.isSelected()) SceneStackService.push(RescuerSessionView.DEPOSER_DIPLOME);
    }

    @FXML
    void VPSPSauv(ActionEvent event) {
        Logger.info("Action: VPSPSauv checkbox toggled to " + VPSPSauvCheckBox.isSelected());
        if (VPSPSauvCheckBox.isSelected()) SceneStackService.push(RescuerSessionView.DEPOSER_DIPLOME);
    }

    @FXML
    void emailSauv(ActionEvent event) {
        Logger.info("Action: emailSauv text changed to '" + emailSauvField.getText() + "'");
    }

    @FXML
    void idSauv(ActionEvent event) {
        Logger.info("Action: idSauv text changed to '" + idSauvField.getText() + "'");
    }

    @FXML
    void nomSauv(ActionEvent event) {
        Logger.info("Action: nomSauv text changed to '" + nomSauvField.getText() + "'");
    }

    @FXML
    void passwordSauv(ActionEvent event) {
        Logger.info("Action: passwordSauv text changed (hidden for security)");
    }

    @FXML
    void prenomSauv(ActionEvent event) {
        Logger.info("Action: prenomSauv text changed to '" + prenomSauvField.getText() + "'");
    }
     /**
     * Saves the updated personal info to the session and database.
     */
    @FXML
    void validatePersonalInfo(ActionEvent event) {
        Logger.info("Action: validatePersonalInfo clicked");

        if (emailSauvField.getText().isEmpty()){
            Session.getCurrentUser().setEmail(emailSauvField.getText());
        }
        if (idSauvField.getText().isEmpty()){
            Session.getCurrentUser().setIdentifier(idSauvField.getText());
        }
        if (passwordSauvField.getText().isEmpty()){
            Session.getCurrentUser().setPassword(passwordSauvField.getText());
        }
        if (nomSauvField.getText().isEmpty()){
            Session.getCurrentUser().setName(nomSauvField.getText());
        }
        if (prenomSauvField.getText().isEmpty()){
            Session.getCurrentUser().setFName(prenomSauvField.getText());
        }
        ManagerContext.get().getRescuerManager().update(Session.getCurrentUser());
        SceneStackService.pop();
    }
    /**
     * Returns to the previous screen.
     */
    @FXML
    void returnToPrevious(ActionEvent event) {
        SceneStackService.pop();
    }
    /**
     * Navigates to the home screen.
     */
    @FXML
    void goHome(ActionEvent event) {
        Logger.info("Action: goHome clicked, replacing with ACCUEIL view");
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
