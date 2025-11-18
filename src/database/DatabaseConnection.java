package database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection class to manage SQLite database connections.
 * Ensures database directory and file exist before attempting connection.
 */
public class DatabaseConnection {
    private static final String DB_RELATIVE_PATH = "lib/database/CLIMS_Database.sqlite";
    private static final String DB_URL = "jdbc:sqlite:" + DB_RELATIVE_PATH;

    private static void ensureDatabaseFile() {
        File dbFile = new File(DB_RELATIVE_PATH);
        File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                System.err.println("Failed to create database directory: " + parentDir.getAbsolutePath());
            }
        }
        // SQLite will create the file automatically when connecting if directory
        // exists.
        // Optional explicit creation to surface IO errors earlier.
        if (!dbFile.exists()) {
            try {
                boolean newFile = dbFile.createNewFile();
                if (!newFile) {
                    System.err
                            .println("Database file was not created (may already exist): " + dbFile.getAbsolutePath());
                }
            } catch (IOException e) {
                System.err.println("Unable to create database file: " + e.getMessage());
            }
        }
    }

    public static Connection connect() {
        ensureDatabaseFile();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return connection;
    }
}
