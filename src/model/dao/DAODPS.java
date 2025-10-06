package model.dao;

import core.DAO;
import model.persistence.DPS;
import model.services.DatabaseConnector;

import java.sql.*;
import java.util.*;

/**
 * DAO class for managing DPS entities in the database.
 */
public class DAODPS implements DAO<DPS> {

    private final Connection connection;

    /**
     * Constructs a DAODPS instance using the default database connection.
     */
    public DAODPS() {
        this.connection = DatabaseConnector.get().getConnection();
    }

    /**
     * Retrieves a DPS by its ID, along with its associated needs and sport.
     *
     * @param id the DPS ID
     * @return the corresponding DPS, or null if not found
     */
    @Override
    public DPS get(int id) {
        String sql = "SELECT * FROM DPS WHERE idDPS = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int idJournee = rs.getInt("idJournee");
                String idSite = rs.getString("idSite");
                String note=rs.getString("note");
                DPS dps = new DPS(id, idJournee, idSite);
                dps.setNote(note);

                // Charger les besoins associés
                Map<Integer, Integer> besoins = new HashMap<>();
                String besoinSql = "SELECT idCompetence, nombre FROM Besoin WHERE idDPS = ?";
                try (PreparedStatement besoinStmt = connection.prepareStatement(besoinSql)) {
                    besoinStmt.setInt(1, id);
                    ResultSet besoinRs = besoinStmt.executeQuery();
                    while (besoinRs.next()) {
                        int idComp = besoinRs.getInt("idCompetence");
                        int nombre = besoinRs.getInt("nombre");
                        besoins.put(idComp, nombre);
                    }
                }
                dps.setBesoins(besoins);
                String sportSql = "SELECT idSport FROM Concerne WHERE idDPS = ?";
                try (PreparedStatement sportStmt = connection.prepareStatement(sportSql)) {
                    sportStmt.setInt(1, id);
                    ResultSet sportRs = sportStmt.executeQuery();
                    if (sportRs.next()) {
                        dps.setIdSport(sportRs.getString("idSport"));
                    }
                }
                return dps;
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans get() : " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all DPS entries from the database, including their needs and sport.
     *
     * @return a list of all DPS entries
     */
    @Override
    public List<DPS> getAll() {
        List<DPS> list = new ArrayList<>();
        String sql = "SELECT * FROM DPS";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int idDPS = rs.getInt("idDPS");
                int idJournee = rs.getInt("idJournee");
                String idSite = rs.getString("idSite");
                String note=rs.getString("note");
                DPS dps = new DPS(idDPS, idJournee, idSite);
                dps.setNote(note);

                // Charger les besoins associés
                Map<Integer, Integer> besoins = new HashMap<>();
                String besoinSql = "SELECT idCompetence, nombre FROM Besoin WHERE idDPS = ?";
                try (PreparedStatement besoinStmt = connection.prepareStatement(besoinSql)) {
                    besoinStmt.setInt(1, idDPS);
                    ResultSet besoinRs = besoinStmt.executeQuery();
                    while (besoinRs.next()) {
                        int idComp = besoinRs.getInt("idCompetence");
                        int nombre = besoinRs.getInt("nombre");
                        besoins.put(idComp, nombre);
                    }
                }
                dps.setBesoins(besoins);
                // Charger le sport associé
                String sportSql = "SELECT idSport FROM Concerne WHERE idDPS = ?";
                try (PreparedStatement sportStmt = connection.prepareStatement(sportSql)) {
                    sportStmt.setInt(1, idDPS);
                    ResultSet sportRs = sportStmt.executeQuery();
                    if (sportRs.next()) {
                        dps.setIdSport(sportRs.getString("idSport"));
                    }
                }

                list.add(dps);
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans getAll() : " + e.getMessage());
        }
        return list;
    }

