package com.school_management.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.school_management.dao.StudentDAO;
import com.school_management.dao.StudentFeeDAO;
import com.school_management.model.Student;
import com.school_management.model.StudentFee;
import com.school_management.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/payment")
public class PaymentController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StudentDAO studentDAO;
    private StudentFeeDAO studentFeeDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
        studentFeeDAO = new StudentFeeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        try {
            switch (action) {
                case "list":
                    listFees(request, response);
                    break;
                default:
                    listFees(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("payment");
            return;
        }
        try {
            switch (action) {
                case "pay":
                    payFee(request, response);
                    break;
                default:
                    response.sendRedirect("payment");
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    // ── List all fees for the logged-in student ───────────────────────────────
    private void listFees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        Student student = resolveStudent(request);
        if (student == null) {
            // User account is not linked to a Student record
            request.setAttribute("flashError", "Your account is not linked to a student record. Please contact admin.");
            request.getRequestDispatcher("/WEB-INF/views/fee-payment-portal.jsp").forward(request, response);
            return;
        }

        List<StudentFee> fees = studentFeeDAO.getFeesByStudentId(student.getStudentId());
        BigDecimal totalDebt = studentFeeDAO.sumUnpaidByStudent(student.getStudentId());

        // Carry flash message from redirect (e.g. after a successful payment)
        HttpSession session = request.getSession();
        String flashSuccess = (String) session.getAttribute("flashSuccess");
        if (flashSuccess != null) {
            request.setAttribute("flashSuccess", flashSuccess);
            session.removeAttribute("flashSuccess");
        }
        String flashError = (String) session.getAttribute("flashError");
        if (flashError != null) {
            request.setAttribute("flashError", flashError);
            session.removeAttribute("flashError");
        }

        request.setAttribute("student", student);
        request.setAttribute("fees", fees);
        request.setAttribute("totalDebt", totalDebt);
        request.getRequestDispatcher("/WEB-INF/views/fee-payment-portal.jsp").forward(request, response);
    }

    // ── Mark a single fee as paid ─────────────────────────────────────────────
    private void payFee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        Student student = resolveStudent(request);
        if (student == null) {
            response.sendRedirect("payment?error=Session+expired.+Please+log+in+again.");
            return;
        }

        String feeIdParam = request.getParameter("feeId");
        if (feeIdParam == null || feeIdParam.trim().isEmpty()) {
            request.getSession().setAttribute("flashError", "Invalid fee ID.");
            response.sendRedirect("payment");
            return;
        }

        int feeId;
        try {
            feeId = Integer.parseInt(feeIdParam);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("flashError", "Invalid fee ID.");
            response.sendRedirect("payment");
            return;
        }

        // studentId guard inside payFee ensures a student cannot pay another student's
        // fee
        boolean success = studentFeeDAO.payFee(feeId, student.getStudentId());
        if (success) {
            request.getSession().setAttribute("flashSuccess", "Payment submitted successfully.");
        } else {
            request.getSession().setAttribute("flashError",
                    "Payment failed. The fee may already be paid or not found.");
        }
        response.sendRedirect("payment");
    }

    // ── Resolve the Student record for the current session user ───────────────
    private Student resolveStudent(HttpServletRequest request) throws SQLException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null; // No student here
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return null;
        }
        return studentDAO.getStudentByUserId(user.getUserId());
    }
}