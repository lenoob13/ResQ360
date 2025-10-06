package model.managers;

import model.dao.DAOSport;
import model.persistence.Sport;
import util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class for handling Sport entities.
 * Manages a local cache and ensures synchronization with the database through DAOSport.
 */
public class SportManager {
    private final List<Sport> cache;
    private final DAOSport dao;

     /**
     * Initializes the sport manager by loading all sports from the database into the cache.
     */
    SportManager() {
        cache = new ArrayList<>();
        dao = new DAOSport();
        this.cache.addAll(dao.getAll());
    }

    /**
     * Retrieves a sport by its code.
     *
     * @param code the sport code
     * @return the matching Sport, or null if not found
     */
    public Sport getSport(String index){
        for(Sport sport: cache){
            if(sport.getCode().equals(index)){
                return sport;
            }
        }
        return null;
    }
     /**
     * Returns the entire list of cached sports.
     *
     * @return the list of sports
     */
    public List<Sport> getCache(){
        return cache;
    }

   /**
     * Adds a new sport if its code does not already exist.
     *
     * @param sport the Sport to add
     */
    public void addSport(Sport sport){
        if (getSport(sport.getCode()) != null) {
            Logger.warn("A Sport with this code already exists.");
            return;
        }
        if(dao.insert(sport)){
            this.cache.add(sport);
        }else{
            Logger.warn("SQL failed");
        }
    }

    /**
     * Replaces an existing sport with a new one using the same code.
     *
     * @param code the sport code
     * @param name the new name for the sport
     */
    public void changeSport(String code, String name){
        if (getSport(code) == null) {
            Logger.warn("A Sport with this code does not exist.");
            return;
        }
        removeSport(code);
        Sport newSport = new Sport(code, name);
        addSport(newSport);
    }

    /**
     * Removes a sport by its code.
     *
     * @param code the sport code
     */
    public void removeSport(String code){
        if(getSport(code) == null) {
            Logger.warn("A Sport with this code does not exist.");
            return;
        }
        if(dao.delete(code)){
            this.cache.remove(getSport(code));
        }
    }

    /**
     * Replaces the entire list of sports with a new list.
     *
     * @param sports the new list of sports
     */
    public void setAll(List<Sport> sports) {
        for(Sport sport:this.cache){
            removeSport(sport.getCode());
        }
        for(Sport sport:sports){
            addSport(sport);
        }
    }
}
