package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogDAO {

    public static void insertLoginLog(int userId, String actionType, String timestamp) {
        String sql = "INSERT INTO login_logs (user_id,action_type,timestamp) VALUES (?,?,?)";
        Connection conn = database.DatabaseConnection.connect();
        if (conn == null) {
            System.out.println("Insert login log failed: no database connection");
            return;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, actionType);
            ps.setString(3, timestamp);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Insert login log error: " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void insertActionLog(int userId, String desc, String timestamp) {
        String sql = "INSERT INTO action_logs (user_id,action_description,timestamp) VALUES (?,?,?)";
        Connection conn = database.DatabaseConnection.connect();
        if (conn == null) {
            System.out.println("Insert action log failed: no database connection");
            return;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, desc);
            ps.setString(3, timestamp);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Insert action log error: " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static List<String[]> getLoginLogs() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT l.id, u.username, l.action_type, l.timestamp FROM login_logs l LEFT JOIN users u ON l.user_id = u.id ORDER BY l.id DESC";
        Connection conn = database.DatabaseConnection.connect();
        if (conn == null) {
            System.out.println("Fetch login logs failed: no database connection");
            return list;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String[] row = new String[4];
                row[0] = String.valueOf(rs.getInt(1));
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                list.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Fetch login logs error: " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
        return list;
    }

    /**
     * Get login logs where timestamp starts with the provided date prefix (format
     * MM/dd/yyyy)
     */
    public static List<String[]> getLoginLogsByDatePrefix(String datePrefix) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT l.id, u.username, l.action_type, l.timestamp FROM login_logs l LEFT JOIN users u ON l.user_id = u.id WHERE l.timestamp LIKE ? ORDER BY l.id DESC";
        Connection conn = database.DatabaseConnection.connect();
        if (conn == null) {
            System.out.println("Fetch login logs by date failed: no database connection");
            return list;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, datePrefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] row = new String[4];
                    row[0] = String.valueOf(rs.getInt(1));
                    row[1] = rs.getString(2);
                    row[2] = rs.getString(3);
                    row[3] = rs.getString(4);
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Fetch login logs by date error: " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
        return list;
    }

    /**
     * Get action logs (user actions) joined with usernames
     */
    public static List<String[]> getActionLogs() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT a.id, u.username, a.action_description, a.timestamp FROM action_logs a LEFT JOIN users u ON a.user_id = u.id ORDER BY a.id DESC";
        Connection conn = database.DatabaseConnection.connect();
        if (conn == null) {
            System.out.println("Fetch action logs failed: no database connection");
            return list;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String[] row = new String[4];
                row[0] = String.valueOf(rs.getInt(1));
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                list.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Fetch action logs error: " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
        return list;
    }

    /**
     * Get action logs where timestamp starts with the provided date prefix (format
     * MM/dd/yyyy)
     */
    public static List<String[]> getActionLogsByDatePrefix(String datePrefix) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT a.id, u.username, a.action_description, a.timestamp FROM action_logs a LEFT JOIN users u ON a.user_id = u.id WHERE a.timestamp LIKE ? ORDER BY a.id DESC";
        Connection conn = database.DatabaseConnection.connect();
        if (conn == null) {
            System.out.println("Fetch action logs by date failed: no database connection");
            return list;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, datePrefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] row = new String[4];
                    row[0] = String.valueOf(rs.getInt(1));
                    row[1] = rs.getString(2);
                    row[2] = rs.getString(3);
                    row[3] = rs.getString(4);
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Fetch action logs by date error: " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
        return list;
    }
}
