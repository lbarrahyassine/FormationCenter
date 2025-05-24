import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    private ClientDAO clientDAO = new ClientDAO();
    private FormationDAO formationDAO = new FormationDAO();

    public boolean createEnrollment(Enrollment enrollment) {
        String query = "INSERT INTO client_formation (id_client, id_formation) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, enrollment.getClientId());
            stmt.setInt(2, enrollment.getFormationId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating enrollment: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEnrollment(int clientId, int formationId) {
        String query = "DELETE FROM client_formation WHERE id_client = ? AND id_formation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            stmt.setInt(2, formationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting enrollment: " + e.getMessage());
            return false;
        }
    }

    public List<Enrollment> getEnrollmentsByClient(int clientId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT * FROM client_formation WHERE id_client = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, clientId);

            // First get all enrollment records
            List<Integer> formationIds = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    formationIds.add(rs.getInt("id_formation"));
                }
            }

            // Then get client and formations data
            Client client = clientDAO.getClientById(clientId);
            for (Integer formationId : formationIds) {
                Formation formation = formationDAO.getFormationById(formationId);
                if (formation != null && client != null) {
                    Enrollment enrollment = new Enrollment(0, clientId, formationId, null, null);
                    enrollment.setClient(client);
                    enrollment.setFormation(formation);
                    enrollments.add(enrollment); // THIS WAS MISSING!
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting client enrollments: " + e.getMessage());
            e.printStackTrace();
        }
        return enrollments;
    }

    public List<Enrollment> getEnrollmentsByFormation(int formationId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT * FROM client_formation WHERE id_formation = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, formationId);

            // First get all enrollment records
            List<Integer> clientIds = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientIds.add(rs.getInt("id_client"));
                }
            }

            // Then get formation and clients data
            Formation formation = formationDAO.getFormationById(formationId);
            for (Integer clientId : clientIds) {
                Client client = clientDAO.getClientById(clientId);
                if (client != null && formation != null) {
                    Enrollment enrollment = new Enrollment(0, clientId, formationId, null, null);
                    enrollment.setClient(client);
                    enrollment.setFormation(formation);
                    enrollments.add(enrollment); // Make sure this is added!
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting formation enrollments: " + e.getMessage());
            e.printStackTrace();
        }
        return enrollments;
    }

    public boolean isClientEnrolled(int clientId, int formationId) {
        String query = "SELECT COUNT(*) FROM client_formation WHERE id_client = ? AND id_formation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            stmt.setInt(2, formationId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking enrollment: " + e.getMessage());
            return false;
        }
    }

    // Alternative more efficient method to get all enrollments
    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT * FROM client_formation";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int clientId = rs.getInt("id_client");
                    int formationId = rs.getInt("id_formation");

                    Client client = clientDAO.getClientById(clientId);
                    Formation formation = formationDAO.getFormationById(formationId);

                    if (client != null && formation != null) {
                        Enrollment enrollment = new Enrollment(0, clientId, formationId, null, null);
                        enrollment.setClient(client);
                        enrollment.setFormation(formation);
                        enrollments.add(enrollment);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting all enrollments: " + e.getMessage());
            e.printStackTrace();
        }
        return enrollments;
    }
}