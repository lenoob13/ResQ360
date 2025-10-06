package model.services;

import model.managers.RescuerManager;
import model.persistence.Rescuer;

import java.util.List;
/**
 * Service class that handles assigning and removing rescuer-to-DPS relationships.
 *
 * <p>This service acts as a layer between the application logic and the {@link RescuerManager},
 * allowing centralized control over assignment operations.</p>
 *
 * <p>Each assignment links a rescuer (by ID) to a DPS (by ID).</p>
 *
 * @author ResQ360
 */
public final class AssignmentService {

    private RescuerManager rescuerManager;

    /**
     * Creates a new {@code AssignmentService} with the given rescuer manager.
     *
     * @param rescuerManager the manager used to access and update rescuers
     */
    public AssignmentService(RescuerManager rescuerManager) {
        this.rescuerManager = rescuerManager;
    }

    /**
     * Assigns a rescuer to a specific DPS by adding the DPS ID to their assignment list.
     *
     * @param rescuerId the ID of the rescuer
     * @param dpsId     the ID of the DPS
     */
    public void assignRescuer(int rescuerId, int dpsId) {
        Rescuer rescuer = rescuerManager.get(rescuerId);
        if (rescuer != null) {
            rescuer.addAssignment(dpsId);
            System.out.println("Rescuer " + rescuerId + " assigned to DPS " + dpsId);
        } else {
            System.out.println("Rescuer not found.");
        }
    }

    /**
     * Removes a specific DPS assignment from the rescuer's list.
     *
     * @param rescuerId the ID of the rescuer
     * @param dpsId     the ID of the DPS to remove
     */
    public void removeAssignment(int rescuerId, int dpsId) {
        Rescuer rescuer = rescuerManager.get(rescuerId);
        if (rescuer != null) {
            rescuer.removeAssignment(dpsId);
            System.out.println("Assignment removed for rescuer " + rescuerId);
        } else {
            System.out.println("Rescuer not found.");
        }
    }

    /**
     * Retrieves the list of DPS assignments for a given rescuer.
     *
     * @param rescuerId the ID of the rescuer
     * @return a list of assigned DPS IDs, or {@code null} if the rescuer does not exist
     */
    public List<Integer> getAssignments(int rescuerId) {
        Rescuer rescuer = rescuerManager.get(rescuerId);
        if (rescuer != null) {
            return rescuer.getAssignments();
        }
        System.out.println("Rescuer not found.");
        return null;
    }
}
