package com.school_management.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.school_management.model.Teacher;

public class TeacherDAO {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/school_management?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Daigia_minhphuc1511"; // CHANGE THIS
    // SQL queries
    private static final String SELECT_ALL = "SELECT * FROM Teachers";
    private static final String SELECT_ALL_TEACHER_ID = "SELECT teacher_id FROM Teachers";
    private static final String SELECT_TEACHER_BY_ID = "SELECT * FROM Teachers WHERE teacher_id = ?";
    private static final String INSERT_TEACHER = "INSERT INTO Teachers (teacher_name, phone, email, salary) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_TEACHER = "UPDATE Teachers SET teacher_name = ?, phone = ?, email = ?, salary = ? WHERE teacher_id = ?";
    private static final String DELETE_TEACHER = "DELETE FROM Teachers WHERE teacher_id = ?";

    // ── Get database connection
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // ── Get all teachers (unsorted, used for CSV export) ─────────────────────
    public List<Teacher> getAllTeachers() throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                teachers.add(extractTeacherFromResultSet(rs));
            }
        }
        return teachers;
    }

    // ── Get teacher by ID ─────────────────────────────────────────────────────
    public Teacher getTeacherById(int id) throws SQLException {
        Teacher teacher = null;
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_TEACHER_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    teacher = extractTeacherFromResultSet(rs);
                }
            }
        }
        return teacher;
    }

    public List<Integer> getTeacherId() throws SQLException {
        List<Integer> teacherIds = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TEACHER_ID);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                teacherIds.add(rs.getInt("teacher_id"));
            }
        }
        return teacherIds;
    }

    // ── Helper
    private Teacher extractTeacherFromResultSet(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(rs.getInt("teacher_id"));
        teacher.setTeacherName(rs.getString("teacher_name"));
        teacher.setPhone(rs.getString("phone"));
        teacher.setEmail(rs.getString("email"));
        teacher.setSalary(rs.getBigDecimal("salary"));
        return teacher;
    }
}