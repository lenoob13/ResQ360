package model.dao;

import core.DAO;
import model.persistence.Rescuer;
import model.persistence.Site;
import model.services.DatabaseConnector;
import util.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * DAO class for accessing and managing Rescuer data from the database.
 */
public class DAORescuer implements DAO<Rescuer>{

    private final Connection connection;

    /**
     * Creates a DAORescuer using the current database connection.
     */
    public DAORescuer() {
        this.connection = DatabaseConnector.get().getConnection();
    }

    /**
     * Retrieves a rescuer by their unique ID.
     *
     * @param id the rescuer's ID
     * @return the Rescuer object if found, null otherwise
     */
    public Rescuer get(int id) {
        String sql = "SELECT * FROM Secouriste WHERE idSecouriste = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String identifier = resultSet.getString("identifiant");
                String email = resultSet.getString("email");
                String password = resultSet.getString("motDePasse");
                String name = resultSet.getString("nom");
                String fName = resultSet.getString("prenom");

                List<String> skills = getSkills(id);
                List<Integer> assignments = getAssignments(id);
                boolean admin = resultSet.getBoolean("admin");

                Rescuer rescuer = new Rescuer(id, identifier, email, skills, password,admin,assignments,name,fName);

                return rescuer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a rescuer by their identifier (login).
     *
     * @param identifier the unique identifier
     * @return the corresponding Rescuer or null if not found
     */
    public Rescuer getByIdentifier(String identifier) {
        Rescuer rescuer = null;
        String sql = "SELECT * FROM Secouriste WHERE identifiant = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, identifier);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("idSecouriste");
                    String email = rs.getString("email");

                    String password = rs.getString("motDePasse");
                    boolean admin = rs.getBoolean("admin");
                    String name = rs.getString("nom");
                    String fName = rs.getString("prenom");

                    List<String> skills = getSkills(id);
                    List<Integer> assignments = getAssignments(id);

                    rescuer = new Rescuer(id, identifier, email, skills, password, admin,assignments,name,fName);
                }
            }
        } catch (SQLException e) {
            Logger.error("Erreur lors de la récupération du Secouriste par identifiant", e);
        }

        return rescuer;
    }

    /**
     * Retrieves all rescuers (excluding admins).
     *
     * @return a list of all non-admin rescuers
     */
    public List<Rescuer> getAll() {
        List<Rescuer> rescuers = new ArrayList<>();
        String sql = "SELECT * FROM Secouriste";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("idSecouriste");
                String identifier = resultSet.getString("identifiant");
                String email = resultSet.getString("email");
                String password = resultSet.getString("motDePasse");
                boolean admin = resultSet.getBoolean("admin");
                String name = resultSet.getString("nom");
                String fName = resultSet.getString("prenom");

                List<String> skills = getSkills(id);
                List<Integer> assignments = getAssignments(id);

                Rescuer rescuer = new Rescuer(id, identifier, email, skills, password,admin,assignments,name,fName);

                if (!rescuer.getAdmin()) rescuers.add(rescuer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rescuers;
    }

    /**
     * Inserts a new rescuer into the database and links their skills and assignments.
     *
     * @param rescuer the rescuer to insert
     * @return true if insertion succeeded, false otherwise
     */
    public boolean insert(Rescuer rescuer) {
        String sql = """
            INSERT INTO Secouriste (idSecouriste, identifiant, email, motDePasse, admin, nom, prenom)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rescuer.getId());
            stmt.setString(2, rescuer.getIdentifier());
            stmt.setString(3, rescuer.getEmail());
            stmt.setString(4, rescuer.getPassword());
            stmt.setBoolean(5, rescuer.getAdmin());
            stmt.setString(6, rescuer.getName());
            stmt.setString(7, rescuer.getFName());

            stmt.executeUpdate();

            insertSkills(rescuer);
            insertAssignments(rescuer);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates a rescuer's personal information, skills, and assignments.
     *
     * @param rescuer the rescuer to update
     * @return true if update succeeded, false otherwise
     */
    public boolean update(Rescuer rescuer) {
        String sql = """
            UPDATE Secouriste
            SET identifiant = ?, email = ?, motDePasse = ?, nom = ?, prenom = ?
            WHERE idSecouriste = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, rescuer.getIdentifier());
            stmt.setString(2, rescuer.getEmail());
            stmt.setString(3, rescuer.getPassword());
            stmt.setString(4, rescuer.getName());
            stmt.setString(5, rescuer.getFName());
            stmt.setInt(6, rescuer.getId());
            stmt.executeUpdate();

            deleteLinks(rescuer.getId());
            insertSkills(rescuer);
            insertAssignments(rescuer);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a rescuer from the database, including all links (skills and assignments).
     *
     * @param id the rescuer's ID
     * @return true if deletion succeeded, false otherwise
     */
    public boolean delete(int id) {
        try {
            deleteLinks(id);
            String sql = "DELETE FROM Secouriste WHERE idSecouriste = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------- PRIVATE HELPERS ----------------

    /**
     * Retrieves the list of skills for the given rescuer ID.
     *
     * @param id the rescuer's ID
     * @return a list of skill names
     * @throws SQLException if a database error occurs
     */
    private List<String> getSkills(int id) throws SQLException {
        List<String> skills = new ArrayList<>();
        String sql = """
            SELECT c.intitule FROM Possede p
            JOIN Competence c ON p.idCompetence = c.idCompetence
            WHERE p.idSecouriste = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                skills.add(resultSet.getString("intitule"));
            }
        }
        return skills;
    }

    /**
     * Retrieves the list of DPS assignments for the given rescuer ID.
     *
     * @param id the rescuer's ID
     * @return a list of DPS IDs
     * @throws SQLException if a database error occurs
     */
    private List<Integer> getAssignments(int id) throws SQLException {
        List<Integer> assignments = new ArrayList<>();
        String sql = "SELECT idDPS FROM EstAffecteA WHERE idSecouriste = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                assignments.add(resultSet.getInt("idDPS"));
            }
        }
        return assignments;
    }

    /**
     * Inserts skill links into the database for the given rescuer.
     *
     * @param rescuer the rescuer whose skills to insert
     * @throws SQLException if a database error occurs
     */
    private void insertSkills(Rescuer rescuer) throws SQLException {
        String sql = """
            INSERT INTO Possede (idSecouriste, idCompetence)
            SELECT ?, idCompetence FROM Competence WHERE intitule = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (String skill : rescuer.getSkills()) {
                stmt.setInt(1, rescuer.getId());
                stmt.setString(2, skill);
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Inserts DPS assignments for the given rescuer.
     *
     * @param rescuer the rescuer whose assignments to insert
     * @throws SQLException if a database error occurs
     */
    private void insertAssignments(Rescuer rescuer) throws SQLException {
        String sql = "INSERT INTO EstAffecteA (idSecouriste, idDPS) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int dpsId : rescuer.getAssignments()) {
                stmt.setInt(1, rescuer.getId());
                stmt.setInt(2, dpsId);
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Deletes all skill and assignment links for a given rescuer.
     *
     * @param id the rescuer's ID
     * @throws SQLException if a database error occurs
     */
    private void deleteLinks(int id) throws SQLException {
        String[] tables = {"Possede", "EstAffecteA"};
        for (String table : tables) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + table + " WHERE idSecouriste = ?")) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        }
    }
}
