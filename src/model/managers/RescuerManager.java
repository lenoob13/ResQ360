package model.managers;

import model.dao.DAORescuer;
import model.persistence.Rescuer;
import util.Logger;

import java.util.ArrayList;
import java.util.List;
/**
 * Manages all rescuer entities: loading from the DAO, caching, and synchronizing updates.
 * <p>
 * Provides CRUD operations (create, read, update, delete) and handles ID uniqueness.
 * </p>
 *
 * @author ResQ360
 */
public final class RescuerManager {

    private final List<Rescuer> cache;
    private final DAORescuer dao;

    /**
     * Constructor that initializes the DAO and loads all rescuers into memory.
     */
    RescuerManager() {
        this.cache = new ArrayList<>();
        this.dao = new DAORescuer();
        this.cache.addAll(dao.getAll());
    }

    /**
     * Returns the rescuer with the given ID.
     *
     * @param id the rescuer ID
     * @return the matching {@link Rescuer} or null if not found
     */
    public Rescuer get(int id) {
        for (Rescuer rescuer : this.cache) {
            if (rescuer.getId() == id) {
                return rescuer;
            }
        }
        Logger.debug("No rescuer found with ID: " + id);
        return null;
    }

    /**
     * Returns the rescuer with the given identifier (username).
     *
     * @param identifier the rescuer's login/identifier
     * @return the matching {@link Rescuer} or null if not found
     */
    public Rescuer get(String identifier) {
        for (Rescuer rescuer : this.cache) {
            if (rescuer.getIdentifier().equals(identifier)) {
                return rescuer;
            }
        }
        Logger.debug("No rescuer found with identifier: " + identifier);
        return null;
    }

    /**
     * Generates a unique ID for a new rescuer based on the current maximum in the cache.
     *
     * @param cache the current list of rescuers
     * @return a new unique ID
     */
    private int generateId(List<Rescuer> cache) {
        int maxId = 0;
        for (Rescuer r : cache)
            if (r.getId() > maxId)
                maxId = r.getId();

        return maxId + 1;
    }

    /**
     * Adds a rescuer to the database and cache.
     * If the rescuer already exists, a new ID is generated.
     *
     * @param rescuer the rescuer to add
     */
    public void add(Rescuer rescuer) {
        if (get(rescuer.getId()) != null) {
            Logger.warn("A rescuer with ID " + rescuer.getId() + " already exists. Generating a new ID.");
            rescuer.setId(generateId(cache));
        }

        if (dao.insert(rescuer)) {
            this.cache.add(rescuer);
            Logger.info("Rescuer inserted and cached with ID: " + rescuer.getId());
        } else {
            Logger.error("Failed to insert rescuer into database.");
        }
    }

    /**
     * Updates a rescuer in both the database and cache.
     *
     * @param updatedRescuer the rescuer to update
     */
    public void update(Rescuer updatedRescuer) {
        Rescuer existing = get(updatedRescuer.getId());
        if (existing == null) {
            Logger.warn("No existing rescuer with ID " + updatedRescuer.getId() + " found.");
            return;
        }

        if (dao.update(updatedRescuer)) {
            int index = cache.indexOf(existing);
            cache.set(index, updatedRescuer);
            Logger.info("Rescuer updated in DB and cache.");
        } else {
            Logger.error("Failed to update rescuer in database.");
        }
    }

    /**
     * Removes a rescuer from the database and cache by ID.
     *
     * @param id the ID of the rescuer to remove
     */
    public void remove(int id) {
        Rescuer rescuerToRemove = get(id);
        if (rescuerToRemove == null) {
            Logger.warn("No rescuer found with ID " + id + ".");
            return;
        }

        if (dao.delete(id)) {
            this.cache.remove(rescuerToRemove);
            Logger.info("Rescuer with ID " + id + " has been removed.");
        } else {
            Logger.error("Failed to delete rescuer from database.");
        }
    }

    /**
     * Returns all rescuers in the system (defensive copy).
     *
     * @return list of all {@link Rescuer}
     */
    public List<Rescuer> getAll() {
        return new ArrayList<>(this.cache); // copie défensive
    }

    /**
     * Replaces all rescuers in the system.
     * ⚠ Clears the cache and database and inserts the new list.
     *
     * @param cache the new list of rescuers to use
     */
    public void setAll(List<Rescuer> cache) {
        for (Rescuer resc : this.cache) {
            remove(resc.getId());
        }
        for (Rescuer resc : cache) {
            add(resc);
        }
    }
}
