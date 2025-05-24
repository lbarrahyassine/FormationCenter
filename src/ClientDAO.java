import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Client operations
 */
public class ClientDAO {

    /**
     * Creates a new client in the database
     *
     * @param client the Client object to create
     * @return the created client with generated ID, or null if creation failed
     */
    public Client createClient(Client client) {
        String query = "INSERT INTO client (nom, prenom, email, telephone, date_arrivee) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getLastname());
            stmt.setString(2, client.getFirstname());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhoneNumber());
            stmt.setDate(5, new Date(client.getRegistrationDate().getTime()));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        client.setClientId(rs.getInt(1));
                        return client;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating client: " + e.getMessage());
        }

        return null;
    }


    /**
     * Updates an existing client in the database
     *
     * @param client the Client object to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateClient(Client client) {
        String query = "UPDATE clients SET nom = ?, prenom =?, email = ?, phone_number = ? " +
                "WHERE id_client = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, client.getLastname());
            stmt.setString(2, client.getFirstname());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhoneNumber());
            stmt.setInt(5, client.getClientId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating client: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets a client by ID
     *
     * @param clientId the client ID
     * @return Client object if found, null otherwise
     */
    public Client getClientById(int clientId) {
        String query = "SELECT * FROM client WHERE id_client = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("id_client"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("telephone"),
                            rs.getDate("date_arrivee")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting client: " + e.getMessage());
        }

        return null;
    }

    /**
     * Gets all clients from the database
     *
     * @return List of Client objects
     */
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM client ORDER BY nom";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id_client"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("telephone"),
                        rs.getDate("date_arrivee")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting clients: " + e.getMessage());
        }

        return clients;
    }

    /**
     * Searches for clients by name
     *
     * @param searchTerm the search term
     * @return List of matching Client objects
     */
    public List<Client> searchClientsByName(String searchTerm) {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM client WHERE nom LIKE ? ORDER BY nom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchTerm + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clients.add(new Client(
                            rs.getInt("id_client"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("telephone"),
                            rs.getDate("date_arrivee")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching clients: " + e.getMessage());
        }

        return clients;
    }
}