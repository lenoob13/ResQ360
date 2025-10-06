package model.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a DPS (medical support deployment) for a given day, site, and sport.
 * 
 * <p>Each DPS has needs (skills required), and is linked to a day, site, and optionally a sport.</p>
 * 
 * @author ResQ360
 */
public class DPS {
    private int idDPS;
    private int idJournee;
    private String idSite;
    private Map<Integer, Integer> besoins; // clé = idCompetence, valeur = nombre requis
    private String idSport;
    private String note;


    /**
     * Creates a DPS with its ID, day ID, and site ID.
     *
     * @param idDPS the DPS identifier
     * @param idJournee the day ID (linked to a Day object)
     * @param idSite the site ID (linked to a Site)
     */
    public DPS(int idDPS, int idJournee, String idSite) {
        this.idDPS = idDPS;
        this.idJournee = idJournee;
        this.idSite = idSite;
        this.besoins = new HashMap<>();
        this.idSport = new String();
        this.note = new String();
    }


    /**
     * Gets the DPS ID.
     */
    public int getId() { 
        return idDPS; 
    }
    /**
     * Gets the associated day ID.
     */
    public int getIdJournee() { 
        return idJournee; 
    }
    /**
     * Gets the associated site ID.
     */
    public String getIdSite() { 
        return idSite; 
    }
    /**
     * Gets the skill needs (map: skill ID → required number).
     */
    public Map<Integer, Integer> getBesoins() { 
        return besoins; 
    }
    /**
     * Adds or updates a required skill and its quantity.
     *
     * @param idCompetence the skill ID
     * @param nombre the number of people needed
     */
    public void setBesoins(int idCompetence, int nombre) { 
        this.besoins.put(idCompetence, nombre); 
    }
    /**
     * Gets the associated sport ID.
     */
    public String getIdSport() { 
        return idSport; 
    }
    /**
     * Gets the note or comment.
     */
    public String getNote() { 
        return note; 
    }

     /**
     * Sets the sport ID.
     */
    public void setIdSport(String idSport) { 
        this.idSport = idSport; 
    }
    /**
     * Sets the DPS ID.
     */
    public void setIdDPS(int idDPS) { 
        this.idDPS = idDPS; 
    }
    /**
     * Sets the day ID.
     */
    public void setIdJournee(int idJournee) { 
        this.idJournee = idJournee;
    }
    /**
     * Sets the site ID.
     */
    public void setIdSite(String idSite) { 
        this.idSite = idSite; 
    }
    /**
     * Replaces all skill needs with a new map.
     */
    public void setBesoins(Map<Integer, Integer> besoins) {
        this.besoins = besoins;
    }
    /**
     * Sets the note or comment.
     */
    public void setNote(String note) { 
        this.note = note; 
    }
    /**
     * Displays the DPS as a readable string.
     */
    @Override
    public String toString() {
        return "DPS {" +
                "\n  idDPS = " + idDPS +
                ",\n  idJournee = " + idJournee +
                ",\n  idSite = '" + idSite + '\'' +
                ",\n  besoins = " + besoins +
                ",\n  idSport = '" + idSport + '\'' +
                "\n}";
    }
}
