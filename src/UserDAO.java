
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for User authentication
 */
public class UserDAO {

    /**
     * Authenticates a user based on username and password
     *
     * @param username the username
     * @param password the password
     * @return User object if authentication successful, null otherwise
     */
    public User authenticate(String username, String password) {
        String[] tables = {"receptionniste", "administrateur"};

        for (String table : tables) {
            String query = "SELECT * FROM " + table + " WHERE username = ? AND password = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, username);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int id;
                        if (table.equals("administrateur")) {
                            id = rs.getInt("id_admin");
                        } else {
                            id = rs.getInt("id_professionnel");
                        }

                        return new User(
                                id,
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("nom"),
                                table // role
                        );
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error checking credentials in table " + table + ": " + e.getMessage());
            }
        }

        return null;
    }



    /**
     * Creates a new user in the database
     *
     * @param user the User object to create
     * @return true if creation was successful, false otherwise
     */
    public boolean createUser(User user) {
        String query = "INSERT INTO users (username, password, full_name, is_active) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }
}