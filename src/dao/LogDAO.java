package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogDAO {
    /**
     * Insert a record into the `login_logs` table.
     *
     * @param userId     the id of the user performing the login/logout action
     * @param actionType a short text describing the action (e.g. "LOGIN", "LOGOUT")
     * @param timestamp  the timestamp string to store (expected formatted
     *                   date/time)
     */
    public static void insertLoginLog(int userId, String actionType, String timestamp) {
        String sql = "INSERT INTO login_logs (user_id,action_type,timestamp) VALUES (?,?,?)";
        try (java.sql.Connection connection = database.DatabaseConnection.connect()) {
            if (connection == null) {
                System.out.println("Insert login log failed: no database connection");
                return;
            }
            try (java.sql.PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setString(2, actionType);
                ps.setString(3, timestamp);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Insert login log error: " + e.getMessage());
        }
    }

    /**
     * Insert a record into the `action_logs` table describing a user action.
     *
     * @param userId    the id of the user that performed the action
     * @param desc      a short description of the action performed
     * @param timestamp the timestamp string to store (expected formatted date/time)
     */
    public static void insertActionLog(int userId, String desc, String timestamp) {
        String sql = "INSERT INTO action_logs (user_id,action_description,timestamp) VALUES (?,?,?)";
        try (java.sql.Connection connection = database.DatabaseConnection.connect()) {
            if (connection == null) {
                System.out.println("Insert action log failed: no database connection");
                return;
            }
            try (java.sql.PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setString(2, desc);
                ps.setString(3, timestamp);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Insert action log error: " + e.getMessage());
        }
    }

    /**
     * Fetch all login logs joined with username, newest first.
     *
     * @return a list of String[] rows where each row contains (id, username,
     *         action_type, timestamp)
     */
    public static List<String[]> getLoginLogs() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT l.id, u.username, l.action_type, l.timestamp FROM login_logs l LEFT JOIN users u ON l.user_id = u.id ORDER BY l.id DESC";
        try (java.sql.Connection connection = database.DatabaseConnection.connect()) {
            if (connection == null) {
                System.out.println("Fetch login logs failed: no database connection");
                return list;
            }
            try (java.sql.PreparedStatement ps = connection.prepareStatement(sql);
                    java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(DaoUtils.rowToArray(rs, 4));
                }
            }
        } catch (SQLException e) {
            System.out.println("Fetch login logs error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Get login logs where timestamp starts with the provided date prefix (format
     * MM/dd/yyyy)
     */
    /**
     * Fetch login logs where the timestamp starts with the given date prefix.
     * Commonly used to filter logs by a specific day (prefix format like
     * "MM/dd/yyyy").
     *
     * @param datePrefix a date string prefix to match on the timestamp column
     * @return a list of String[] rows where each row contains (id, username,
     *         action_type, timestamp)
     */
    public static List<String[]> getLoginLogsByDatePrefix(String datePrefix) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT l.id, u.username, l.action_type, l.timestamp FROM login_logs l LEFT JOIN users u ON l.user_id = u.id WHERE l.timestamp LIKE ? ORDER BY l.id DESC";
        try (java.sql.Connection connection = database.DatabaseConnection.connect()) {
            if (connection == null) {
                System.out.println("Fetch login logs by date failed: no database connection");
                return list;
            }
            try (java.sql.PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, datePrefix + "%");
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(DaoUtils.rowToArray(rs, 4));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Fetch login logs by date error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Fetch all action logs (user-generated actions) joined with username, newest
     * first.
     *
     * @return a list of String[] rows where each row contains (id, username,
     *         action_description, timestamp)
     */
    public static List<String[]> getActionLogs() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT a.id, u.username, a.action_description, a.timestamp FROM action_logs a LEFT JOIN users u ON a.user_id = u.id ORDER BY a.id DESC";
        try (java.sql.Connection connection = database.DatabaseConnection.connect()) {
            if (connection == null) {
                System.out.println("Fetch action logs failed: no database connection");
                return list;
            }
            try (java.sql.PreparedStatement ps = connection.prepareStatement(sql);
                    java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(DaoUtils.rowToArray(rs, 4));
                }
            }
        } catch (SQLException e) {
            System.out.println("Fetch action logs error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Fetch action logs where the timestamp starts with the given date prefix.
     * Commonly used to filter logs by a specific day (prefix format like
     * "MM/dd/yyyy").
     *
     * @param datePrefix a date string prefix to match on the timestamp column
     * @return a list of String[] rows where each row contains (id, username,
     *         action_description, timestamp)
     */
    public static List<String[]> getActionLogsByDatePrefix(String datePrefix) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT a.id, u.username, a.action_description, a.timestamp FROM action_logs a LEFT JOIN users u ON a.user_id = u.id WHERE a.timestamp LIKE ? ORDER BY a.id DESC";
        try (java.sql.Connection connection = database.DatabaseConnection.connect()) {
            if (connection == null) {
                System.out.println("Fetch action logs by date failed: no database connection");
                return list;
            }
            try (java.sql.PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, datePrefix + "%");
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(DaoUtils.rowToArray(rs, 4));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Fetch action logs by date error: " + e.getMessage());
        }
        return list;
    }
}
