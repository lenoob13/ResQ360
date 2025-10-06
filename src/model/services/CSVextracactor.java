package model.services;

import model.managers.RescuerManager;
import model.persistence.Rescuer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.stage.FileChooser;
/**
 * Utility class to export a list of rescuers into a CSV file.
 *
 * <p>The user is prompted to choose a save location using a file dialog.
 * All rescuer data is extracted from a {@link RescuerManager} and written
 * to the chosen file in CSV format.</p>
 *
 * <p>Each line corresponds to a {@link Rescuer} and uses the format defined by {@code csvString()}.</p>
 *
 * @author ResQ360
 */
public class CSVextracactor {
    /**
     * Opens a save dialog and writes all rescuers from the manager to a CSV file.
     *
     * @param rm the {@link RescuerManager} containing all rescuers
     * @throws IOException if the file cannot be written
     */
    public static void extract(RescuerManager rm) throws IOException {
        FileChooser chooser =new FileChooser();
        File selected =chooser.showSaveDialog(null);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(selected))) {
            // CSV header
            bw.write("id ,identifier ,mail ,skills ,password ,is admin ,assignments ,Last Name , First Name\n");
            // CSV content
            for (Rescuer r : rm.getAll()) {
                bw.write(r.csvString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
