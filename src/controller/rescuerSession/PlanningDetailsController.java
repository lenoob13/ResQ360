package controller.rescuerSession;

import controller.Controller;
import core.DataReceiver;
import core.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import model.managers.ManagerContext;
import model.persistence.DPS;
import model.services.SceneStackService;
import views.RescuerSessionView;

import java.net.URL;
import java.util.ResourceBundle;
/**
 * Controller for the DPS details view in the rescuer session.
 * Displays information about a specific DPS including title, note, date, and location on a map.
 * @author resQ30
 */
public class PlanningDetailsController extends Controller implements DataReceiver<DPS> {
    /**
     * Loads data into the view from a DPS object.
     *
     * @param dps the DPS to display
     */
    @Override
    public void setData(DPS dps){
        String titre = ManagerContext.get().getSportManager().getSport(dps.getIdSport()).getName();
        String note = dps.getNote();
        String date = ManagerContext.get().getDayManager().getDayById(dps.getIdJournee()).toString();
        float lat = ManagerContext.get().getSiteManager().get(dps.getIdSite()).getLatitude();
        float lon = ManagerContext.get().getSiteManager().get(dps.getIdSite()).getLongitude();
        setInfo(titre, note, date, lat, lon);
    }

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

    @FXML
    void returnToPrevious(ActionEvent event) {
        SceneStackService.pop();
    }

    @FXML
    void goHome(ActionEvent event) {
        SceneStackService.replaceWith(RescuerSessionView.ACCUEIL);
    }

    @FXML
    void logOut(ActionEvent event) {
        Session.logout();
    }
    /**
     * Updates the UI components with given DPS info.
     *
     * @param title the sport name
     * @param note  the optional note attached to the DPS
     * @param date  the date and time of the DPS
     * @param lat   latitude of the DPS location
     * @param lon   longitude of the DPS location
     */
    public void setInfo(String titre,String note,String date,float lat,float lon) {
        titreLabel.setText(titre);
        dateHoraireLabel.setText(date);
        coordLabel.setText(lat+","+lon);
        noteTexte.setText(note);
        String url =String.format("https://www.google.com/maps/@%f,%f,15z", lat, lon);
        mapWebView.getEngine().load(url);
    }
}