    /**
     * Inserts a new DPS into the database, including its needs and sport.
     *
     * @param dps the DPS to insert
      @return true if the insertion was successful, false otherwise
     */
    @Override
    public boolean insert(DPS dps) {
        String sql = "INSERT INTO DPS (idDPS, idJournee, idSite,note) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, dps.getId());
            stmt.setInt(2, dps.getIdJournee());
            stmt.setString(3, dps.getIdSite());
            stmt.setString(4, dps.getNote());
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                // Insérer les besoins
                String besoinSql = "INSERT INTO Besoin (idDPS, idCompetence, nombre) VALUES (?, ?, ?)";
                try (PreparedStatement besoinStmt = connection.prepareStatement(besoinSql)) {
                    for (Map.Entry<Integer, Integer> entry : dps.getBesoins().entrySet()) {
                        besoinStmt.setInt(1, dps.getId());
                        besoinStmt.setInt(2, entry.getKey());
                        besoinStmt.setInt(3, entry.getValue());
                        besoinStmt.addBatch();
                    }
                    besoinStmt.executeBatch();
                }
                // Insérer le sport concerné
                String sportSql = "INSERT INTO Concerne (idDPS, idSport) VALUES (?, ?)";
                try (PreparedStatement sportStmt = connection.prepareStatement(sportSql)) {
                    sportStmt.setInt(1, dps.getId());
                    sportStmt.setString(2, dps.getIdSport());
                    sportStmt.executeUpdate();
                }

                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans insert() : " + e.getMessage());
        }
        return false;
    }

    /**
     * Updates an existing DPS and replaces its needs and sport.
     *
     * @param dps the DPS to update
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean update(DPS dps) {
        String sql = "UPDATE DPS SET idJournee = ?, idSite = ?, note = ? WHERE idDPS = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, dps.getIdJournee());
            stmt.setString(2, dps.getIdSite());
            stmt.setString(3, dps.getNote());
            stmt.setInt(4, dps.getId());
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                // Supprimer les anciens besoins
                String deleteBesoins = "DELETE FROM Besoin WHERE idDPS = ?";
                try (PreparedStatement deleteStmt = connection.prepareStatement(deleteBesoins)) {
                    deleteStmt.setInt(1, dps.getId());
                    deleteStmt.executeUpdate();
                }
                // Supprimer l’ancien sport
                String deleteSportSql = "DELETE FROM Concerne WHERE idDPS = ?";
                try (PreparedStatement otherStmt = connection.prepareStatement(deleteSportSql)) {
                    otherStmt.setInt(1, dps.getId());
                    otherStmt.executeUpdate();
                }

                // Réinsérer les besoins
                String besoinSql = "INSERT INTO Besoin (idDPS, idCompetence, nombre) VALUES (?, ?, ?)";
                try (PreparedStatement besoinStmt = connection.prepareStatement(besoinSql)) {
                    for (Map.Entry<Integer, Integer> entry : dps.getBesoins().entrySet()) {
                        besoinStmt.setInt(1, dps.getId());
                        besoinStmt.setInt(2, entry.getKey());
                        besoinStmt.setInt(3, entry.getValue());
                        besoinStmt.addBatch();
                    }
                    besoinStmt.executeBatch();
                }
                // Réinsérer le sport
                String insertSportSql = "INSERT INTO Concerne (idDPS, idSport) VALUES (?, ?)";
                try (PreparedStatement otherStmt = connection.prepareStatement(insertSportSql)) {
                    otherStmt.setInt(1, dps.getId());
                    otherStmt.setString(2, dps.getIdSport());
                    otherStmt.executeUpdate();
                }

                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur dans update() : " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a DPS and all associated data (needs, sport).
     *
     * @param id the ID of the DPS to delete
     * @return true if the deletion was successful, false otherwise
     */
    @Override
    public boolean delete(int id) {
        // Supprimer les besoins d'abord
        String deleteBesoins = "DELETE FROM Besoin WHERE idDPS = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteBesoins)) {
            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression des besoins : " + e.getMessage());
        }
        // Supprimer le sport concerné
        String deleteSportSql = "DELETE FROM Concerne WHERE idDPS = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSportSql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du sport : " + e.getMessage());
        }

        // Supprimer le DPS
        String sql = "DELETE FROM DPS WHERE idDPS = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur dans delete() : " + e.getMessage());
        }
        return false;
    }
}
