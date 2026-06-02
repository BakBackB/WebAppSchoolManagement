/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.school_management.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.school_management.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * AdminFilter — applied only to /student requests. Certain actions (create,
 * update, delete) require the admin role. Read-only actions (list, view,
 * search) are available to all authenticated users.
 */
@WebFilter(filterName = "TeacherFilter", urlPatterns = { "/payment", "/payroll" })
public class TeacherFilter implements Filter {
    // private AuditLogger auditLogger = new AuditLogger();
    // private final String UNAUTHORIZED = "User try to: ";
    private static final List<String> WRITE_ACTIONS = Arrays.asList(
            "generate", "edit", "update", "delete", "confirmDelete");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        String action = httpReq.getParameter("action");
        // String ipAddress = request.getRemoteAddr(); // Get ip address for logging user's action
        if (!isWriteAction(action)) {
            // Read-only action — allow any authenticated user (AuthFilter already
            // verified that a session exists, so no need to check again)
            chain.doFilter(request, response);
            return;
        }
        // Write action — must be admin
        HttpSession session = httpReq.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user != null) {
            if (user.isTeacher() || user.isAdmin()) {
                chain.doFilter(request, response);
            } else if (user.isStudent()) {
                // log(user.getUserId(), "UNAUTHORIZED: " + action, UNAUTHORIZED + action,
                // ipAddress);
                // Block and redirect with an error message
                String redirectUrl = httpReq.getContextPath()
                        + "/payment?error=Access+denied:+teacher+privileges+required";
                httpResp.sendRedirect(redirectUrl);
            }
        } else {
            httpResp.sendRedirect(httpReq.getContextPath() + "/login");
        }
    }

    private boolean isWriteAction(String action) {
        return action != null && WRITE_ACTIONS.contains(action);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("[AdminFilter] initialized");
    }

    @Override
    public void destroy() {
        System.out.println("[AdminFilter] destroyed");
    }

    // private void log(Integer id, String actionType, String description, String ipAddress) {
    //     AuditLog log = new AuditLog(id, actionType, description, ipAddress);
    //     auditLogger.log(log);
    // }
}
