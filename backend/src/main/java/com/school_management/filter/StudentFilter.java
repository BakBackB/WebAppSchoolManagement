/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.school_management.filter;

import java.io.IOException;

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
 * AuthFilter — intercepts every request. Requests to public paths are allowed
 * through unconditionally. Requests to protected paths require an authenticated
 * session.
 */
@WebFilter(filterName = "StudentFilter", urlPatterns = {"/payment"})
public class StudentFilter implements Filter {
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
            httpResp.sendRedirect(httpReq.getContextPath() + "/list-payments");
        }
        if (user.isStudent()) {
            // Student passes through unconditionally
            chain.doFilter(request, response);
            return;
        }

        // Non-admin — block with a meaningful redirect
        String requestPath = httpReq.getServletPath(); // e.g. "/payroll" or "/financial-statistics"
        String errorMsg = "Access+denied:+Admin+or+Student+privileges+required";

        if (user.isTeacher()) {
            // Teachers get redirected to their own landing page
            httpResp.sendRedirect(httpReq.getContextPath() + "/financial-statistics?error=" + errorMsg);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("[StudentFilter] initialized");
    }

    @Override
    public void destroy() {
        System.out.println("[StudentFilter] destroyed");
    }
}
