package model.dao;

import core.DAO;
import model.persistence.Site;
import model.services.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for handling database operations related to the Site entity.
 */
public class DAOSite implements DAO<Site> {

    private final Connection connection;

    /**
     * Creates a new DAOSite instance using the shared database connection.
     */
    public DAOSite() {
        this.connection = DatabaseConnector.get().getConnection();
    }

    /**
     * This method is not supported for Site.
     * Use {@link #get(String)} instead.
     */
    @Override
    public Site get(int id) {
        // Ce DAO utilise un code de type String, donc cette méthode n’est pas pertinente avec un int.
        // On peut soit :
        // 1. lever une exception ;
        // 2. surcharger get(String code) ;
        // 3. forcer le cast : ici on lève une exception.
        throw new UnsupportedOperationException("Use get(String code) instead.");
    }

    /**
     * Retrieves a site by its code.
     *
     * @param code the site ID
     * @return the Site object, or null if not found
     */
    public Site get(String code) {
        String sql = "SELECT * FROM Site WHERE idSite = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Site(
                        rs.getString("idSite"),
                        rs.getString("nom"),
                        rs.getFloat("longitude"),
                        rs.getFloat("latitude")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans get(code) : " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all sites from the database.
     *
     * @return a list of all Site objects
     */
    @Override
    public List<Site> getAll() {
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT * FROM Site";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sites.add(new Site(
                        rs.getString("idSite"),
                        rs.getString("nom"),
                        rs.getFloat("longitude"),
                        rs.getFloat("latitude")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans getAll() : " + e.getMessage());
        }
        return sites;
    }

    /**
     * Inserts a new site into the database.
     *
     * @param site the Site to insert
     * @return true if insertion was successful, false otherwise
     */
    @Override
    public boolean insert(Site site) {
        String sql = "INSERT INTO Site (idSite, nom, longitude, latitude) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, site.getCode());
            stmt.setString(2, site.getName());
            stmt.setFloat(3, site.getLongitude());
            stmt.setFloat(4, site.getLatitude());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur dans insert() : " + e.getMessage());
        }
        return false;
    }

    /**
     * Updates an existing site in the database.
     *
     * @param site the updated Site
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean update(Site site) {
        String sql = "UPDATE Site SET nom = ?, longitude = ?, latitude = ? WHERE idSite = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, site.getName());
            stmt.setFloat(2, site.getLongitude());
            stmt.setFloat(3, site.getLatitude());
            stmt.setString(4, site.getCode());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur dans update() : " + e.getMessage());
        }
        return false;
    }

    /**
     * This method is not supported for Site.
     * Use {@link #delete(String)} instead.
     */
    
    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Use delete(String code) instead.");
    }

    /**
     * Deletes a site from the database by its code.
     *
     * @param code the site ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean delete(String code) {
        String sql = "DELETE FROM Site WHERE idSite = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur dans delete(code) : " + e.getMessage());
        }
        return false;
    }
}
