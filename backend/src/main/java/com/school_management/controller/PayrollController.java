package com.school_management.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.school_management.dao.TeacherDAO;
import com.school_management.dao.TeacherPayrollDAO;
import com.school_management.dto.PayrollStatus;
import com.school_management.model.Teacher;
import com.school_management.model.TeacherPayroll;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/payroll")
public class PayrollController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TeacherDAO teacherDAO;
    private TeacherPayrollDAO teacherPayrollDAO;
    private TeacherPayroll teacherPayroll;

    @Override
    public void init() {
        teacherDAO = new TeacherDAO();
        teacherPayrollDAO = new TeacherPayrollDAO();
        teacherPayroll = new TeacherPayroll();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle GET requests for payment-related operations
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        LocalDate current = LocalDate.now();

        request.setAttribute("currentTime", current.getMonth().name() + " " + current.getYear());
        request.setAttribute("currentPeriod", String.format("%d-%02d", current.getYear(), current.getMonthValue()));
        try {
            switch (action) {
                case "generate":
                    generatePayroll(request, response);
                    break;
                case "confirmDelete":
                    confirmDelete(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    listPayroll(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "update":
                    updatePayroll(request, response);
                    break;
                case "delete":
                    deletePayroll(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }

    }

    private void generatePayroll(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {

        LocalDate now = LocalDate.now();
        String period = String.format("%d-%02d", now.getYear(), now.getMonthValue());
        Date defaultPaymentDate = Date.valueOf(now.withDayOfMonth(10));
        if (teacherPayrollDAO.periodAlreadyGenerated(period)) {
            // Don't generate — just redirect back with a message
            request.getSession().setAttribute("flashError", "Payroll for " + period + " has already been generated.");
            response.sendRedirect("payroll?period=" + period);
            return;
        }

        teacherPayroll.setPayPeriod(period);
        teacherPayroll.setPaymentDate(defaultPaymentDate);
        List<Teacher> teachers = teacherDAO.getAllTeachers();
        for (Teacher teacher : teachers) {
            teacherPayrollDAO.insertPayroll(teacherPayroll, teacher);
        }
        response.sendRedirect("payroll?period=" + period);
    }

    private void deletePayroll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int payrollId = Integer.parseInt(request.getParameter("payrollId"));
        String period = request.getParameter("period");
        if (period == null || period.isEmpty()) {
            LocalDate now = LocalDate.now();
            period = String.format("%d-%02d", now.getYear(), now.getMonthValue());
        }
        teacherPayrollDAO.deletePayroll(payrollId);
        response.sendRedirect("payroll?period=" + period);
    }

    private void listPayroll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String period = request.getParameter("period");
        if (period == null || period.isEmpty()) {
            LocalDate now = LocalDate.now();
            period = String.format("%d-%02d", now.getYear(), now.getMonthValue());
        }
        List<TeacherPayroll> payrolls = teacherPayrollDAO.getPayrollByPayPeriod(period);

        HttpSession session = request.getSession();
        String flashError = (String) session.getAttribute("flashError");
        if (flashError != null) {
            request.setAttribute("flashError", flashError);
            session.removeAttribute("flashError");
        }

        countStatus(request);
        request.setAttribute("payrolls", payrolls);
        request.setAttribute("selectedPeriod", period);
        request.getRequestDispatcher("/WEB-INF/views/salary-payroll-dashboard.jsp")
                .forward(request, response);
    }

    private void countStatus(HttpServletRequest request) throws SQLException {
        request.setAttribute("totalPayroll", teacherPayrollDAO.getTotalPayroll());
        request.setAttribute("disbursedCount", teacherPayrollDAO.countDisbursed());
        request.setAttribute("pendingCount", teacherPayrollDAO.countPending());
        request.setAttribute("onHoldCount", teacherPayrollDAO.countOnHold());
    }

    private void confirmDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int payrollId = Integer.parseInt(request.getParameter("payrollId"));
        TeacherPayroll teacherPayroll = teacherPayrollDAO.getPayrollById(payrollId);
        String period = teacherPayroll.getPayPeriod();
        request.setAttribute("payroll", teacherPayroll);
        request.setAttribute("period", period);
        request.getRequestDispatcher("/WEB-INF/views/confirm-delete-payroll.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int payrollId = Integer.parseInt(request.getParameter("payrollId"));
        TeacherPayroll teacherPayroll = teacherPayrollDAO.getPayrollById(payrollId);
        request.setAttribute("payroll", teacherPayroll);
        request.setAttribute("period", teacherPayroll.getPayPeriod());
        request.getRequestDispatcher("/WEB-INF/views/payroll-form.jsp").forward(request, response);
    }

    private void updatePayroll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        if (!validateForm(request)) {
            // Validation failed
            int payrollId = Integer.parseInt(request.getParameter("payrollId"));
            TeacherPayroll teacherPayroll = teacherPayrollDAO.getPayrollById(payrollId);

            request.setAttribute("payroll", teacherPayroll);

            request.getRequestDispatcher("/WEB-INF/views/payroll-form.jsp").forward(request, response);
            return;
        }

        int payrollId = Integer.parseInt(request.getParameter("payrollId"));
        String period = request.getParameter("period");
        String baseSalary = request.getParameter("baseSalary");
        String allowances = request.getParameter("allowances");
        String deductions = request.getParameter("deductions");
        PayrollStatus status = PayrollStatus.valueOf(request.getParameter("status"));
        TeacherPayroll teacherPayroll = new TeacherPayroll(payrollId, period, baseSalary, allowances, deductions,
                status);
        teacherPayrollDAO.updatePayroll(teacherPayroll);
        response.sendRedirect("payroll?period=" + period);
    }

    private boolean validateForm(HttpServletRequest request) {
        boolean isValid = true;

        String baseSalary = request.getParameter("baseSalary");
        String allowances = request.getParameter("allowances");
        String deductions = request.getParameter("deductions");
        String status = request.getParameter("status");

        // Validate Base Salary
        if (baseSalary == null || baseSalary.trim().isEmpty()) {
            request.setAttribute("errorBaseSalary", "Base salary is required.");
            isValid = false;
        } else {
            try {
                double bs = Double.parseDouble(baseSalary);
                if (bs < 0) {
                    request.setAttribute("errorBaseSalary", "Base salary cannot be negative.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorBaseSalary", "Base salary must be a valid numeric value.");
                isValid = false;
            }
        }

        // Validate Allowances
        if (allowances == null || allowances.trim().isEmpty()) {
            request.setAttribute("errorAllowances", "Allowances amount is required.");
            isValid = false;
        } else {
            try {
                double al = Double.parseDouble(allowances);
                if (al < 0) {
                    request.setAttribute("errorAllowances", "Allowances cannot be negative.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorAllowances", "Allowances must be a valid numeric value.");
                isValid = false;
            }
        }

        // Validate Deductions
        if (deductions == null || deductions.trim().isEmpty()) {
            request.setAttribute("errorDeductions", "Deductions amount is required.");
            isValid = false;
        } else {
            try {
                double ded = Double.parseDouble(deductions);
                if (ded < 0) {
                    request.setAttribute("errorDeductions", "Deductions cannot be negative.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorDeductions", "Deductions must be a valid numeric value.");
                isValid = false;
            }
        }

        // Validate Status
        if (status == null || status.trim().isEmpty()) {
            request.setAttribute("errorStatus", "Payroll status is required.");
            isValid = false;
        } else {
            try {
                PayrollStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                request.setAttribute("errorStatus", "Please select a valid status.");
                isValid = false;
            }
        }

        return isValid;
    }
}