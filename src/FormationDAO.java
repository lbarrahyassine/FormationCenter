import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Formation operations
 */
public class FormationDAO {

    /**
     * Gets all formations from the database
     *
     * @return List of Formation objects
     */
    public List<Formation> getAllFormations() {
        List<Formation> formations = new ArrayList<>();
        String query = "SELECT * FROM formation ORDER BY date_debut";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                formations.add(new Formation(
                        rs.getInt("id_formation"),
                        rs.getString("libele"),
                        rs.getString("description"),
                        rs.getDate("date_debut"),
                        rs.getInt("duree"),
                        rs.getDouble("tarif")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting formations: " + e.getMessage());
        }

        return formations;
    }

    /**
     * Gets a formation by ID
     *
     * @param formationId the formation ID
     * @return Formation object if found, null otherwise
     */
    public Formation getFormationById(int formationId) {
        String query = "SELECT * FROM formation WHERE id_formation = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, formationId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Formation(
                            rs.getInt("id_formation"),
                            rs.getString("libele"),
                            rs.getString("description"),
                            rs.getDate("date_debut"),
                            rs.getInt("duree"),
                            rs.getDouble("tarif")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting formation: " + e.getMessage());
        }

        return null;
    }

    /**
     * Gets the current enrollment count for a formation
     *
     * @param formationId the formation ID
     * @return the enrollment count
     */
    public int getEnrollmentCount(int formationId) {
        String query = "SELECT COUNT(*) FROM formation WHERE id_formation = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, formationId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting enrollment count: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Checks if a formation is available for enrollment (has space)
     *
     * @param formationId the formation ID
     * @return true if available, false otherwise
     */
    public boolean isFormationAvailable(int formationId) {
        return true;
    }


    // ------------------------------------CRUD------------------------------------------------
    public boolean insertFormation(Formation formation) {
        String query = "INSERT INTO formation (libele, description, date_debut, duree, tarif) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, formation.getTitle());
            stmt.setString(2, formation.getDescription());
            stmt.setDate(3, (Date) formation.getStartDate());
            stmt.setInt(4, formation.getduree());
            stmt.setDouble(5, formation.getPrice());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        formation.setFormationId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting formation: " + e.getMessage()+e.getStackTrace());
        }
        return false;
    }

    public boolean updateFormation(Formation formation) {
        String query = "UPDATE formation SET libele = ?, description = ?, date_debut = ?, duree = ?, tarif = ? WHERE id_formation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, formation.getTitle());
            stmt.setString(2, formation.getDescription());
            stmt.setDate(3, (Date) formation.getStartDate());
            stmt.setInt(4, formation.getduree());
            stmt.setDouble(5, formation.getPrice());
            stmt.setInt(6, formation.getFormationId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating formation: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteFormation(int id) {
        String query = "DELETE FROM formation WHERE id_formation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting formation: " + e.getMessage());
        }
        return false;
    }
}
