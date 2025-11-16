package dao;

import model.Device;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Small DAO utility helpers to reduce duplication in DAOs.
 */
public final class DaoUtils {
    private DaoUtils() {
    }

    public static Device mapDevice(ResultSet rs) throws SQLException {
        /**
         * Map the current row of a ResultSet to a Device model instance.
         *
         * @param rs ResultSet positioned at a valid row
         * @return populated Device object
         */
        Device device = new Device();
        device.setId(rs.getInt("id"));
        device.setDeviceNumber(rs.getString("device_number"));
        device.setType(rs.getString("type"));
        device.setBrand(rs.getString("brand"));
        device.setModel(rs.getString("model"));
        device.setStatus(rs.getString("status"));
        device.setLocation(rs.getString("location"));
        device.setPurchaseDate(rs.getString("purchase_date"));
        device.setNotes(rs.getString("notes"));
        device.setCost(rs.getDouble("cost"));
        return device;
    }

    public static User mapUser(ResultSet rs) throws SQLException {
        /**
         * Map the current row of a ResultSet to a User model instance.
         *
         * @param rs ResultSet positioned at a valid row
         * @return populated User object
         */
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    }

    public static String[] rowToArray(ResultSet rs, int cols) throws SQLException {
        /**
         * Convert the first {@code cols} columns of the current ResultSet row into
         * a String array. Useful for simple table population code.
         *
         * @param rs   ResultSet positioned at a valid row
         * @param cols number of columns to read
         * @return array of String values from the row
         */
        String[] rowValues = new String[cols];
        for (int i = 0; i < cols; i++) {
            rowValues[i] = rs.getString(i + 1);
        }
        return rowValues;
    }

    public static void closeQuiet(AutoCloseable c) {
        /**
         * Close the given {@link AutoCloseable} quietly, ignoring any thrown exception.
         * This is useful in cleanup paths where closing failures should not
         * interrupt program flow.
         *
         * @param c resource to close (may be null)
         */
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (Exception ignored) {
        }
    }
}
