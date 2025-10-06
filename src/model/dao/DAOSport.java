package model.dao;

import core.DAO;
import model.persistence.Sport;
import model.services.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class to manage database operations for the Sport entity.
 */
public class DAOSport implements DAO<Sport> {

    private final Connection connection;

    /**
     * Creates a DAOSport instance using the shared database connection.
     */
    public DAOSport() {
        this.connection = DatabaseConnector.get().getConnection();
    }

     /**
     * This method is not supported for Sport.
     * Use {@link #get(String)} instead.
     */
    @Override
    public Sport get(int id) {
        throw new UnsupportedOperationException("Use get(String code) instead.");
    }

    /**
     * Retrieves a sport by its code.
     *
     * @param code the ID of the sport
     * @return the Sport object, or null if not found
     */
    public Sport get(String code) {
        String sql = "SELECT * FROM Sport WHERE idSport = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Sport(
                        rs.getString("nom"),
                        rs.getString("idSport")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans get(code) : " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all sports from the database.
     *
     * @return a list of all Sport objects
     */
    @Override
    public List<Sport> getAll() {
        List<Sport> sports = new ArrayList<>();
        String sql = "SELECT * FROM Sport";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sports.add(new Sport(
                        rs.getString("nom"),
                        rs.getString("idSport")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans getAll() : " + e.getMessage());
        }
        return sports;
    }

    /**
     * Inserts a new sport into the database.
     *
     * @param sport the Sport to insert
     * @return true if insertion was successful, false otherwise
     */
    @Override
    public boolean insert(Sport sport) {
        String sql = "INSERT INTO Sport (idSport, nom) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sport.getCode());
            stmt.setString(2, sport.getName());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur dans insert() : " + e.getMessage());
        }
        return false;
    }
    /**
     * Updates an existing sport in the database.
     *
     * @param sport the updated Sport
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean update(Sport sport) {
        String sql = "UPDATE Sport SET nom = ? WHERE idSport = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sport.getName());
            stmt.setString(2, sport.getCode());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur dans update() : " + e.getMessage());
        }
        return false;
    }

    /**
     * This method is not supported for Sport.
     * Use {@link #delete(String)} instead.
     */
    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Use delete(String code) instead.");
    }

    /**
     * Deletes a sport from the database by its code.
     *
     * @param code the ID of the sport to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean delete(String code) {
        String sql = "DELETE FROM Sport WHERE idSport = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur dans delete(code) : " + e.getMessage());
        }
        return false;
    }
}
