package dao;

import model.User;

public class UserDAO {

    public static User authenticate(String username, String password) {
        String sql = "SELECT id,username,password,role FROM users WHERE username = ? AND password = ?";
        try (java.sql.Connection connection = database.DatabaseConnection.connect();
                java.sql.PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);
            try (java.sql.ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return DaoUtils.mapUser(rs);
                }
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Authentication error: " + e.getMessage());
        }
        return null;
    }
}
