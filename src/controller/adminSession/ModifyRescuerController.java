package controller.adminSession;

import controller.Controller;
import model.managers.ManagerContext;
import model.managers.RescuerManager;
import model.persistence.Skill;
import model.services.SceneStackService;
import core.DataReceiver;
import core.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.persistence.Rescuer;
import util.Logger;
import views.AdminSessionView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
/**
 * Controller used to create or modify a rescuer (admin session).
 * Allows editing of identity, credentials, and skills.
 * @author resQ30
 */
public class ModifyRescuerController extends Controller implements DataReceiver<Rescuer> {

    private RescuerManager rescuerManager;

    private boolean isCreating;
    private Rescuer rescuer;
    /**
     * Initializes the controller and gets the RescuerManager instance.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rescuerManager = ManagerContext.get().getRescuerManager();
    }
    /**
     * Receives the rescuer data (null = creation mode).
     * Sets the form fields accordingly.
     *
     * @param data rescuer to modify or null for new creation
     */
    @Override
    public void setData(Rescuer data) {
        Logger.debug("Reçu : " + data);

        isCreating = (data == null);
        rescuer = isCreating ? new Rescuer(0) : data;

        if (!isCreating) {
            nameTextField.setText(data.getName());
            fNameTextField.setText(data.getFName());
            mailTextField.setText(data.getEmail());
            identifiantTextField.setText(data.getIdentifier());
            fullNameTextField.setText(data.getFName() + " " + data.getName());
            passwordTextField.setText(data.getPassword());

            // Réinitialise toutes les cases
            for (CheckBox cb : skillCheckboxes()) cb.setSelected(false);

            // Active celles correspondant aux compétences
            for (String skillStr : data.getSkills()) {
                Skill skill = Skill.valueOf(skillStr);
                switch (skill) {
                    case CE -> CEAdminCheckBox.setSelected(true);
                    case CO -> COAdminCheckBox.setSelected(true);
                    case CP -> CPAdminCheckBox.setSelected(true);
                    case PBC -> PBCAdminCheckBox.setSelected(true);
                    case PBF -> PBFAdminCheckBox.setSelected(true);
                    case PSE1 -> PSE1AdminCheckBox.setSelected(true);
                    case PSE2 -> PSE2AdminCheckBox.setSelected(true);
                    case SSA -> SSAAdminCheckBox.setSelected(true);
                    case VPSP -> VPSPAdminCheckBox.setSelected(true);
                }
            }
        }
    }

    @FXML private GridPane SkillGrid;
    @FXML private CheckBox CEAdminCheckBox, COAdminCheckBox, CPAdminCheckBox, PBCAdminCheckBox;
    @FXML private CheckBox PBFAdminCheckBox, PSE1AdminCheckBox, PSE2AdminCheckBox, SSAAdminCheckBox, VPSPAdminCheckBox;

    @FXML private TextField addAdresseAdminTextField;
    @FXML private TextField mailTextField, nameTextField, passwordTextField;
    @FXML private TextField fNameTextField, identifiantTextField, fullNameTextField;

    @FXML private Button addPdfButton, deleteButton, logOutButton, logoButton, returnButton, validerButton;
    @FXML private ImageView deleteCreationSauvButton;
    @FXML private VBox pdfListContainer;
    
    /**
     * Reset form fields (not yet implemented).
     */
    @FXML
    void resetEntries(MouseEvent event) {
        // TODO
    }
    /**
     * Deletes the current rescuer.
     */
    @FXML
    void deleteRescuer(ActionEvent event) {
        rescuerManager.remove(rescuer.getId());
        SceneStackService.pop();
    }
    /**
     * Go back to the admin home page.
     */
    @FXML
    void goHome(ActionEvent event) {
        SceneStackService.replaceWith(AdminSessionView.HOME);
    }
    /**
     * Log out the current session.
     */
    @FXML
    void logOut(ActionEvent event) {
        Session.logout();
    }
    /**
     * Return to the previous screen.
     */
    @FXML
    void returnToPrevious(ActionEvent event) {
        SceneStackService.pop();
    }
    /**
     * Validate and save the rescuer data.
     * If it's a new rescuer, assign an unused ID.
     */
    @FXML
    void validate(ActionEvent event) {
        if (isCreating) {
            for (int i = 0; i < 1000; i++) {
                if (rescuerManager.get(i) == null) {
                    rescuer = new Rescuer(i);
                    break;
                } else if (i == 999) {
                    Logger.warn("Aucun ID libre pour créer un nouveau sauveteur.");
                    return;
                }
            }
        }

        String name = nameTextField.getText();
        String fName = fNameTextField.getText();
        String email = mailTextField.getText();
        String identifier = identifiantTextField.getText();
        String password = passwordTextField.getText();

        List<Skill> selectedSkills = getSelectedSkills();

        rescuer.setName(name);
        rescuer.setFName(fName);
        rescuer.setEmail(email);
        rescuer.setIdentifier(identifier);
        rescuer.setPassword(password);
        rescuer.setSkills(selectedSkills);

        if (isCreating) {
            rescuerManager.add(rescuer);
            Logger.debug("Sauveteur créé : " + rescuer);
        } else {
            rescuerManager.update(rescuer);
            Logger.debug("Sauveteur mis à jour : " + rescuer);
        }

        SceneStackService.pop();
    }
    /**
     * Get the list of selected skills from checkboxes.
     *
     * @return a list of Skill enums
     */
    private List<Skill> getSelectedSkills() {
        List<Skill> skills = new ArrayList<>();
        if (CEAdminCheckBox.isSelected()) skills.add(Skill.CE);
        if (COAdminCheckBox.isSelected()) skills.add(Skill.CO);
        if (CPAdminCheckBox.isSelected()) skills.add(Skill.CP);
        if (PBCAdminCheckBox.isSelected()) skills.add(Skill.PBC);
        if (PBFAdminCheckBox.isSelected()) skills.add(Skill.PBF);
        if (PSE1AdminCheckBox.isSelected()) skills.add(Skill.PSE1);
        if (PSE2AdminCheckBox.isSelected()) skills.add(Skill.PSE2);
        if (SSAAdminCheckBox.isSelected()) skills.add(Skill.SSA);
        if (VPSPAdminCheckBox.isSelected()) skills.add(Skill.VPSP);
        return skills;
    }
    /**
     * Get all skill-related checkboxes in a list.
     *
     * @return list of CheckBox controls
     */
    private List<CheckBox> skillCheckboxes() {
        return List.of(
                CEAdminCheckBox, COAdminCheckBox, CPAdminCheckBox,
                PBCAdminCheckBox, PBFAdminCheckBox,
                PSE1AdminCheckBox, PSE2AdminCheckBox,
                SSAAdminCheckBox, VPSPAdminCheckBox
        );
    }
}
