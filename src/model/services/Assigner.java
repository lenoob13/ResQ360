package model.services;

import model.graph.adaptation.BesoinsAdapter;
import model.managers.RescuerManager;
import model.persistence.Rescuer;
import util.Logger;

import java.util.List;

/**
 * Service class that applies DPS-rescuer assignments based on an assignment matrix.
 *
 * <p>This class uses data from {@link BesoinsAdapter.ResultatAdaptation} to
 * update rescuers with their assigned DPS based on a precomputed matrix.</p>
 *
 * @author ResQ360
 */
public class Assigner {

    private final RescuerManager rescuerManager;

    /**
     * Creates a new {@code Assigner} with the given {@link RescuerManager}.
     *
     * @param rescuerManager the manager used to fetch and update rescuers
     */
    public Assigner(RescuerManager rescuerManager) {
        this.rescuerManager = rescuerManager;
    }

    /**
     * Applies assignments from a matrix to the corresponding rescuers.
     *
     * <p>The matrix indicates which rescuer (row) is assigned to which DPS (column).
     * The mapping of column indices to DPS IDs is provided by the {@code correspondanceColonnes} list.</p>
     *
     * <p>If a rescuer is not found for a row index, a warning is logged and the row is skipped.</p>
     *
     * @param resultatAdaptation object containing the assignment matrix and column-to-DPS mapping
     */
    public void applyAssignments(BesoinsAdapter.ResultatAdaptation resultatAdaptation) {
        int[][] assignmentMatrix = resultatAdaptation.matrice();
        List<BesoinsAdapter.DPSColonneInfo> correspondanceColonnes = resultatAdaptation.correspondanceColonnes();

        for (int i = 0; i < assignmentMatrix.length; i++) {
            Rescuer rescuer = rescuerManager.get(i);
            if (rescuer == null) {
                Logger.warn("No rescuer found with ID " + i);
                continue;
            }
            for (int j = 0; j < assignmentMatrix[i].length; j++) {
                if (assignmentMatrix[i][j] == 1) {
                    int idDPS = correspondanceColonnes.get(j).idDPS();

                    rescuer.addAssignment(idDPS);

                    Logger.info("Assigned DPS ID " + idDPS + " to rescuer " + i);
                }
            }
        }
    }
}
