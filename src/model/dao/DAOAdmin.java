package model.dao;

import model.persistence.Admin;
import model.persistence.User;
import model.services.DatabaseConnector;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class for managing Admin users in the database.
 */
public class DAOAdmin {

    private final Connection connection;

    /**
     * Constructs a DAOAdmin with the default database connection.
     */
    public DAOAdmin() {
        this.connection = DatabaseConnector.get().getConnection();
    }

    /**
     * Retrieves an Admin by its identifier.
     *
     * @param identifier the admin's identifier
     * @return the Admin object, or null if not found
     */
    public Admin getByIdentifier(String identifier) {
        String sql = "SELECT identifiant, motDePasse FROM Admin WHERE identifiant = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, identifier);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String password = rs.getString("motDePasse");
                return new Admin(identifier, password);
            }
        } catch (SQLException e) {
            Logger.error("Erreur lors de la récupération de l'Admin par identifiant", e);
        }
        return null;
    }

    /**
     * Inserts a new Admin into the database.
     *
     * @param admin the admin to insert
     * @return true if insertion succeeded, false otherwise
     */
    public boolean insert(Admin admin) {
        String sql = "INSERT INTO Admin (identifiant, motDePasse) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, admin.getIdentifier());
            stmt.setString(2, admin.getPassword());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.error("Erreur lors de l'insertion de l'Admin", e);
        }
        return false;
    }

    /**
     * Updates the password of an existing Admin.
     *
     * @param admin the admin to update
     * @return true if update succeeded, false otherwise
     */
    public boolean update(Admin admin) {
        String sql = "UPDATE Admin SET motDePasse = ? WHERE identifiant = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, admin.getPassword());
            stmt.setString(2, admin.getIdentifier());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.error("Erreur lors de la mise à jour de l'Admin", e);
        }
        return false;
    }

    /**
     * Deletes an Admin by its identifier.
     *
     * @param identifier the identifier of the admin to delete
     * @return true if deletion succeeded, false otherwise
     */
    public boolean delete(String identifier) {
        String sql = "DELETE FROM Admin WHERE identifiant = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, identifier);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.error("Erreur lors de la suppression de l'Admin", e);
        }
        return false;
    }
}
