package controller;

import core.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.services.SceneStackService;
import util.Logger;
import views.RescuerSessionView;

import java.net.URL;
import java.util.ResourceBundle;
/**
 * Controller for the login (Accueil) screen.
 * 
 * Handles user input for authentication and provides default credentials for testing.
 * @author resQ360
 */
public class AccueilController extends Controller {
    /**
     * Called after the FXML file has been loaded.
     * Sets default credentials in the login form for testing purposes.
     *
     * @param location  the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idTxt.setText("admin");
        mdpTxt.setText("admin");
    }

    @FXML
    private TextField idTxt;

    @FXML
    private PasswordField mdpTxt;

    @FXML
    private Button connexionButton;

    @FXML
    private Button inscriptionButton;

    @FXML
    private ImageView logo;

    /**
     * Handles the login button click.
     * Retrieves input values and attempts to log in via the Session class.
     */
    @FXML
    private void connexion() {
        String identifiant = idTxt.getText();
        String motDePasse = mdpTxt.getText();

        if (identifiant == null || identifiant.isEmpty())
            identifiant = "Unknown";

        if (motDePasse == null || motDePasse.isEmpty())
            motDePasse = "Unknown";

        Logger.info("Tentative de connexion avec :");
        Logger.info("Identifiant : " + identifiant);
        Logger.info("Mot de passe : " + motDePasse);

        Session.login(identifiant, motDePasse);
    }
}
