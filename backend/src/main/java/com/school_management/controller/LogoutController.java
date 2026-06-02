/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.school_management.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.school_management.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    
    private UserDAO userDAO;
    // private AuditLogger auditLogger;
    // private final String LOG_OUT = "User unsuccessfully log in";
    

    @Override
    public void init() {
        userDAO = new UserDAO();
        // auditLogger = new AuditLogger();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Redirect to login, passing a success message as a query parameter
        response.sendRedirect(request.getContextPath() + "/login?message=You+have+been+logged+out");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // getSession(false) returns null if no session exists — avoids creating one
        HttpSession session = request.getSession(false);
        String sessionLogOutToken = (String) session.getAttribute("csrfToken");
        String requestLogOutToken = request.getParameter("csrfToken");
        String ipAddress = request.getRemoteAddr(); // Get ip address for logging user's action
        // 1. Delete from Database
        String tokenHash = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("remember_token".equals(c.getName())) {
                    tokenHash = c.getValue();
                    // 2. Clear Cookie
                    c.setMaxAge(0);
                    c.setPath("/");
                    response.addCookie(c);
                }
            }
        }

        if (tokenHash != null) {
            try {
                userDAO.deleteTokenFromDatabase(tokenHash);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (sessionLogOutToken != null && sessionLogOutToken.equals(requestLogOutToken)) {
            // User user = (User) session.getAttribute("user");
            // Token valid: Process logging out
            // log(user.getUserId(), "LOG_OUT", LOG_OUT, ipAddress);
            session.invalidate(); // Clear session
            response.sendRedirect(request.getContextPath() + "/login?message=You+have+been+logged+out");
        } else {
            // Token invalid: Reject request
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF token invalid");
        }
    }
    // private void log(int id, String actionType, String description, String ipAddress) {
    //     AuditLog log = new AuditLog(id, actionType, description, ipAddress);
    //     auditLogger.log(log);
    // }
}
