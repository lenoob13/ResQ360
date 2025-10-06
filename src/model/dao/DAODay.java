package model.dao;

import core.DAO;
import model.persistence.Day;
import model.services.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * DAO class for accessing and managing Day objects from the database.
 */
public class DAODay implements DAO<Day> {

    private DatabaseConnector database;
    private Connection connection;

    /**
     * Constructs a DAODay instance using the default database connector.
     */
    public DAODay() {
        this.database = DatabaseConnector.get();
        this.connection = database.getConnection();
    }

    /**
     * Retrieves a Day by its unique ID.
     *
     * @param id the ID of the Day
     * @return the Day object if found, null otherwise
     */
    @Override
    public Day get(int id) {
        String sql = "SELECT * FROM Journee WHERE idJour = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int day = rs.getInt("jour");
                int month = rs.getInt("mois");
                int year = rs.getInt("annee");
                int[] startTime = { rs.getInt("startHour"), rs.getInt("startMinute") };
                int[] endTime = { rs.getInt("endHour"), rs.getInt("endMinute") };

                return new Day(id, day, month, year, startTime, endTime);
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Erreur lors du get() : " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all Day records from the database.
     *
     * @return a list of all valid Day objects
     */
    @Override
    public List<Day> getAll() {
        List<Day> list = new ArrayList<>();
        String sql = "SELECT * FROM Journee";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("idJour");
                int day = rs.getInt("jour");
                int month = rs.getInt("mois");
                int year = rs.getInt("annee");
                int[] startTime = { rs.getInt("startHour"), rs.getInt("startMinute") };
                int[] endTime = { rs.getInt("endHour"), rs.getInt("endMinute") };

                try {
                    list.add(new Day(id, day, month, year, startTime, endTime));
                } catch (IllegalArgumentException e) {
                    System.err.println("Donnée ignorée car invalide : " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du getAll() : " + e.getMessage());
        }
        return list;
    }

    /**
     * Inserts a Day into the database.
     *
     * @param d the Day to insert
     * @return true if the insertion was successful, false otherwise
     */
    @Override
    public boolean insert(Day d) {
        String sql = "INSERT INTO Journee (idJour, jour, mois, annee, startHour, startMinute, endHour, endMinute) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, d.getId());
            stmt.setInt(2, d.getDay());
            stmt.setInt(3, d.getMonth());
            stmt.setInt(4, d.getYear());
            stmt.setInt(5, d.getStartTime()[0]);
            stmt.setInt(6, d.getStartTime()[1]);
            stmt.setInt(7, d.getEndTime()[0]);
            stmt.setInt(8, d.getEndTime()[1]);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion : " + e.getMessage());
        }
        return false;
    }

    /**
     * Updates an existing Day in the database.
     *
     * @param d the Day with updated values
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean update(Day d) {
        String sql = "UPDATE Journee SET jour = ?, mois = ?, annee = ?, " +
                     "startHour = ?, startMinute = ?, endHour = ?, endMinute = ? WHERE idJour = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, d.getDay());
            stmt.setInt(2, d.getMonth());
            stmt.setInt(3, d.getYear());
            stmt.setInt(4, d.getStartTime()[0]);
            stmt.setInt(5, d.getStartTime()[1]);
            stmt.setInt(6, d.getEndTime()[0]);
            stmt.setInt(7, d.getEndTime()[1]);
            stmt.setInt(8, d.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a Day from the database by its ID.
     *
     * @param id the ID of the Day to delete
     * @return true if the deletion was successful, false otherwise
     */
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Journee WHERE idJour = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
        return false;
    }
}
