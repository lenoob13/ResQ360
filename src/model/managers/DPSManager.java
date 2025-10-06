package model.managers;

import model.dao.DAODPS;
import model.persistence.DPS;
import util.Logger;

import java.util.ArrayList;
import java.util.List;
/**
 * Manages a list of DPS (missions) in memory and synchronizes them with the database.
 * 
 * <p>This manager uses a cache to optimize access and delegates persistence operations to {@link DAODPS}.</p>
 * 
 * @author ResQ360
 */
public final class DPSManager {

    private final List<DPS> cache;
    private final DAODPS dao;
    /**
     * Creates a DPSManager and loads all DPS from the database into memory.
     */
    DPSManager() {
        this.cache = new ArrayList<>();
        this.dao = new DAODPS();
        this.cache.addAll(dao.getAll());
    }
    /**
     * Gets a DPS by its ID.
     *
     * @param id the DPS ID
     * @return the corresponding DPS or null if not found
     */
    public DPS getDPS(int id) {
        for (DPS dps : this.cache) {
            if (dps.getId() == id) {
                return dps;
            }
        }
        return null;
    }
    /**
     * Adds a new DPS if it doesn't already exist.
     *
     * @param dps the DPS to add
     */
    public void addDPS(DPS dps) {
        if (getDPS(dps.getId()) != null) {
            Logger.warn("A DPS with this ID already exists.");
            return;
        }
        if (dao.insert(dps)) {
            this.cache.add(dps);
        } else {
            Logger.error("Failed to insert DPS into database.");
        }
    }
    /**
     * Updates an existing DPS both in the database and in the cache.
     *
     * @param updatedDps the DPS with updated data
     */
    public void updateDPS(DPS updatedDps) {
        DPS existing = getDPS(updatedDps.getId());
        if (existing == null) {
            Logger.warn("No existing DPS with ID " + updatedDps.getId() + " found.");
            return;
        }

        if (dao.update(updatedDps)) {
            int index = cache.indexOf(existing);
            cache.set(index, updatedDps);
            Logger.info("DPS updated in DB and cache.");
        } else {
            Logger.error("Failed to update DPS in database.");
        }
    }
     /**
     * Removes a DPS by its ID from both cache and database.
     *
     * @param id the ID of the DPS to remove
     */
    public void removeDPS(int id) {
        DPS dpsToRemove = getDPS(id);
        if (dpsToRemove == null) {
            Logger.warn("No DPS found with ID " + id + ".");
            return;
        }

        if (dao.delete(id)) {
            this.cache.remove(dpsToRemove);
            Logger.info("DPS with ID " + id + " has been removed.");
        } else {
            Logger.error("Failed to delete DPS from database.");
        }
    }
    /**
     * Returns a copy of all DPS in the cache.
     *
     * @return the list of all DPS
     */
    public List<DPS> getAll() {
        return new ArrayList<>(cache); // copie défensive
    }
    /**
     * Replaces all current DPS with a new list.
     *
     * @param newCache the new DPS list to set
     */
    public void setAll(List<DPS> newCache) {
        for (DPS dps : new ArrayList<>(cache)) {
            removeDPS(dps.getId());
        }
        for (DPS dps : newCache) {
            addDPS(dps);
        }
    }
}
