/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.school_management.dao;

import java.sql.Connection;
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

    

    private static final String SQL_AUTH = "SELECT u.*, r.* FROM users u LEFT JOIN roles r ON u.role_id = r.role_id WHERE u.username = ? AND u.is_active = 1";
    private static final String SQL_UPDATE_LOGIN = "UPDATE users SET last_login = NOW() WHERE user_id = ?";
    private static final String SQL_BY_ID = "SELECT u.*, r.* FROM users u LEFT JOIN roles r ON u.role_id = r.role_id WHERE u.user_id = ?";
    private static final String STORE_TOKEN = "INSERT INTO RememberTokens (user_id, token_hash, expires_at) VALUES (?, ?, ?)";
    private static final String VALIDATE_TOKEN = "SELECT user_id FROM RememberTokens WHERE token_hash=? AND expires_at > NOW()";
    private static final String DELETE_TOKEN = "DELETE FROM RememberTokens WHERE token_hash = ?";
    private static final String INSERT_USER = "INSERT INTO users (username, password_hash, email, role_id, is_active) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_USER_BY_USERNAME = "SELECT u.*, r.* FROM users u LEFT JOIN roles r ON u.role_id = r.role_id WHERE u.username = ?";
    private static final String SQL_CHECK_USERNAME = "SELECT COUNT(*) FROM users WHERE username = ?";
    private static final String SQL_CHECK_EMAIL = "SELECT COUNT(*) FROM users WHERE email = ?";
    
    private static final String SQL_CHECK_TEACHER_CODE = "SELECT COUNT(*) FROM users WHERE teacher_code = ?";

   private Connection getConnection() throws SQLException {
        // Calls your central config class directly from DatabaseConfig, no need to change username and password on every DAO class
        return DatabaseConfig.getConnection(); 
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
                    System.out.println("====== DATABASE DATA INSIDE JAVA ======");
                    System.out.println("Input Username from web page: '" + username + "'");
                    System.out.println("Database Username found: '" + rs.getString("username") + "'");
                    System.out.println("Database Hash retrieved: '" + storedHash + "'");
                    System.out.println("=======================================");
                    // Bcrypt.checkpw extracts the salt from the hash automatically
                    if (BCrypt.checkpw(password, storedHash)) {
                        User user = extractUserFromResultSet(rs);
                        updateLastLogin(user.getUserId()); // update the last time user logged in
                        return user; // authentication success
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // authentication failed
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
        return null; // validation failed
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

    public User getUserByUsername(String username) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        }
        return null;
    }

    // ── Insert
    public boolean insertUser(User user) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT_USER)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getRole().getRoleId());
            stmt.setBoolean(5, true);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean isUsernameExists(String username) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Checks if an email already exists in the database.
     */
    public boolean isEmailExists(String email) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_EMAIL)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
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
        User user = userDAO.validateRememberToken("b25eeacb-1e0e-4f07-bca7-1d2c312eca2e");
        System.out.println(user.toString());
    }
}
