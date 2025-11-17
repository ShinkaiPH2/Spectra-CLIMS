package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection class to manage SQLite database connections.
 */

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:lib/database/CLIMS_Database.sqlite";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return connection;
    }
}
