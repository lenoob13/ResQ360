package model.managers;

import model.dao.DAODay;
import model.persistence.Day;
import util.Logger;

import java.util.ArrayList;
import java.util.List;
/**
 * Manages {@link Day} objects in memory and synchronizes them with the database.
 *
 * <p>Uses an in-memory cache for fast access and a {@link DAODay} instance for
 * persistence operations.</p>
 *
 * @author ResQ360
 */
public class DayManager {
    private final List<Day> cache;  // Cache local des jours
    private final DAODay dao;    // DAO pour les accès base
    /**
     * Loads all days from the database into the cache.
     */
    DayManager() {
        this.cache = new ArrayList<>();
        this.dao = new DAODay();
        this.cache.addAll(dao.getAll()); // Chargement initial du cache
    }

    /**
     * Finds a day by its ID in the cache.
     *
     * @param id the day ID
     * @return the matching {@code Day}, or {@code null} if not found
     */
    public Day getDayById(int id) {
        for (Day day : cache) {
            if (day.getId() == id)
                return day;
        }
        Logger.debug("No Day found with ID: " + id);
        return null;
    }

    /**
     * Adds a new day to both the database and the cache.
     *
     * @param day the day to add
     */
    public void addDay(Day day) {
        if (getDayById(day.getId()) != null) {
            Logger.warn("A Day with ID " + day.getId() + " already exists.");
            return;
        }

        if (dao.insert(day)) {
            cache.add(day);
        } else {
            Logger.error("Failed to insert Day into database.");
        }
    }

    /**
     * Updates an existing day in both the database and the cache.
     *
     * @param day the day with updated values
     */
    public void updateDay(Day day) {
        Day existing = getDayById(day.getId());
        if (existing == null) {
            Logger.warn("No existing Day with ID " + day.getId() + " found.");
            return;
        }

        if (dao.update(day)) {
            int index = cache.indexOf(existing);
            cache.set(index, day);
        } else {
            Logger.error("Failed to update Day in database.");
        }
    }

    /**
     * Deletes a day from both the database and the cache.
     *
     * @param id the ID of the day to remove
     */
    public void removeDay(int id) {
        Day toRemove = getDayById(id);
        if (toRemove == null) {
            Logger.warn("No Day found with ID " + id + ".");
            return;
        }

        if (dao.delete(id)) {
            cache.remove(toRemove);
        } else {
            Logger.warn("Failed to delete Day from database.");
        }
    }

    /**
     * Returns a defensive copy of all cached days.
     *
     * @return list of all days
     */
    public List<Day> getAllDays() {
        return new ArrayList<>(cache);
    }

    /**
     * Replaces every stored day with a new list.
     * <p>Internally performs successive deletions and insertions.</p>
     *
     * @param newDays the new list of days
     *//**
     * Replaces every stored day with a new list.
     * <p>Internally performs successive deletions and insertions.</p>
     *
     * @param newDays the new list of days
     */
    public void setAll(List<Day> newDays) {
        // Supprimer tous les jours
        for (Day day : new ArrayList<>(cache)) {
            removeDay(day.getId());
        }

        // Ajouter les nouveaux jours
        for (Day day : newDays) {
            addDay(day);
        }
    }
}
