package com.school_management.controller;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import com.school_management.dto.DayOfWeekEnum;
import com.school_management.model.ClassSchedule;
import com.school_management.service.ClassScheduleService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ClassScheduleServlet", urlPatterns = {"/schedules"})
public class ClassScheduleController extends HttpServlet {

    private final ClassScheduleService classScheduleService = new ClassScheduleService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<ClassSchedule> schedules = classScheduleService.getAllSchedules();
        request.setAttribute("schedules", schedules);
        request.getRequestDispatcher("/WEB-INF/views/schedule-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int classId = Integer.parseInt(request.getParameter("classId"));
        int subjectId = Integer.parseInt(request.getParameter("subjectId"));
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String dayStr = request.getParameter("dayOfWeek");
        LocalTime start = LocalTime.parse(request.getParameter("startTime"));
        LocalTime end = LocalTime.parse(request.getParameter("endTime"));

       DayOfWeekEnum day = DayOfWeekEnum.valueOf(dayStr.toUpperCase());

        ClassSchedule schedule = new ClassSchedule(classId, subjectId, roomId, day, start, end);
        String result = classScheduleService.addSchedule(schedule);

        if (result.startsWith("Error")) {
            request.setAttribute("errorMessage", result);
            List<ClassSchedule> schedules = classScheduleService.getAllSchedules();
            request.setAttribute("schedules", schedules);
            request.getRequestDispatcher("/WEB-INF/views/schedule-list.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/schedules");
        }
    }
}