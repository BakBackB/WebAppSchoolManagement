package com.school_management.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.school_management.dto.FeeStatus;
import com.school_management.model.Student;
import com.school_management.model.StudentFee;

public class StudentFeeDAO {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/school_management?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "yourpassword"; // CHANGE THIS
    // SQL queries
    private static final String SELECT_ALL = "SELECT sf.*, s.student_code, s.student_name, s.major, s.class_id " +
            "FROM StudentFees sf " +
            "JOIN Students s ON sf.student_id = s.student_id";
    private static final String SELECT_BY_ID = "SELECT sf.*, s.student_code, s.student_name, s.major, s.class_id " +
            "FROM StudentFees sf " +
            "JOIN Students s ON sf.student_id = s.student_id " +
            "WHERE sf.fee_id = ?";
    private static final String SELECT_BY_STUDENT_ID = "SELECT sf.*, s.student_code, s.student_name, s.major, s.class_id "
            +
            "FROM StudentFees sf " +
            "JOIN Students s ON sf.student_id = s.student_id " +
            "WHERE sf.student_id = ? " +
            "ORDER BY sf.due_date DESC";
    private static final String INSERT_FEE = "INSERT INTO StudentFees (student_id, amount, due_date, status) VALUES (?, ?, ?, ?)";
    // Payment: set status='PAID' and record the payment_date
    private static final String PAY_FEE = "UPDATE StudentFees SET status = 'PAID', payment_date = CURDATE() WHERE fee_id = ? AND student_id = ?";
    private static final String DELETE_FEE = "DELETE FROM StudentFees WHERE fee_id = ?";
    private static final String COUNT_UNPAID_BY_STUDENT_ID = "SELECT COUNT(*) FROM StudentFees WHERE student_id = ? AND status = 'UNPAID'";
    private static final String SUM_UNPAID_BY_STUDENT_ID = "SELECT COALESCE(SUM(amount), 0) FROM StudentFees WHERE student_id = ? AND status = 'UNPAID'";

    // ── Get database connection ───────────────────────────────────────────────
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // ── Get all fees (admin use) ──────────────────────────────────────────────
    public List<StudentFee> getAllFees() throws SQLException {
        List<StudentFee> fees = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                fees.add(extractFeeFromResultSet(rs));
            }
        }
        return fees;
    }

    // ── Get a single fee by ID ────────────────────────────────────────────────
    public StudentFee getFeeById(int feeId) throws SQLException {
        StudentFee fee = null;
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, feeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    fee = extractFeeFromResultSet(rs);
                }
            }
        }
        return fee;
    }

    // ── Get all fees for one student (the portal list view) ───────────────────
    public List<StudentFee> getFeesByStudentId(int studentId) throws SQLException {
        List<StudentFee> fees = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BY_STUDENT_ID)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fees.add(extractFeeFromResultSet(rs));
                }
            }
        }
        return fees;
    }

    // ── Insert a new fee record ───────────────────────────────────────────────
    public boolean insertFee(StudentFee fee) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT_FEE)) {
            stmt.setInt(1, fee.getStudent().getStudentId());
            stmt.setBigDecimal(2, fee.getAmount());
            stmt.setDate(3, fee.getDueDate());
            stmt.setString(4, fee.getStatus().name());
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Mark a specific fee as paid. The student_id guard ensures a student
     * can only pay their own fees (defence-in-depth alongside the filter).
     */
    public boolean payFee(int feeId, int studentId) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(PAY_FEE)) {
            stmt.setInt(1, feeId);
            stmt.setInt(2, studentId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ── Delete a fee record ───────────────────────────────────────────────────
    public boolean deleteFee(int feeId) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(DELETE_FEE)) {
            stmt.setInt(1, feeId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ── Count unpaid fees for a student (used for the portal header) ──────────
    public int countUnpaidByStudent(int studentId) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(COUNT_UNPAID_BY_STUDENT_ID)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        }
        return 0;
    }

    // ── Total outstanding amount for a student (portal header debt figure) ────
    public BigDecimal sumUnpaidByStudent(int studentId) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SUM_UNPAID_BY_STUDENT_ID)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getBigDecimal(1);
            }
        }
        return java.math.BigDecimal.ZERO;
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private StudentFee extractFeeFromResultSet(ResultSet rs) throws SQLException {
        StudentFee fee = new StudentFee();
        Student student = new Student();
        // Map Student sub-object
        student.setStudentId(rs.getInt("student_id"));
        student.setStudentCode(rs.getString("student_code"));
        student.setStudentName(rs.getString("student_name"));
        student.setMajor(rs.getString("major"));
        fee.setStudent(student);

        // Map StudentFee fields
        fee.setFeeId(rs.getInt("fee_id"));
        fee.setAmount(rs.getBigDecimal("amount"));
        fee.setDueDate(rs.getDate("due_date"));
        fee.setPaymentDate(rs.getDate("payment_date"));

        // Resolve effective status: PAID | UNPAID |
        fee.setStatus(FeeStatus.resolve(rs.getString("status"), rs.getDate("due_date")));

        return fee;
    }
}
