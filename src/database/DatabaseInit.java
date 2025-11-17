package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Initialize database schema and seed minimal data.
 */
public class DatabaseInit {

    /**
     * Initialize database schema and seed minimal data.
     */

    public static void initialize() {
        try (Connection conn = DatabaseConnection.connect()) {
            if (conn == null)
                return;

            String usersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username TEXT UNIQUE NOT NULL,"
                    + "password TEXT NOT NULL,"
                    + "role TEXT" + ");";

            String devicesTable = "CREATE TABLE IF NOT EXISTS devices ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "device_number TEXT,"
                    + "type TEXT,"
                    + "brand TEXT,"
                    + "model TEXT,"
                    + "status TEXT,"
                    + "location TEXT,"
                    + "purchase_date TEXT,"
                    + "notes TEXT,"
                    + "cost REAL" + ");";

            String loginLogs = "CREATE TABLE IF NOT EXISTS login_logs ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER,"
                    + "action_type TEXT,"
                    + "timestamp TEXT" + ");";

            String actionLogs = "CREATE TABLE IF NOT EXISTS action_logs ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER,"
                    + "action_description TEXT,"
                    + "timestamp TEXT" + ");";

            try (java.sql.Statement stmt = conn.createStatement()) {
                stmt.execute(usersTable);
                stmt.execute(devicesTable);
                stmt.execute(loginLogs);
                stmt.execute(actionLogs);
            }

            // Ensure a default admin user exists
            String checkAdmin = "SELECT id FROM users WHERE username = 'admin'";
            try (PreparedStatement ps = conn.prepareStatement(checkAdmin);
                    ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    String insertAdmin = "INSERT INTO users (username,password,role) VALUES (?,?,?)";
                    try (PreparedStatement ins = conn.prepareStatement(insertAdmin)) {
                        ins.setString(1, "admin");
                        ins.setString(2, "admin"); // default password: admin (change in production)
                        ins.setString(3, "admin");
                        ins.executeUpdate();
                    }
                }
            }

            System.out.println("Database initialized.");

        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }
}
