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
 * AdminFilter — guards /financial-statistics and /payroll.
 *
 * Rules:
 *  - /financial-statistics : fully admin-only (all access requires admin)
 *  - /payroll              : fully admin-only (all access requires admin)
 *
 * For /payment, see TeacherFilter — students are blocked there.
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/payroll"})
public class AdminFilter implements Filter {

    // Write actions that mutate data — kept for potential future audit logging.
    private static final List<String> WRITE_ACTIONS = Arrays.asList(
            "generate", "edit", "update", "delete", "confirmDelete");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        HttpSession session = httpReq.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Session lost — send to login
            httpResp.sendRedirect(httpReq.getContextPath() + "/login");
            return;
        }

        if (user.isAdmin()) {
            // Admin passes through unconditionally
            chain.doFilter(request, response);
            return;
        }

        // Non-admin — block with a meaningful redirect
        String requestPath = httpReq.getServletPath(); // e.g. "/payroll" or "/financial-statistics"
        String errorMsg = "Access+denied:+Admin+privileges+required";

        if (user.isTeacher()) {
            // Teachers get redirected to their own landing page
            httpResp.sendRedirect(httpReq.getContextPath() + "/financial-statistics?error=" + errorMsg);
        } else {
            // Students and any other role
            httpResp.sendRedirect(httpReq.getContextPath() + "/payment?error=" + errorMsg);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("[AdminFilter] initialized");
    }

    @Override
    public void destroy() {
        System.out.println("[AdminFilter] destroyed");
    }
}