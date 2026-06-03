package com.school_management.controller;

import java.io.IOException;
import java.util.List;

import com.school_management.model.Subject;
import com.school_management.service.SubjectService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SubjectServlet", urlPatterns = {"/subjects"})
public class SubjectController extends HttpServlet {

    private final SubjectService subjectService = new SubjectService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Subject> subjects = subjectService.getAllSubjects();
        request.setAttribute("subjects", subjects);
        request.getRequestDispatcher("/WEB-INF/views/subject-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String name = request.getParameter("subjectName");

        Subject subject = new Subject(name);
        String result = subjectService.registerSubject(subject);

        if (result.startsWith("Error")) {
            request.setAttribute("errorMessage", result);
            List<Subject> subjects = subjectService.getAllSubjects();
            request.setAttribute("subjects", subjects);
            request.getRequestDispatcher("/WEB-INF/views/subject-list.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/subjects");
        }
    }
}