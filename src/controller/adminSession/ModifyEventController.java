package controller.adminSession;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import model.managers.DPSManager;
import model.managers.ManagerContext;
import model.managers.RescuerManager;
import model.managers.SiteManager;
import model.persistence.*;
import model.services.SceneStackService;
import core.DataReceiver;
import core.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import util.Logger;
import views.AdminSessionView;

import java.net.URL;
import java.util.*;
/**
 * Controller used to create or edit a DPS event.
 * Allows selecting site location, schedule, required skills, and assigned rescuers.
 * @author resQ30
 */

public class ModifyEventController extends Controller implements DataReceiver<DPS> {

    private DPSManager dpsManager;
    private RescuerManager rescuerManager;
    private SiteManager siteManager;

    private boolean isCreating;
    private DPS dps;

    private final ObservableList<Rescuer> assignedRescuers = FXCollections.observableArrayList();

    @FXML private TextField EpreuveTextField;
    @FXML private CheckBox CEEventCheckBox;
    @FXML private CheckBox COEventCheckBox;
    @FXML private CheckBox CPEventCheckBox;
    @FXML private CheckBox PBCEventCheckBox;
    @FXML private CheckBox PBFEventCheckBox;
    @FXML private CheckBox PSE1EventCheckBox;
    @FXML private CheckBox PSE2EventCheckBox;
    @FXML private CheckBox SSAEventCheckBox;
    @FXML private CheckBox VPSPEventCheckBox;
    @FXML private ComboBox<Rescuer> comboBoxAddSauv;
    @FXML private TextField dateEventTextField;
    @FXML private Button deleteSauvButton;
    @FXML private TextField endDateEventTextField;
    @FXML private TextArea inFoImpEventTextField;
    @FXML private TextField latitudeTextField;
    @FXML private Button logOutButton;
    @FXML private Button logoButton;
    @FXML private TextField longitudeTextField;
    @FXML private Button returnButton;
    @FXML private ListView<Rescuer> sauvListContainer;
    @FXML private TextField startDateEventTextField;
    @FXML private Button validEventPageButton;
    @FXML private Label skillsSummaryLabel;
    /**
     * Initializes UI elements and event listeners for rescuer assignment and skill summary.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dpsManager = ManagerContext.get().getDpsManager();
        rescuerManager = ManagerContext.get().getRescuerManager();
        siteManager = ManagerContext.get().getSiteManager();

        updateComboBoxRescuers();

        comboBoxAddSauv.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Rescuer r, boolean empty) {
                super.updateItem(r, empty);
                setText((empty || r == null) ? null : r.getIdentifier() + " (" + String.join(", ", r.getSkills()) + ")");
            }
        });
        comboBoxAddSauv.setButtonCell(comboBoxAddSauv.getCellFactory().call(null));

        sauvListContainer.setItems(assignedRescuers);
        sauvListContainer.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Rescuer r, boolean empty) {
                super.updateItem(r, empty);
                setText((empty || r == null) ? null : r.getIdentifier() + " (" + String.join(", ", r.getSkills()) + ")");
            }
        });

        comboBoxAddSauv.setOnAction(e -> addSelectedRescuer());

        assignedRescuers.addListener((javafx.collections.ListChangeListener<Rescuer>) change -> {
            try {
                updateSkillsSummary();
                updateComboBoxRescuers();
            } catch (Exception e) {
                Logger.debug("Erreur lors de la mise à jour: " + e.getMessage());
            }
        });

        updateSkillsSummary();
    }

    /**
     * Updates the ComboBox with rescuers who are not yet assigned to the event.
     */
    private void updateComboBoxRescuers() {
        try {
            List<Rescuer> availableRescuers = new ArrayList<>();
            List<Rescuer> allRescuers = rescuerManager.getAll();

            if (allRescuers != null) {
                for (Rescuer rescuer : allRescuers) {
                    if (rescuer != null && !assignedRescuers.contains(rescuer)) {
                        availableRescuers.add(rescuer);
                    }
                }
            }

            javafx.application.Platform.runLater(() -> {
                try {
                    comboBoxAddSauv.getItems().setAll(availableRescuers);
                } catch (Exception e) {
                    Logger.debug("Erreur lors de la mise à jour de la ComboBox: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            Logger.debug("Erreur dans updateComboBoxRescuers: " + e.getMessage());
        }
    }

    /**
     * Updates the skill summary label based on currently assigned rescuers.
     */
    private void updateSkillsSummary() {
        Map<Skill, Integer> skillCounts = new HashMap<>();
        for (Skill skill : Skill.values()) skillCounts.put(skill, 0);

        for (Rescuer rescuer : assignedRescuers) {
            for (String skillName : rescuer.getSkills()) {
                try {
                    Skill skill = Skill.valueOf(skillName);
                    skillCounts.put(skill, skillCounts.get(skill) + 1);
                } catch (IllegalArgumentException e) {
                    Logger.debug("Compétence inconnue : " + skillName);
                }
            }
        }

        StringBuilder summary = new StringBuilder("Compétences disponibles: ");
        boolean first = true;
        for (Map.Entry<Skill, Integer> entry : skillCounts.entrySet()) {
            if (entry.getValue() > 0) {
                if (!first) summary.append(", ");
                summary.append(entry.getKey().name()).append(": ").append(entry.getValue());
                first = false;
            }
        }
        if (first) summary.append("Aucune");

        if (skillsSummaryLabel != null) skillsSummaryLabel.setText(summary.toString());
    }
    /**
     * Receives a DPS to edit, or null to start creating a new one.
     * Fills all form fields accordingly.
     *
     * @param data the DPS object to edit, or null to create a new one
     */
    @Override
    public void setData(DPS data) {
        Logger.debug("Reçu : " + data);
        dps = data;

        if (data == null) {
            isCreating = true;
            assignedRescuers.clear();
            resetSkillCheckboxes();
            updateSkillsSummary();
            updateComboBoxRescuers();
            // Nettoyer champs
            EpreuveTextField.clear();
            dateEventTextField.clear();
            startDateEventTextField.clear();
            endDateEventTextField.clear();
            latitudeTextField.clear();
            longitudeTextField.clear();
            inFoImpEventTextField.clear();
            return;
        }
        isCreating = false;

        EpreuveTextField.setText(dps.getIdSport());

        Day day = ManagerContext.get().getDayManager().getDayById(dps.getIdJournee());
        if (day != null) {
            dateEventTextField.setText(String.format("%02d/%02d/%04d", day.getDay(), day.getMonth(), day.getYear()));

            int[] start = day.getStartTime();
            startDateEventTextField.setText((start != null && start.length == 2) ? String.format("%02d:%02d", start[0], start[1]) : "");

            int[] end = day.getEndTime();
            endDateEventTextField.setText((end != null && end.length == 2) ? String.format("%02d:%02d", end[0], end[1]) : "");
        } else {
            dateEventTextField.clear();
            startDateEventTextField.clear();
            endDateEventTextField.clear();
        }

        Site site = siteManager.get(dps.getIdSite());
        if (site != null) {
            latitudeTextField.setText(String.valueOf(site.getLatitude()));
            longitudeTextField.setText(String.valueOf(site.getLongitude()));
        } else {
            latitudeTextField.clear();
            longitudeTextField.clear();
        }

        inFoImpEventTextField.setText(dps.getNote() != null ? dps.getNote() : "");

        resetSkillCheckboxes();
        setNeedCheckboxes(dps.getBesoins());

        assignedRescuers.clear();
        List<Rescuer> allRescuers = rescuerManager.getAll();
        if (allRescuers != null) {
            for (Rescuer rescuer : allRescuers) {
                if (rescuer != null && rescuer.getAssignments().contains(dps.getId())) {
                    assignedRescuers.add(rescuer);
                }
            }
        }
    }
    /**
     * Unchecks all skill CheckBoxes.
     */
    private void resetSkillCheckboxes() {
        CEEventCheckBox.setSelected(false);
        COEventCheckBox.setSelected(false);
        CPEventCheckBox.setSelected(false);
        PBCEventCheckBox.setSelected(false);
        PBFEventCheckBox.setSelected(false);
        PSE1EventCheckBox.setSelected(false);
        PSE2EventCheckBox.setSelected(false);
        SSAEventCheckBox.setSelected(false);
        VPSPEventCheckBox.setSelected(false);
    }
    /**
     * Selects skill CheckBoxes based on the provided skill need map.
     *
     * @param needs a map of skill IDs to required counts
     */
    private void setNeedCheckboxes(Map<Integer, Integer> needs) {
        if (needs == null) return;

        for (Integer key : needs.keySet()) {
            Skill skill = Skill.fromId(key);
            if (skill == null) continue;
            switch (skill) {
                case CE -> CEEventCheckBox.setSelected(true);
                case CO -> COEventCheckBox.setSelected(true);
                case CP -> CPEventCheckBox.setSelected(true);
                case PBC -> PBCEventCheckBox.setSelected(true);
                case PBF -> PBFEventCheckBox.setSelected(true);
                case PSE1 -> PSE1EventCheckBox.setSelected(true);
                case PSE2 -> PSE2EventCheckBox.setSelected(true);
                case SSA -> SSAEventCheckBox.setSelected(true);
                case VPSP -> VPSPEventCheckBox.setSelected(true);
            }
        }
    }
    /**
     * Returns the list of selected skills based on the UI CheckBoxes.
     *
     * @return list of selected skills
     */
    private List<Skill> getSelectedSkills() {
        List<Skill> selectedSkills = new ArrayList<>();
        if (CEEventCheckBox.isSelected()) selectedSkills.add(Skill.CE);
        if (COEventCheckBox.isSelected()) selectedSkills.add(Skill.CO);
        if (CPEventCheckBox.isSelected()) selectedSkills.add(Skill.CP);
        if (PBCEventCheckBox.isSelected()) selectedSkills.add(Skill.PBC);
        if (PBFEventCheckBox.isSelected()) selectedSkills.add(Skill.PBF);
        if (PSE1EventCheckBox.isSelected()) selectedSkills.add(Skill.PSE1);
        if (PSE2EventCheckBox.isSelected()) selectedSkills.add(Skill.PSE2);
        if (SSAEventCheckBox.isSelected()) selectedSkills.add(Skill.SSA);
        if (VPSPEventCheckBox.isSelected()) selectedSkills.add(Skill.VPSP);
        return selectedSkills;
    }
    /**
     * Adds the currently selected rescuer from the ComboBox to the assigned list.
     */
    private void addSelectedRescuer() {
        Rescuer selected = comboBoxAddSauv.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (!assignedRescuers.contains(selected)) {
            assignedRescuers.add(selected);
            Logger.debug("Sauveteur ajouté : " + selected.getIdentifier());
            javafx.application.Platform.runLater(() -> comboBoxAddSauv.getSelectionModel().clearSelection());
        }
    }

    @FXML
    void addSauv(ActionEvent event) {
        // Cette méthode semble inutilisée car tu ajoutes avec la ComboBox.
        addSelectedRescuer();
    }
    /**
     * Removes the selected rescuer from the assigned list.
     */
    @FXML
    void deleteSauv(ActionEvent event) {
        Rescuer selected = sauvListContainer.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Logger.debug("Aucun sauveteur sélectionné pour suppression.");
            return;
        }

        int selectedIndex = sauvListContainer.getSelectionModel().getSelectedIndex();
        assignedRescuers.remove(selected);
        Logger.debug("Sauveteur supprimé : " + selected.getIdentifier());

        javafx.application.Platform.runLater(() -> {
            if (!assignedRescuers.isEmpty()) {
                int newIndex = Math.min(selectedIndex, assignedRescuers.size() - 1);
                if (newIndex >= 0) sauvListContainer.getSelectionModel().select(newIndex);
            } else {
                sauvListContainer.getSelectionModel().clearSelection();
            }
        });
    }
    /**
     * Clears all assigned rescuers from the list.
     */
    @FXML
    void clearAllRescuers(ActionEvent event) {
        sauvListContainer.getSelectionModel().clearSelection();
        assignedRescuers.clear();
        Logger.debug("Tous les sauveteurs ont été supprimés.");
    }

    @FXML
    void goHome(ActionEvent event) {
        SceneStackService.replaceWith(AdminSessionView.HOME);
    }

    @FXML
    void logOut(ActionEvent event) {
        Session.logout();
    }

    @FXML
    void returnToPrevious(ActionEvent event) {
        SceneStackService.pop();
    }
     /**
     * Validates the form fields and either creates or updates the DPS event.
     * Handles parsing of dates, times, coordinates, and assignment of rescuers.
     */
    @FXML
    void validate(ActionEvent event) {
        Integer newDpsId = null;
        String newSiteId = null;
        Integer newDayId;

        if (isCreating) {
            for (int i = 0; i < 200; i++) {
                if (dpsManager.getDPS(i) == null) {
                    newDpsId = i;
                    break;
                }
            }
            if (newDpsId == null) {
                Logger.warn("Impossible de trouver un id DPS libre");
                return;
            }

            // Chercher ID libre pour Site uniquement en création
            for (int i = 0; i < 200; i++) {
                Site siteTest = siteManager.get(String.valueOf(i));
                if (siteTest == null) {
                    newSiteId = String.valueOf(i);
                    break;
                }
            }
            if (newSiteId == null) {
                Logger.warn("Impossible de trouver un id Site libre");
                return;
            }
        }

// === Ici : récupérer et parser latitude et longitude ===
        String latStr = latitudeTextField.getText().trim();
        String lonStr = longitudeTextField.getText().trim();

        float latitude, longitude;
        try {
            latitude = Float.parseFloat(latStr);
            longitude = Float.parseFloat(lonStr);
        } catch (NumberFormatException e) {
            Logger.debug("Latitude ou longitude invalide (doivent être des nombres) : " + latStr + " / " + lonStr);
            return; // tu peux aussi afficher un message d'erreur à l'utilisateur
        }

// === Ensuite ===
// Création ou modification du site, avec les coordonnées récupérées
        if (isCreating) {
            Site newSite = new Site(newSiteId, "", latitude, longitude);
            siteManager.add(newSite);
            Logger.debug("Site créé avec id " + newSiteId + " et coordonnées " + latitude + ", " + longitude);
        } else {
            Site existingSite = siteManager.get(dps.getIdSite());
            if (existingSite != null) {
                existingSite.setLatitude(latitude);
                existingSite.setLongitude(longitude);
                siteManager.changeSite(existingSite.getCode(),existingSite.getName(),existingSite.getLatitude(),existingSite.getLongitude());
                Logger.debug("Site modifié id " + existingSite.getCode() + " avec nouvelles coordonnées " + latitude + ", " + longitude);
            } else {
                Site newSite = new Site(dps.getIdSite(), "", latitude, longitude);
                siteManager.add(newSite);
                Logger.debug("Site introuvable, création site id " + newSite.getCode());
            }
        }


        // Lecture des champs obligatoires
        String idSport = EpreuveTextField.getText().trim();
        String noteDps = inFoImpEventTextField.getText().trim();

        if (idSport.isEmpty()) {
            Logger.debug("Le champ idSport est vide");
            return;
        }

        // Parsing date JJ/MM/AAAA
        String dateStr = dateEventTextField.getText().trim();
        String[] dateParts = dateStr.split("/");
        if (dateParts.length != 3) {
            Logger.debug("Date invalide (format attendu JJ/MM/AAAA) : " + dateStr);
            return;
        }
        int day, month, year;
        try {
            day = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]);
            year = Integer.parseInt(dateParts[2]);
        } catch (NumberFormatException e) {
            Logger.debug("Date invalide (non numérique) : " + dateStr);
            return;
        }

        // Parsing heure début hh:mm
        String startStr = startDateEventTextField.getText().trim();
        String[] startParts = startStr.split(":");
        if (startParts.length != 2) {
            Logger.debug("Heure de début invalide (format attendu hh:mm) : " + startStr);
            return;
        }
        int[] startTime = new int[2];
        try {
            startTime[0] = Integer.parseInt(startParts[0]);
            startTime[1] = Integer.parseInt(startParts[1]);
        } catch (NumberFormatException e) {
            Logger.debug("Heure de début invalide (non numérique) : " + startStr);
            return;
        }

        // Parsing heure fin hh:mm
        String endStr = endDateEventTextField.getText().trim();
        String[] endParts = endStr.split(":");
        if (endParts.length != 2) {
            Logger.debug("Heure de fin invalide (format attendu hh:mm) : " + endStr);
            return;
        }
        int[] endTime = new int[2];
        try {
            endTime[0] = Integer.parseInt(endParts[0]);
            endTime[1] = Integer.parseInt(endParts[1]);
        } catch (NumberFormatException e) {
            Logger.debug("Heure de fin invalide (non numérique) : " + endStr);
            return;
        }

        // Gestion ID Day
        if (isCreating) {
            newDayId = newDpsId; // choix arbitraire : dayId = dpsId
        } else {
            newDayId = dps.getIdJournee();
        }

        // Création/modification Day
        Day dayObj = new Day(newDayId, day, month, year, startTime, endTime);
        ManagerContext.get().getDayManager().addDay(dayObj);

        // Création ou mise à jour DPS
        if (isCreating) {
            dps = new DPS(newDpsId, newDayId, newSiteId);
            dps.setIdSport(idSport);
            dps.setNote(noteDps);
            dpsManager.addDPS(dps);
            Logger.debug("DPS créé avec id " + dps.getId());
        } else {
            dps.setIdSport(idSport);
            dps.setIdJournee(newDayId);
            // Ne pas changer siteId en modification, conserver l'existant
            dps.setNote(noteDps);
            dpsManager.updateDPS(dps);
            Logger.debug("DPS modifié avec id " + dps.getId());
        }

        // Mise à jour des besoins compétences
        Map<Integer, Integer> besoins = new HashMap<>();
        for (Skill s : getSelectedSkills()) {
            besoins.put(s.getIndice(), 1);
        }
        dps.setBesoins(besoins);

        // Mise à jour assignments sauveteurs (ajout seulement)
        for (Rescuer rescuer : assignedRescuers) {
            if (!rescuer.getAssignments().contains(dps.getId())) {
                rescuer.addAssignment(dps.getId());
                rescuerManager.update(rescuer);
            }
        }

        Logger.debug("Validation terminée avec " + assignedRescuers.size() + " sauveteurs.");
        SceneStackService.pop();
    }
    /**
     * Returns the list of currently assigned rescuers.
     *
     * @return list of rescuers
     */
    public List<Rescuer> getAssignedRescuers() {
        return new ArrayList<>(assignedRescuers);
    }
    /**
     * Returns a map showing how many rescuers have each skill.
     *
     * @return map of skill counts
     */
    public Map<Skill, Integer> getSkillsSummary() {
        Map<Skill, Integer> skillCounts = new HashMap<>();
        for (Skill skill : Skill.values()) skillCounts.put(skill, 0);

        for (Rescuer rescuer : assignedRescuers) {
            for (String skillName : rescuer.getSkills()) {
                try {
                    Skill skill = Skill.valueOf(skillName);
                    skillCounts.put(skill, skillCounts.get(skill) + 1);
                } catch (IllegalArgumentException e) {
                    Logger.debug("Compétence inconnue : " + skillName);
                }
            }
        }
        return skillCounts;
    }
}
