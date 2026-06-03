/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.school_management.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import com.school_management.dao.UserDAO;
import com.school_management.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author admin
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {

    private UserDAO userDAO;
    // private LoginAttemptTracker loginAttemptTracker;
    // private AuditLogger auditLogger;
    // private final String LOGIN_SUCCESS = "User logged in successfully"; // log for successful login
    // private final String LOGIN_FAILURE = "User unsuccessfully log in"; // log for unsuccessful login

    @Override
    public void init() {
        userDAO = new UserDAO();
        // loginAttemptTracker = new LoginAttemptTracker();
        // auditLogger = new AuditLogger();
    }

    /**
     * GET /login — display the login form. If the user already has an
     * authenticated session, skip the form and redirect.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // HttpSession session = request.getSession(false);

        // if (session != null && session.getAttribute("user") != null) {
        //     response.sendRedirect(request.getContextPath() + "/financial-statistic.jsp");
        //     return;
        // }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    /**
     * POST /login — process the login form submission.
     *
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    System.out.println("====== WEB PAGE DATA INSIDE SERVLET ======");
    System.out.println("Collected Username: '" + request.getParameter("username") + "'");
    System.out.println("Collected Password: '" + request.getParameter("password") + "'");
    System.out.println("==========================================");
       String username = request.getParameter("username");
       String password = request.getParameter("password");
    //    String ipAddress = request.getRemoteAddr(); // Get ip address for logging user's action
       // Server-side validation — never rely on the browser alone
       if (username == null || username.isBlank()
               || password == null || password.isBlank()) {
           request.setAttribute("error", "Username and password are required");
           request.setAttribute("username", username); // pre-fill username field
           request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
           return;
       }
    //    if (loginAttemptTracker.isLocked(username)) {
    //        request.setAttribute("error", "Account locked. Please try again in 15 minutes.");
    //        request.setAttribute("username", username); // pre-fill username field
    //        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    //        return;
    //    }
       User user = userDAO.authenticate(username.trim(), password);
       System.out.println(user);
       // After successful authentication
       if (user != null) {
           // Get parameter of RememberMe
           String rememberMe = request.getParameter("remember");
           // Log successful action
        //    log(user.getId(), "LOGIN_SUCCESS", LOGIN_SUCCESS, ipAddress);
           // Reset counter attemps for login limit
        //    loginAttemptTracker.resetAttempts(username);
           // Check if rememberMe is on
           if ("on".equals(rememberMe)) {
               String tokenHash = UUID.randomUUID().toString();
               long thirtyDaysInSec = 30L * 24 * 60 * 60;

               // 1. Store in Database, notice expires time is assigned in UserDAO's function
               try {
                   userDAO.storeTokenToDatabase(user.getUserId(), thirtyDaysInSec, tokenHash);
               } catch (SQLException e) {
                   e.printStackTrace();
               }
               // 2. Store in Cookie
               Cookie cookie = new Cookie("remember_token", tokenHash);
               cookie.setMaxAge((int) thirtyDaysInSec);
               cookie.setHttpOnly(true);
               cookie.setPath("/");
               response.addCookie(cookie);

               // --- Session fixation protection ---
               // If a session exists from before login (anonymous browsing),
               // destroy it so the attacker cannot reuse the pre-login session ID.
               HttpSession oldSession = request.getSession(false);
               if (oldSession != null) {
                   oldSession.invalidate();
               }
           }
           // Create a brand-new session with a fresh, unpredictable ID
           HttpSession session = request.getSession(true);
           String csrfToken = UUID.randomUUID().toString();
           session.setAttribute("user", user);
           session.setAttribute("role", user.getRole().getRoleName());
           session.setAttribute("fullName", user.getUsername());
           session.setAttribute("csrfToken", csrfToken);
           session.setMaxInactiveInterval(5 * 60); // 5 minutes of inactivity
           // Role-based redirect
           String redirect = null; 
           if(user.isAdmin()) {
               redirect = "/payroll";
           } else if(user.isTeacher()) {
               redirect = "/financial-statistics";
           } else if(user.isStudent()) {
               redirect = "/payment";
           }
           response.sendRedirect(request.getContextPath() + redirect);
       } else {
           // Authentication failed — do not say whether username or password was wrong
        //    loginAttemptTracker.recordFailedAttempt(username);
           // Log successful action
        //    log(null, "LOGIN_FAILURE", LOGIN_FAILURE, ipAddress);
           request.setAttribute("error", "Invalid username or password");
           request.setAttribute("username", username); // pre-fill username field
           request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
       }
   }
//
//    private void log(Integer id, String actionType, String description, String ipAddress) {
//        AuditLog log = new AuditLog(id, actionType, description, ipAddress);
//        auditLogger.log(log);
    // }
}
