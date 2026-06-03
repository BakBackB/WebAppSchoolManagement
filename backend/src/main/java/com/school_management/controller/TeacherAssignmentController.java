package com.school_management.controller;

import java.io.IOException;
import java.util.List;

import com.school_management.dao.TeachingAssignmentDAO;
import com.school_management.model.ClassSchedule;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TeacherAssignmentServlet", urlPatterns = {"/teacher/claim-classes"})
public class TeacherAssignmentController extends HttpServlet {

    private final TeachingAssignmentDAO assignmentDAO = new TeachingAssignmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Integer teacherId = (Integer) request.getSession().getAttribute("userId");
        if (teacherId == null) {
            teacherId = 1; // Demo fallback safety
        }

        // 1. Fetch available open slots
        List<ClassSchedule> openSchedules = assignmentDAO.getUnassignedSchedules();
        request.setAttribute("openSchedules", openSchedules);
        
        List<ClassSchedule> mySchedules = assignmentDAO.getTeacherCurrentSchedules(teacherId);
        request.setAttribute("mySchedules", mySchedules);
        
        request.getRequestDispatcher("/WEB-INF/views/teacher-claim.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        int classId = Integer.parseInt(request.getParameter("classId"));
        int subjectId = Integer.parseInt(request.getParameter("subjectId"));
        
        Integer teacherId = (Integer) request.getSession().getAttribute("userId");
        if (teacherId == null) {
            teacherId = 1;
        }

        if ("drop".equals(action)) {
            assignmentDAO.dropClassBlock(classId, subjectId);
            response.sendRedirect(request.getContextPath() + "/teacher/claim-classes");
            return;
        }

        // Handle Claim Time Check
        List<ClassSchedule> unassigned = assignmentDAO.getUnassignedSchedules();
        ClassSchedule target = null;
        for (ClassSchedule s : unassigned) {
            if (s.getClassId() == classId && s.getSubjectId() == subjectId) {
                target = s;
                break;
            }
        }

        if (target != null) {
            List<ClassSchedule> activeDuties = assignmentDAO.getTeacherCurrentSchedules(teacherId);
            for (ClassSchedule active : activeDuties) {
                if (active.getDayOfWeek() == target.getDayOfWeek()) {
                    if (target.getStartTime().isBefore(active.getEndTime()) && 
                        target.getEndTime().isAfter(active.getStartTime())) {
                        
                        request.setAttribute("errorMessage", "❌ TIME CONFLICT ERROR: This course overlaps with a class block you already claimed!");
                        request.setAttribute("openSchedules", assignmentDAO.getUnassignedSchedules());
                        request.setAttribute("mySchedules", assignmentDAO.getTeacherCurrentSchedules(teacherId));
                        request.getRequestDispatcher("/WEB-INF/views/teacher-claim.jsp").forward(request, response);
                        return;
                    }
                }
            }
        }

        boolean success = assignmentDAO.claimClassBlock(teacherId, classId, subjectId);
        if (!success) {
            request.setAttribute("errorMessage", "Error: That course block has already been claimed!");
        }
        
        response.sendRedirect(request.getContextPath() + "/teacher/claim-classes");
    }
}