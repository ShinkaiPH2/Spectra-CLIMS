package model;

/**
 * Simple User model used for authentication and authorization checks.
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String role;

    /**
     * Create an empty User instance. Fields can be set with the setters.
     */
    public User() {
    }

    /**
     * Create a User with fields initialized.
     *
     * @param id       user id
     * @param username login username
     * @param password login password (plain text in this sample app)
     * @param role     user role string
     */
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Get user id.
     *
     * @return user id
     */
    public int getId() {
        return id;
    }

    /**
     * Set user id.
     *
     * @param id user id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the username.
     *
     * @return username string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username.
     *
     * @param username username string
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password. Note: passwords are stored as plain text in this
     * simple example.
     *
     * @return password string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password.
     *
     * @param password password string
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get user role.
     *
     * @return role string
     */
    public String getRole() {
        return role;
    }

    /**
     * Set user role.
     *
     * @param role role string
     */
    public void setRole(String role) {
        this.role = role;
    }
}
