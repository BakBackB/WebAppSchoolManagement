/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.school_management.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.school_management.dao.UserDAO;
import com.school_management.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * AuthFilter — intercepts every request. Requests to public paths are allowed
 * through unconditionally. Requests to protected paths require an authenticated
 * session.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = { "/*" })
public class AuthFilter implements Filter {
    // Paths that must be accessible without a session
    private UserDAO userDAO = new UserDAO();
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/register",
            "/login",
            "/logout");
    // File extensions that must never require authentication
    private static final List<String> PUBLIC_EXTENSIONS = Arrays.asList(
            ".css", ".js", ".png", ".jpg", ".jpeg", ".gif", ".ico", ".woff", ".woff2");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("[" + LocalDateTime.now() + "]" + "[AuthFilter used here]");
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        // Strip context path to get the path within the application
        String contextPath = httpReq.getContextPath();
        String requestURI = httpReq.getRequestURI();
        String path = requestURI.substring(contextPath.length());
        // Static resources and public pages pass through immediately
        if (isPublic(path)) {
            chain.doFilter(request, response);
            return;
        }
        // All other paths require authentication
        HttpSession session = httpReq.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("user") != null;
        // If not logged in, or session null, or user null, then
        if (!loggedIn) {
            Cookie[] cookies = httpReq.getCookies();
            String rememberTokenHash = null;
            String csrfTokenHash = null;
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("remember_token".equals(c.getName())) {
                        rememberTokenHash = c.getValue();
                        break;
                    }
                }
                for (Cookie c : cookies) {
                    if ("csrf_token".equals(c.getName())) {
                        csrfTokenHash = c.getValue();
                        break;
                    }
                }
            }
            if (rememberTokenHash != null) /* If the user has clicked "Remember Me" before, then */ {
                User user = userDAO.validateRememberToken(rememberTokenHash); // Logic: SELECT user_id FROM
                                                                              // RememberTokens WHERE
                // token=? AND expires_at > NOW()
                if (user != null) {
                    // Auto-populate session
                    httpReq.getSession().setAttribute("user", user);
                    httpReq.getSession().setAttribute("role", user.getRole().getRoleName());
                    redirectRoleBased(user, httpResp, contextPath);
                } else {
                    httpResp.sendRedirect(contextPath + "/login");
                }
            } else {
                // Preserve the originally requested URL so the user can be
                // redirected back after a successful login (optional enhancement)
                httpResp.sendRedirect(contextPath + "/login");
            }

        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isPublic(String path) {
        // Check exact public paths (e.g., /login, /logout)
        for (String pub : PUBLIC_PATHS) {
            if (path.startsWith(pub)) {
                return true;
            }
        }
        // Check static file extensions
        for (String ext : PUBLIC_EXTENSIONS) {
            if (path.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    private void redirectRoleBased(User user, HttpServletResponse httpResp, String contextPath) throws IOException {
        if (user.isAdmin()) {
            httpResp.sendRedirect(contextPath + "/payroll");
        } else if (user.isTeacher()) {
            httpResp.sendRedirect(contextPath + "/financial-statistics");
        } else {
            httpResp.sendRedirect(contextPath + "/payment");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("[AuthFilter] initialized");
    }

    @Override
    public void destroy() {
        System.out.println("[AuthFilter] destroyed");
    }
}
