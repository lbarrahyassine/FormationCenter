

/**
 * Model class representing a user/receptionist
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String role; // "admin" or "receptionniste"

    // Constructor with all fields
    public User(int id, String username, String password, String fullName, String role) {
        this.userId = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    // Constructor for new users
    public User(String username, String password, String fullName, String role, boolean isActive) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role=role;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;


}}