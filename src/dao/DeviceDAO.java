package dao;

import model.Device;
import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAO {

    /**
     * Fetch all devices from the database.
     *
     * @return list of Device objects (empty list if none or on error)
     */
    public static List<Device> getAll() {
        List<Device> devices = new ArrayList<>();
        String sql = "SELECT * FROM devices ORDER BY id";
        try (Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                devices.add(DaoUtils.mapDevice(rs));
            }
        } catch (SQLException e) {
            System.out.println("Device fetch error: " + e.getMessage());
        }
        return devices;
    }

    /**
     * Insert a new device row into the database.
     *
     * @param d Device to insert (id is ignored)
     * @return true if insertion succeeded, false otherwise
     */
    public static boolean insert(Device d) {
        String sql = "INSERT INTO devices (device_number,type,brand,model,status,location,purchase_date,notes,cost) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, d.getDeviceNumber());
            statement.setString(2, d.getType());
            statement.setString(3, d.getBrand());
            statement.setString(4, d.getModel());
            statement.setString(5, d.getStatus());
            statement.setString(6, d.getLocation());
            statement.setString(7, d.getPurchaseDate());
            statement.setString(8, d.getNotes());
            statement.setDouble(9, d.getCost());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Insert device error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete a device by id.
     *
     * @param id device id to delete
     * @return true if a row was deleted, false otherwise
     */
    public static boolean delete(int id) {
        String sql = "DELETE FROM devices WHERE id = ?";
        try (Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete device error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Load a single Device by its id.
     *
     * @param id device id
     * @return Device object or null if not found
     */
    public static Device getById(int id) {
        String sql = "SELECT * FROM devices WHERE id = ?";
        try (Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return DaoUtils.mapDevice(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Get device by id error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Update an existing device row in the database.
     *
     * @param d Device object with id set and fields to update
     * @return true if update succeeded, false otherwise
     */
    public static boolean update(Device d) {
        String sql = "UPDATE devices SET device_number=?,type=?,brand=?,model=?,status=?,location=?,purchase_date=?,notes=?,cost=? WHERE id=?";
        try (Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, d.getDeviceNumber());
            statement.setString(2, d.getType());
            statement.setString(3, d.getBrand());
            statement.setString(4, d.getModel());
            statement.setString(5, d.getStatus());
            statement.setString(6, d.getLocation());
            statement.setString(7, d.getPurchaseDate());
            statement.setString(8, d.getNotes());
            statement.setDouble(9, d.getCost());
            statement.setInt(10, d.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update device error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Count all device rows.
     *
     * @return total device count
     */
    public static int countAll() {
        String sql = "SELECT COUNT(*) FROM devices";
        try (Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Count all devices error: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Count devices matching a single status (case-insensitive).
     *
     * @param status status string to match (null treated as empty)
     * @return count of matching devices
     */
    public static int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM devices WHERE LOWER(status) = ?";
        try (Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status == null ? "" : status.toLowerCase());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Count by status error: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Count devices whose status is in the provided list (case-insensitive).
     *
     * @param statuses array of status strings
     * @return count of matching devices
     */
    public static int countByStatuses(String[] statuses) {
        if (statuses == null || statuses.length == 0)
            return 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < statuses.length; i++) {
            if (i > 0)
                sb.append(",");
            sb.append("?");
        }
        String sql = "SELECT COUNT(*) FROM devices WHERE LOWER(status) IN (" + sb.toString() + ")";
        try (Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < statuses.length; i++) {
                statement.setString(i + 1, statuses[i].toLowerCase());
            }
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Count by statuses error: " + e.getMessage());
        }
        return 0;
    }
}
