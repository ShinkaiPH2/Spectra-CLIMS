import database.DatabaseInit;
import ui.views.LoginUI;

public class Main {
    public static void main(String[] args) {
        // Ensure database file and tables exist
        // open+close a connection to ensure DB file exists, then initialize schema
        try (java.sql.Connection c = database.DatabaseConnection.connect()) {
            // no-op; just ensure the file/connection can be created
        } catch (Exception ignored) {
        }
        DatabaseInit.initialize();

        // Launch login UI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}
