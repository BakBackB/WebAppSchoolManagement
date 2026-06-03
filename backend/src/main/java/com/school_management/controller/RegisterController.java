/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.school_management.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import com.school_management.dao.StudentDAO;
import com.school_management.dao.TeacherDAO;
import com.school_management.dao.UserDAO;
import com.school_management.model.Role;
import com.school_management.model.Student;
import com.school_management.model.Teacher;
import com.school_management.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author admin
 */
@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private UserDAO userDAO;
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    // private LoginAttemptTracker loginAttemptTracker;
    // private AuditLogger auditLogger;
    // private final String LOGIN_SUCCESS = "User logged in successfully"; // log
    // for successful login
    // private final String LOGIN_FAILURE = "User unsuccessfully log in"; // log for
    // unsuccessful login

    @Override
    public void init() {
        userDAO = new UserDAO();
        studentDAO = new StudentDAO();
        teacherDAO = new TeacherDAO();
        // loginAttemptTracker = new LoginAttemptTracker();
        // auditLogger = new AuditLogger();
    }

    /**
     * GET /login — display the login form. If the user already has an
     * authenticated session, skip the form and redirect.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // HttpSession session = request.getSession(false);

        // if (session != null && session.getAttribute("user") != null) {
        // response.sendRedirect(request.getContextPath() + "/financial-statistic.jsp");
        // return;
        // }
        request.getRequestDispatcher("/WEB-INF/views/registration-form.jsp").forward(request, response);
    }

    /**
     * POST /login — process the login form submission.
     *
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!validateForm(request)) {
            // Validation failed
            returnInputBack(request);
            request.getRequestDispatcher("/WEB-INF/views/registration-form.jsp").forward(request, response);
            return;
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        int roleId = Integer.parseInt(request.getParameter("role"));
        String code = checkRoleForCode(roleId, request);
        String email = request.getParameter("email");
        try {
            insertUserToDatabase(username, hashPassword, roleId, email, code);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User user = userDAO.authenticate(username.trim(), password);
        // After successful authentication
        if (user != null) {
            // Create a brand-new session with a fresh, unpredictable ID
            HttpSession session = request.getSession(true);
            String csrfToken = UUID.randomUUID().toString();
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole().getRoleName());
            session.setAttribute("fullName", user.getUsername());
            session.setAttribute("csrfToken", csrfToken);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes of inactivity
            // Role-based redirect
            String redirect = null;
            if (user.isAdmin()) {
                redirect = "/payroll";
            } else if (user.isTeacher()) {
                redirect = "/financial-statistics";
            } else if (user.isStudent()) {
                redirect = "/payment";
            }
            response.sendRedirect(request.getContextPath() + redirect);
        } else {
            // Authentication failed — do not say whether username or password was wrong
            // loginAttemptTracker.recordFailedAttempt(username);
            // Log successful action
            // log(null, "LOGIN_FAILURE", LOGIN_FAILURE, ipAddress);
            request.setAttribute("error", "Invalid username or password");
            request.setAttribute("username", username); // pre-fill username field
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    private String checkRoleForCode(int roleId, HttpServletRequest request) {
        switch (roleId) {
            case 2:
                return request.getParameter("teacherCode");
            case 3:
                return request.getParameter("studentCode");
            default:
                return null;
        }
    }

    // private void log(int id, String actionType, String description, String
    // ipAddress) {
    // AuditLog log = new AuditLog(id, actionType, description, ipAddress);
    // auditLogger.log(log);
    // }

    private boolean validateForm(HttpServletRequest request) {
        boolean isValid = true;

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm");
        String roleStr = request.getParameter("role");
        int roleId = Integer.parseInt(roleStr);
        String email = request.getParameter("email");
        String agreeLicense = request.getParameter("license");

        // Validate Username
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("errorUsername", "Username is required.");
            isValid = false;
        } else {
            try {
                if (userDAO.isUsernameExists(username.trim())) {
                    request.setAttribute("errorUsername", "Username is already taken.");
                    isValid = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorUsername", "Database error verification failed.");
                isValid = false;
            }
        }

        // Validate Email
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("errorEmail", "Email is required.");
            isValid = false;
        } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            request.setAttribute("errorEmail", "Please enter a valid email address.");
            isValid = false;
        } else {
            try {
                if (userDAO.isEmailExists(email.trim())) {
                    request.setAttribute("errorEmail", "Email is already registered.");
                    isValid = false;
                }
                if (roleId == 3) {
                    if (studentDAO.getStudentByEmail(email.trim()) == null) {
                        request.setAttribute("errorEmail", "Email is not associated with any student account. Please contact admin for help.");
                        isValid = false;
                    }
                }
                if (roleId == 2) {
                    if (teacherDAO.getTeacherByEmail(email.trim()) == null) {
                        request.setAttribute("errorEmail", "Email is not associated with any teacher account. Please contact admin for help.");
                        isValid = false;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorEmail", "Database error verification failed.");
                isValid = false;
            }
        }

        // Validate Password
        if (password == null || password.isEmpty()) {
            request.setAttribute("errorPassword", "Password is required.");
            isValid = false;
        } else if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[_*!@#$%^&()\\-+=]).{8,30}$")) {
            request.setAttribute("errorPassword",
                    "Password must be 8-30 characters and include letters, numbers, and special characters _*!@#$%^&()\\-+=).");
            isValid = false;
        }

        // Validate Confirm Password
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            request.setAttribute("errorConfirmPassword", "Please confirm your password.");
            isValid = false;
        } else if (!confirmPassword.equals(password)) {
            request.setAttribute("errorConfirmPassword", "Passwords do not match.");
            isValid = false;
        }

        // Validate Role & Role-Specific Codes
        if (roleStr == null || roleStr.trim().isEmpty()) {
            request.setAttribute("errorRole", "Please select a role.");
            isValid = false;
        } else {
            try {
                // Validate Student Code
                if (roleId == 3) {
                    String studentCode = request.getParameter("studentCode");
                    if (studentCode == null || !studentCode.matches("^[A-Z]{2}\\d{3}$")) {
                        request.setAttribute("errorStudentCode",
                                "Student code must be 2 uppercase letters followed by 3 digits (e.g., IT070).");
                        isValid = false;
                    }
                    if (studentCode != null) {
                        if (studentDAO.isAccountExistByStudentCode(studentCode.trim())) {
                            request.setAttribute("errorStudentCode", "This account has already been registered with this student code. Please contact admin for help.");
                            isValid = false;
                        }
                        if (studentDAO.getStudentByCode(studentCode) == null) {
                            request.setAttribute("errorStudentCode", "Invalid student code or need to be a student of the school to register. Please contact admin for help.");
                            isValid = false;
                        }
                    }
                }
                // Validate Teacher Code
                else if (roleId == 2) {
                    String teacherCode = request.getParameter("teacherCode");
                    if (teacherCode == null || !teacherCode.matches("^T[A-Z]{2}\\d{3}$")) {
                        request.setAttribute("errorTeacherCode",
                                "Teacher code must be T followed by 2 uppercase letters and 3 digits (e.g., TIT367).");
                        isValid = false;
                    }
                    if (teacherCode != null) {
                        if (teacherDAO.isAccountExistByTeacherCode(teacherCode.trim())) {
                            request.setAttribute("errorTeacherCode", "Teacher code is already registered. Please contact admin for help.");
                            isValid = false;
                        }
                        if (teacherDAO.getTeacherByCode(teacherCode) == null) {
                            request.setAttribute("errorTeacherCode", "Invalid teacher code or need to be a teacher of the school to register. Please contact admin for help.");
                            isValid = false;
                        }
                    } 
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorRole", "Invalid role selection.");
                isValid = false;
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorRole", "Database error occurred.");
                isValid = false;
            }
        }

        // Validate License Acceptance
        if (agreeLicense == null || !agreeLicense.equals("on")) {
            request.setAttribute("errorLicense", "You must accept the terms and conditions to proceed.");
            isValid = false;
        }

        return isValid;
    }

    private void insertUserToDatabase(String username, String hashPassword, int roleId, String email, String code)
            throws SQLException {
        User user = new User();
        Role role = new Role();
        role.setRoleId(roleId);
        user.setRole(role);
        user.setUsername(username);
        user.setPasswordHash(hashPassword);
        user.setEmail(email);
        userDAO.insertUser(user);
        User addedUser = userDAO.getUserByUsername(username);
        linkUser(addedUser, code, role);
    }

    private void linkUser(User user, String code, Role role) throws SQLException {
        if (role.getRoleId() == 3) {
            Student student = studentDAO.getStudentByCode(code);
            if (student != null) {
                student.setUser(user);
                studentDAO.updateStudent(student);
            }
        } else if (role.getRoleId() == 2) {
            Teacher teacher = teacherDAO.getTeacherByCode(code);
            if (teacher != null) {
                teacher.setUser(user);
                teacherDAO.updateTeacher(teacher);
            }
        }
    }

    private void returnInputBack(HttpServletRequest request) {
        request.setAttribute("username", request.getParameter("username"));
        request.setAttribute("password", request.getParameter("password"));
        request.setAttribute("confirmPassword", request.getParameter("confirmPassword"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("teacherCode", request.getParameter("teacherCode"));
        request.setAttribute("studentCode", request.getParameter("studentCode"));
        request.setAttribute("role", request.getParameter("role"));
    }

}
