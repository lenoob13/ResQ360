package controller.adminSession;

import controller.Controller;
import javafx.concurrent.Worker;
import javafx.scene.chart.*;
import model.graph.algorithme.GraphExaustif;
import model.persistence.*;
import model.services.SceneStackService;
import core.Session;
import model.managers.ManagerContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import model.graph.adaptation.BesoinsAdapter;
import model.graph.algorithme.GraphGloutton;
import model.managers.DPSManager;
import model.managers.RescuerManager;
import model.services.Assigner;
import util.Logger;
import views.AdminSessionView;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static model.services.CSVextracactor.extract;
/**
 * Admin home controller.
 * Displays summary statistics (pie chart, bar chart),
 * and allows executing assignments (greedy/exhaustive) and exporting rescuer data.
 *
 * @author resQ30
 */
public class HomeAdminController extends Controller {

    private DPSManager dpsManager;
    private RescuerManager rescuerManager;
    /**
     * Initializes managers and charts.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dpsManager = ManagerContext.get().getDpsManager();
        this.rescuerManager = ManagerContext.get().getRescuerManager();
        chartLoader();
    }
    /**
     * Loads charts: map view, pie chart for assignment progress,
     * bar chart for rescuer skill distribution.
     */
    private void chartLoader(){
        mapWebView.getEngine().load("https://www.google.com/maps/@39.5,7.5,10z");

        //données du pie chart
        int tot=0;
        for(DPS dps: ManagerContext.get().getDpsManager().getAll()) {
            for(int i : dps.getBesoins().values()){
                tot+=i;
            }
        }
        int nbAssign=0;
        for (Rescuer rescuer: ManagerContext.get().getRescuerManager().getAll()) {
            nbAssign+=rescuer.getAssignments().size();
        }

        double percentCompleted = 0;
        if (nbAssign != 0) {
            percentCompleted = ((double) tot / nbAssign) * 100;
        }
        pieChart.getData().clear();
        pieChart.getData().addAll(
                new PieChart.Data("Complété", percentCompleted),
                new PieChart.Data("Non complété", 100-percentCompleted)
        );

        //données du bar chart
        Map<String, Integer> map = new HashMap<>();

        // Récupérer la hiérarchie (clé = Skill)
        Map<Skill, ?> hierarchie = CompetenceDependancies.getHierarchie();

        for (Skill skill : hierarchie.keySet()) {
            map.put(skill.name(), 0);
        }
        for (Rescuer rescuer: ManagerContext.get().getRescuerManager().getAll()) {
            List<String> comp=rescuer.getSkills();
            for (String skillName : comp) {
                map.put(skillName, map.get(skillName) + 1);
            }
        }


        xAxis.setLabel("Diplômes");
        yAxis.setLabel("Nombre");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Diplômes obtenus");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String competence = entry.getKey();
            Integer nombre = entry.getValue();

            series.getData().add(new XYChart.Data<>(competence, nombre));
        }
        barChart.getData().clear();
        barChart.getData().add(series);
    }

    @FXML
    private ImageView ButtonAction;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private ImageView buttonLogOut;

    @FXML
    private Button exhaustiveAssignmentButton;

    @FXML
    private Button exportCSVButton;

    @FXML
    private Button gererEpreuvesButton;

    @FXML
    private Button gluttonousAssignmentButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button logoButton;

    @FXML
    private WebView mapWebView;

    @FXML
    private PieChart pieChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    /**
     * Executes the exhaustive assignment algorithm and applies the result.
     */
    @FXML
    private void exhaustiveAssignment() {
        Logger.info("Affectation exhaustive lancée...");
        // TODO : Logique pour affectation exhaustive
        List<Rescuer> rescuers = rescuerManager.getAll();
        List<DPS> dpsList = dpsManager.getAll();

        BesoinsAdapter.ResultatAdaptation resultat = BesoinsAdapter.buildExtendedMatrix(rescuers, dpsList);
        int[][] matrice = resultat.matrice();

        GraphExaustif exhaustif = new GraphExaustif(matrice);
        int[][] solution = exhaustif.assign();

        Assigner assigner = new Assigner(rescuerManager);
        assigner.applyAssignments(new BesoinsAdapter.ResultatAdaptation(solution, resultat.correspondanceColonnes()));

        Logger.info("Exhaustive assignment");
        Logger.debug("Liste des sauveteurs : " + rescuerManager.getAll());
    }
    /**
     * Executes the greedy assignment algorithm and applies the result.
     */
    @FXML
    private void gluttonousAssignment() {
        Logger.info("Affectation gloutonne lancée...");
        // TODO : donner la liste de sauveteurs management et dps management dans sauveteurs et dps et donner le recuerManager a assigner
        List<Rescuer> rescuers = rescuerManager.getAll();
        List<DPS> dpsList = dpsManager.getAll();

        BesoinsAdapter.ResultatAdaptation resultat = BesoinsAdapter.buildExtendedMatrix(rescuers, dpsList);
        int[][] matrice = resultat.matrice();

        GraphGloutton glouton = new GraphGloutton(matrice);
        int[][] solution = glouton.assign();

        Assigner assigner = new Assigner(rescuerManager);
        assigner.applyAssignments(new BesoinsAdapter.ResultatAdaptation(solution, resultat.correspondanceColonnes()));

        Logger.info("Glouton assignment");
        Logger.debug("Liste des sauveteurs : " + rescuerManager.getAll());
    }
    /**
     * Exports all rescuer data to a CSV file.
     */
    @FXML
    void exportCSV(ActionEvent event) {
        try {
            extract(rescuerManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Navigates back to the admin home screen.
     */
    @FXML
    private void goHome() {
        SceneStackService.replaceWith(AdminSessionView.HOME);
    }
    /**
     * Logs out the current user.
     */
    @FXML
    private void logOut() {
        Session.logout();
    }
    /**
     * Opens the event and rescuer management page.
     */
    @FXML
    private void manageEvents() {
        SceneStackService.push(AdminSessionView.MANAGE_EVENT_RESCUER);
    }
}
