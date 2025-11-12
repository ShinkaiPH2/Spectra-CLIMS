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

    public static List<Device> getAll() {
        List<Device> list = new ArrayList<>();
        String sql = "SELECT * FROM devices ORDER BY id";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Device d = new Device();
                d.setId(rs.getInt("id"));
                d.setDeviceNumber(rs.getString("device_number"));
                d.setType(rs.getString("type"));
                d.setBrand(rs.getString("brand"));
                d.setModel(rs.getString("model"));
                d.setStatus(rs.getString("status"));
                d.setLocation(rs.getString("location"));
                d.setPurchaseDate(rs.getString("purchase_date"));
                d.setNotes(rs.getString("notes"));
                d.setCost(rs.getDouble("cost"));
                list.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Device fetch error: " + e.getMessage());
        }
        return list;
    }

    public static boolean insert(Device d) {
        String sql = "INSERT INTO devices (device_number,type,brand,model,status,location,purchase_date,notes,cost) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getDeviceNumber());
            ps.setString(2, d.getType());
            ps.setString(3, d.getBrand());
            ps.setString(4, d.getModel());
            ps.setString(5, d.getStatus());
            ps.setString(6, d.getLocation());
            ps.setString(7, d.getPurchaseDate());
            ps.setString(8, d.getNotes());
            ps.setDouble(9, d.getCost());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Insert device error: " + e.getMessage());
        }
        return false;
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM devices WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete device error: " + e.getMessage());
        }
        return false;
    }

    public static Device getById(int id) {
        String sql = "SELECT * FROM devices WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Device d = new Device();
                    d.setId(rs.getInt("id"));
                    d.setDeviceNumber(rs.getString("device_number"));
                    d.setType(rs.getString("type"));
                    d.setBrand(rs.getString("brand"));
                    d.setModel(rs.getString("model"));
                    d.setStatus(rs.getString("status"));
                    d.setLocation(rs.getString("location"));
                    d.setPurchaseDate(rs.getString("purchase_date"));
                    d.setNotes(rs.getString("notes"));
                    d.setCost(rs.getDouble("cost"));
                    return d;
                }
            }
        } catch (SQLException e) {
            System.out.println("Get device by id error: " + e.getMessage());
        }
        return null;
    }

    public static boolean update(Device d) {
        String sql = "UPDATE devices SET device_number=?,type=?,brand=?,model=?,status=?,location=?,purchase_date=?,notes=?,cost=? WHERE id=?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getDeviceNumber());
            ps.setString(2, d.getType());
            ps.setString(3, d.getBrand());
            ps.setString(4, d.getModel());
            ps.setString(5, d.getStatus());
            ps.setString(6, d.getLocation());
            ps.setString(7, d.getPurchaseDate());
            ps.setString(8, d.getNotes());
            ps.setDouble(9, d.getCost());
            ps.setInt(10, d.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update device error: " + e.getMessage());
        }
        return false;
    }

    public static int countAll() {
        String sql = "SELECT COUNT(*) FROM devices";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Count all devices error: " + e.getMessage());
        }
        return 0;
    }

    public static int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM devices WHERE LOWER(status) = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status == null ? "" : status.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Count by status error: " + e.getMessage());
        }
        return 0;
    }

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
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < statuses.length; i++)
                ps.setString(i + 1, statuses[i].toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Count by statuses error: " + e.getMessage());
        }
        return 0;
    }
}
