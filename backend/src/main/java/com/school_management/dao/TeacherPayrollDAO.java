package com.school_management.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.school_management.dto.PayrollStatus;
import com.school_management.model.Teacher;
import com.school_management.model.TeacherPayroll;

public class TeacherPayrollDAO {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/school_management?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Daigia_minhphuc1511"; // CHANGE THIS
    // SQL queries
    private static final String SELECT_ALL = "SELECT tp.*, t.teacher_name, t.email, t.phone " +
            "FROM TeacherPayroll tp " +
            "JOIN Teachers t ON tp.teacher_id = t.teacher_id";
    private static final String SELECT_TEACHER_PAYROLL_BY_ID = "SELECT tp.*, t.teacher_name, t.email, t.phone " +
            "FROM TeacherPayroll tp " +
            "JOIN Teachers t ON tp.teacher_id = t.teacher_id " +
            "WHERE tp.payroll_id = ?";
    private static final String SELECT_TEACHER_PAYROLL_BY_PAY_PERIOD = "SELECT tp.*, t.teacher_name, t.email, t.phone "
            +
            "FROM TeacherPayroll tp " +
            "JOIN Teachers t ON tp.teacher_id = t.teacher_id " +
            "WHERE tp.pay_period = ?";
    private static final String SELECT_TEACHER_PAYROLL_BY_STATUS = "SELECT tp.*, t.teacher_name, t.email, t.phone " +
            "FROM TeacherPayroll tp " +
            "JOIN Teachers t ON tp.teacher_id = t.teacher_id " +
            "WHERE tp.status = ?";
    private static final String INSERT_TEACHER_PAYROLL = "INSERT INTO TeacherPayroll (teacher_id, pay_period, base_salary) VALUES (?, ?, ?)";
    private static final String UPDATE_TEACHER_PAYROLL = "UPDATE TeacherPayroll SET base_salary = ?, allowances = ?, deductions = ?, status = ? WHERE payroll_id = ?";
    private static final String DELETE_TEACHER_PAYROLL = "DELETE FROM TeacherPayroll WHERE payroll_id = ?";
    private static final String SELECT_TOTAL_PAYROLL = "SELECT SUM(net_amount) FROM TeacherPayroll";
    private static final String COUNT_DISBURSED = "SELECT COUNT(*) FROM TeacherPayroll WHERE status = 'DISBURSED'";
    private static final String COUNT_PENDING = "SELECT COUNT(*) FROM TeacherPayroll WHERE status = 'PENDING'";
    private static final String COUNT_ON_HOLD = "SELECT COUNT(*) FROM TeacherPayroll WHERE status = 'ON_HOLD'";
    private static final String CHECK_PERIOD_EXISTS = "SELECT COUNT(*) FROM TeacherPayroll WHERE pay_period = ?";
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
    public List<TeacherPayroll> getPayrollByPayPeriod(String payPeriod) throws SQLException {
        List<TeacherPayroll> payrolls = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_TEACHER_PAYROLL_BY_PAY_PERIOD)) {
            stmt.setString(1, payPeriod);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    payrolls.add(extractPayrollFromResultSet(rs));
                }
            }
        }
        return payrolls;
    }

    // ── Get Payroll by Status
    public List<TeacherPayroll> getPayrollByStatus(PayrollStatus status) throws SQLException {
        List<TeacherPayroll> payrolls = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_TEACHER_PAYROLL_BY_STATUS)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    payrolls.add(extractPayrollFromResultSet(rs));
                }
            }
        }
        return payrolls;
    }

    // ── Insert
    public boolean insertPayroll(TeacherPayroll payroll, Teacher teacher) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT_TEACHER_PAYROLL)) {
            stmt.setInt(1, teacher.getTeacherId());
            stmt.setString(2, payroll.getPayPeriod());
            stmt.setBigDecimal(3, teacher.getSalary());
            return stmt.executeUpdate() > 0;
        }
    }

    // ── Update
    public boolean updatePayroll(TeacherPayroll payroll) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_TEACHER_PAYROLL)) {
            stmt.setBigDecimal(1, payroll.getBaseSalary());
            stmt.setBigDecimal(2, payroll.getAllowances());
            stmt.setBigDecimal(3, payroll.getDeductions());
            stmt.setString(4, payroll.getStatus().name());
            stmt.setInt(5, payroll.getPayrollId());
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

    public Integer countDisbursed() throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(COUNT_DISBURSED)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public Integer countPending() throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(COUNT_PENDING)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public Integer countOnHold() throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(COUNT_ON_HOLD)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public Integer getTotalPayroll() throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_TOTAL_PAYROLL)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean periodAlreadyGenerated(String payPeriod) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(CHECK_PERIOD_EXISTS)) {
            stmt.setString(1, payPeriod);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    // ── Helper
    private TeacherPayroll extractPayrollFromResultSet(ResultSet rs) throws SQLException {
        TeacherPayroll payroll = new TeacherPayroll();

        // Map Teacher sub-object
        Teacher teacher = new Teacher();
        teacher.setTeacherId(rs.getInt("teacher_id"));
        teacher.setTeacherName(rs.getString("teacher_name"));
        teacher.setEmail(rs.getString("email"));
        teacher.setPhone(rs.getString("phone"));
        payroll.setTeacher(teacher); // attach it

        // Map TeacherPayroll fields
        payroll.setPayrollId(rs.getInt("payroll_id"));
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

    public static void main (String[] args) throws SQLException {
        TeacherPayrollDAO dao = new TeacherPayrollDAO();
        TeacherPayroll teacherPayroll = dao.getPayrollById(6);
        System.out.println(teacherPayroll.toString());
    }
}