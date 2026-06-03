package com.school_management.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.school_management.model.SchoolClass;
import com.school_management.model.Student;
import com.school_management.model.User;

public class StudentDAO {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/school_management?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Daigia_minhphuc1511"; // CHANGE THIS
    // SQL queries
    private static final String SELECT_ALL = "SELECT s.*, c.* FROM Students s LEFT JOIN Classes c ON s.class_id = c.class_id";
    private static final String SELECT_BY_ID = "SELECT s.*, c.* FROM Students s LEFT JOIN Classes c ON s.class_id = c.class_id WHERE s.student_id = ?";
    private static final String SELECT_BY_CODE = "SELECT s.*, c.* FROM Students s LEFT JOIN Classes c ON s.class_id = c.class_id WHERE s.student_code = ?";
    // Requires the schema_patch.sql ALTER (student_id column on Users)
    private static final String SELECT_BY_USER_ID = "SELECT s.*, c.* FROM Students s LEFT JOIN Users u ON u.user_id = s.user_id LEFT JOIN Classes c ON s.class_id = c.class_id WHERE u.user_id = ?";
    private static final String SELECT_BY_EMAIL = "SELECT s.*, c.* FROM Students s LEFT JOIN Classes c ON s.class_id = c.class_id WHERE s.email = ?";
    private static final String INSERT_STUDENT = "INSERT INTO Students (student_code, student_name, major, email, class_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_STUDENT = "UPDATE Students SET student_code = ?, student_name = ?, major = ?, email = ?, class_id = ?, user_id = ? WHERE student_id = ?";
    private static final String DELETE_STUDENT = "DELETE FROM Students WHERE student_id = ?";
    private static final String CHECK_USER_EXIST_BY_STUDENT_CODE = "SELECT COUNT(*) FROM Students WHERE student_code = ? AND user_id IS NOT NULL";

    // ── Get database connection ───────────────────────────────────────────────
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // ── Get all students ──────────────────────────────────────────────────────
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        }
        return students;
    }

    // ── Get student by primary key ────────────────────────────────────────────
    public Student getStudentById(int id) throws SQLException {
        Student student = null;
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    student = extractStudentFromResultSet(rs);
                }
            }
        }
        return student;
    }

    // ── Get student by student_code (e.g. "SE001") ────────────────────────────
    public Student getStudentByCode(String studentCode) throws SQLException {
        Student student = null;
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CODE)) {
            stmt.setString(1, studentCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    student = extractStudentFromResultSet(rs);
                }
            }
        }
        return student;
    }

    /**
     * Resolves the Student record for the currently logged-in user.
     * Requires the Users.student_id column added by schema_patch.sql.
     */
    public Student getStudentByUserId(int userId) throws SQLException {
        Student student = null;
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BY_USER_ID)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    student = extractStudentFromResultSet(rs);
                }
            }
        }
        return student;
    }

    public Student getStudentByEmail(String email) throws SQLException {
        Student student = null;
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BY_EMAIL)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    student = extractStudentFromResultSet(rs);
                }
            }
        }
        return student;
    }

    // ── Insert ────────────────────────────────────────────────────────────────
    public boolean insertStudent(Student student) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT_STUDENT)) {
            stmt.setString(1, student.getStudentCode());
            stmt.setString(2, student.getStudentName());
            stmt.setString(3, student.getMajor());
            stmt.setString(4, student.getEmail());
            stmt.setInt(5, student.getClasses().getClassId());
            return stmt.executeUpdate() > 0;
        }
    }

    // ── Update ────────────────────────────────────────────────────────────────
    public boolean updateStudent(Student student) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_STUDENT)) {
            stmt.setString(1, student.getStudentCode());
            stmt.setString(2, student.getStudentName());
            stmt.setString(3, student.getMajor());
            stmt.setString(4, student.getEmail());
            stmt.setInt(5, student.getClasses().getClassId());
            stmt.setInt(6, student.getUser().getUserId());
            stmt.setInt(7, student.getStudentId());
            return stmt.executeUpdate() > 0;
        }
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    public boolean deleteStudent(int id) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(DELETE_STUDENT)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }   

    public boolean isAccountExistByStudentCode(String studentCode) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(CHECK_USER_EXIST_BY_STUDENT_CODE)) {
            stmt.setString(1, studentCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } 
        return false;
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        SchoolClass schoolClass = new SchoolClass();
        User user = new User();
        schoolClass.setClassId(rs.getInt("class_id"));
        schoolClass.setClassName(rs.getString("class_name"));
        schoolClass.setSection(rs.getString("section"));
        student.setClasses(schoolClass);
        
        student.setStudentId(rs.getInt("student_id"));
        student.setStudentCode(rs.getString("student_code"));
        student.setStudentName(rs.getString("student_name"));
        student.setMajor(rs.getString("major"));
        student.setEmail(rs.getString("email"));

        user.setUserId(rs.getInt("user_id"));
        student.setUser(user);
        return student;
    }

    public static void main(String[] args) throws SQLException {
        StudentDAO dao = new StudentDAO();
        System.out.println(dao.getStudentByEmail("nguyen.quan@student.edu.com").toString());
    }
}