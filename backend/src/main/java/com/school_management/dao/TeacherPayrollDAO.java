package com.school_management.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.school_management.dto.PayrollStatus;
import com.school_management.model.TeacherPayroll;

public class TeacherPayrollDAO {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/school_management?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password"; // CHANGE THIS
    // SQL queries
    private static final String SELECT_ALL = "SELECT * FROM TeacherPayroll";
    private static final String SELECT_TEACHER_PAYROLL_BY_ID = "SELECT * FROM TeacherPayroll WHERE teacher_id = ?";
    private static final String SELECT_TEACHER_PAYROLL_BY_PAY_PERIOD = "SELECT * FROM TeacherPayroll WHERE pay_period = ?";
    private static final String SELECT_TEACHER_PAYROLL_BY_STATUS = "SELECT * FROM TeacherPayroll WHERE status = ?";
    private static final String INSERT_TEACHER_PAYROLL = "INSERT INTO TeacherPayroll (teacher_id, pay_period, base_salary) VALUES (?, ?, ?)";
    private static final String UPDATE_TEACHER_PAYROLL = "UPDATE TeacherPayroll SET pay_period = ?, base_salary = ?, allowances = ?, deductions = ?, status = ? WHERE teacher_id = ?";
    private static final String DELETE_TEACHER_PAYROLL = "DELETE FROM TeacherPayroll WHERE teacher_id = ?";

    // ── Get database connection
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // ── Get all payrolls (unsorted, used for CSV export) ─────────────────────
    public List<TeacherPayroll> getAllPayroll() throws SQLException {
        List<TeacherPayroll> payrolls = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                payrolls.add(extractPayrollFromResultSet(rs));
            }
        }
        return payrolls;
    }

    // ── Get Payroll by ID
    public TeacherPayroll getPayrollById(int id) throws SQLException {
        TeacherPayroll payroll = null;
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_TEACHER_PAYROLL_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    payroll = extractPayrollFromResultSet(rs);
                }
            }
        }
        return payroll;
    }

    // ── Get Payroll by PayPeriod
    public TeacherPayroll getPayrollByPayPeriod(String payPeriod) throws SQLException {
        TeacherPayroll payroll = null;
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_TEACHER_PAYROLL_BY_PAY_PERIOD)) {
            stmt.setString(1, payPeriod);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    payroll = extractPayrollFromResultSet(rs);
                }
            }
        }
        return payroll;
    }

    // ── Get Payroll by Status
    public TeacherPayroll getPayrollByStatus(PayrollStatus status) throws SQLException {
        TeacherPayroll payroll = null;
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_TEACHER_PAYROLL_BY_STATUS)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    payroll = extractPayrollFromResultSet(rs);
                }
            }
        }
        return payroll;
    }

    // ── Insert
    public boolean insertPayroll(TeacherPayroll payroll) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT_TEACHER_PAYROLL)) {
            stmt.setInt(1, payroll.getTeacherId());
            stmt.setString(2, payroll.getPayPeriod());
            stmt.setBigDecimal(3, payroll.getBaseSalary());
            return stmt.executeUpdate() > 0;
        }
    }

    // ── Update
    public boolean updatePayroll(TeacherPayroll payroll) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_TEACHER_PAYROLL)) {
            stmt.setString(1, payroll.getPayPeriod());
            stmt.setBigDecimal(2, payroll.getBaseSalary());
            stmt.setBigDecimal(3, payroll.getAllowances());
            stmt.setBigDecimal(4, payroll.getDeductions());
            stmt.setString(5, payroll.getStatus().name());
            stmt.setInt(6, payroll.getTeacherId());
            return stmt.executeUpdate() > 0;
        }
    }

    // ── Delete (single)
    public boolean deletePayroll(int id) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(DELETE_TEACHER_PAYROLL)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // ── Helper
    private TeacherPayroll extractPayrollFromResultSet(ResultSet rs) throws SQLException {
        TeacherPayroll payroll = new TeacherPayroll();
        payroll.setTeacherId(rs.getInt("teacher_id"));
        payroll.setPayPeriod(rs.getString("pay_period"));
        payroll.setBaseSalary(rs.getBigDecimal("base_salary"));
        payroll.setAllowances(rs.getBigDecimal("allowances"));
        payroll.setDeductions(rs.getBigDecimal("deductions"));
        payroll.setNetAmount(rs.getBigDecimal("net_amount"));
        payroll.setPaymentDate(rs.getDate("payment_date"));
        payroll.setStatus(PayrollStatus.valueOf(rs.getString("status")));
        payroll.setCreatedAt(rs.getTimestamp("created_at"));
        payroll.setUpdatedAt(rs.getTimestamp("updated_at"));
        return payroll;
    }
}