<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%
    // Get the previous page URL
    String referrer = request.getHeader("referer");

    if (referrer != null && !referrer.isEmpty()) {
        // Send the user back to the page they came from
        response.sendRedirect(referrer);
    } else {
        // Fallback to home page if there is no previous page
        response.sendRedirect(request.getContextPath() + "/login");
    }
%>
