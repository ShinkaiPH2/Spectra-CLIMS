package com.spectra_clims;

import database.DatabaseInit;
import ui.views.LoginUI;

/**
 * Application entry point for the CLIMS desktop application.
 * Responsible for ensuring the database is initialized and launching the
 * Swing login UI on the Event Dispatch Thread.
 */
public class Main {
    /**
     * Application entry point.
     * Ensures the database file and schema exist, then launches the login UI on the
     * EDT.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        // Ensure database file and tables exist
        // open+close a connection to ensure DB file is created, then initialize schema
        try (java.sql.Connection connection = database.DatabaseConnection.connect()) {
            // no-op; opening/closing the connection ensures the DB file is created
        } catch (Exception ignored) {
        }
        DatabaseInit.initialize();

        // Launch login UI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}
