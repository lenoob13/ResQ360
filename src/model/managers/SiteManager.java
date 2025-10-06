package model.managers;

import model.dao.DAOSite;
import model.persistence.Site;
import util.Logger;

import java.util.ArrayList;
import java.util.List;
/**
 * Manages operations related to Site objects: add, remove, update, and retrieve.
 * Uses a local cache to reduce database queries.
 * All changes are synchronized with the database using a DAO.
 * 
 * Author: ResQ360
 */
public class SiteManager {
    private final List<Site> cache;
    private final DAOSite dao;

    /**
     * Initializes the manager and loads all sites from the database into the cache.
     */
    SiteManager() {
        cache = new ArrayList<>();
        dao = new DAOSite();
        this.cache.addAll(dao.getAll());
    }

    /**
     * Returns a site by its unique code.
     * 
     * @param code the site code
     * @return the matching Site, or null if not found
     */
    public Site get(String index) {
        for(Site site: cache){
            if(site.getCode().equals(index)){
                return site;
            }
        }
        return null;
    }

    /**
     * Replaces an existing site with a new one by removing and re-adding it.
     * 
     * @param code the site code
     * @param name the site name
     * @param longitude the longitude
     * @param latitude the latitude
     */
    public void changeSite(String code, String name, float longitude, float latitude){
        if (get(code) != null) {
            Logger.warn("No Site with this code exists.");
            return;
        }
        remove(code);
        Site newSite = new Site(code, name, longitude,latitude);
        add(newSite);
    }

    /**
     * Adds a new site if it does not already exist.
     * 
     * @param site the Site to add
     */
    public void add(Site site){
        if (get(site.getCode()) != null) {
            Logger.warn("A Site with this code already exists.");
            return;
        }
        if (dao.insert(site)) {
            this.cache.add(site);
        }else {
            Logger.warn("SQL failed");
        }
    }

    /**
     * Removes a site from both the cache and the database.
     * 
     * @param code the site code to remove
     */
    public void remove(String code){
        if(get(code) == null){
            Logger.warn("No Site with this code exists.");
            return;
        }
        if (dao.delete(code)) {
            this.cache.remove(get(code));
        } else {Logger.warn("SQL failed");}
    }

    /**
     * Returns the full list of cached sites.
     * 
     * @return the list of all sites
     */
    public List<Site> getAll(){
        return cache;
    }

    /**
     * Replaces all existing sites with a new list.
     * 
     * @param cache the new list of sites
     */
    public void setAll(List<Site> cache) {
        for (Site site:this.cache) {
            remove(site.getCode());
        }
        for (Site site: cache) {
            add(site);
        }
    }
}
