/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.school_management.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.mindrot.jbcrypt.BCrypt;

import com.school_management.model.Role;
import com.school_management.model.User;

/**
 *
 * @author admin
 */
public class UserDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/school_management?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Daigia_minhphuc1511";
    private static final String SQL_AUTH = "SELECT u.*, r.* FROM users u LEFT JOIN roles r ON u.role_id = r.role_id WHERE u.username = ? AND u.is_active = 1";
    private static final String SQL_UPDATE_LOGIN = "UPDATE users SET last_login = NOW() WHERE user_id = ?";
    private static final String SQL_BY_ID = "SELECT u.*, r.* FROM users u LEFT JOIN roles r ON u.role_id = r.role_id WHERE u.user_id = ?";
    private static final String STORE_TOKEN = "INSERT INTO RememberTokens (user_id, token_hash, expires_at) VALUES (?, ?, ?)";
    private static final String VALIDATE_TOKEN = "SELECT user_id FROM RememberTokens WHERE token_hash=? AND expires_at > NOW()";
    private static final String DELETE_TOKEN = "DELETE FROM RememberTokens WHERE token_hash = ?";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found");
        }
    }

    /**
     * Verify username and password. Returns the User object if authentication
     * succeeds; null otherwise.
     *
     * The method never reveals whether the username or the password was wrong —
     * returning null for both cases prevents username enumeration attacks.
     */
    public User authenticate(String username, String password) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL_AUTH)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    // Bcrypt.checkpw extracts the salt from the hash automatically
                    if (BCrypt.checkpw(password, storedHash)) {
                        User user = extractUserFromResultSet(rs);
                        updateLastLogin(user.getUserId()); //update the last time user logged in
                        return user; // authentication success
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; //authentication failed
    }

    public boolean storeTokenToDatabase(int userId, long offsetTime, String tokenHash) throws SQLException {
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + (offsetTime * 1000));
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(STORE_TOKEN);
            stmt.setInt(1, userId);
            stmt.setString(2, tokenHash);
            stmt.setTimestamp(3, expiresAt);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteTokenFromDatabase(String tokenHash) throws SQLException {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(DELETE_TOKEN)) {
            stmt.setString(1, tokenHash);
            return stmt.executeUpdate() > 0;
        }
    }

    public User validateRememberToken(String tokenHash) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(VALIDATE_TOKEN)) {
            stmt.setString(1, tokenHash);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                return getUserbyId(userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; //validation failed
    }

    private void updateLastLogin(int userId) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_LOGIN)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserbyId(int id) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User u = new User();

        Role role = new Role();
        role.setRoleId(rs.getInt("role_id"));
        role.setRoleName(rs.getString("role_name"));
        role.setDescription(rs.getString("description"));
        u.setRole(role);
        
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setEmail(rs.getString("email"));
        u.setActive(rs.getBoolean("is_active"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        u.setLastLogin(rs.getTimestamp("last_login"));
        return u;
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticate("student_bao", "123student");
        System.out.println(user.toString());
    }
}
