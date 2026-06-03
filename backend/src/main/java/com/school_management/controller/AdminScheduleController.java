package com.school_management.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.school_management.dao.DatabaseConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "AdminScheduleServlet", urlPatterns = {"/admin/schedule-dashboard"})
public class AdminScheduleController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Map<String, String>> scheduleRows = new ArrayList<>();
        
        String sql = "SELECT s.schedule_id, c.class_name, c.section, sub.subject_name, r.room_name, s.day_of_week, s.start_time, s.end_time, t.teacher_name " +
                     "FROM ClassSchedules s " +
                     "LEFT JOIN Classes c ON s.class_id = c.class_id " +
                     "LEFT JOIN Subjects sub ON s.subject_id = sub.subject_id " +
                     "LEFT JOIN Rooms r ON s.room_id = r.room_id " +
                     "LEFT JOIN TeachingAssignments ta ON s.class_id = ta.class_id AND s.subject_id = ta.subject_id " +
                     "LEFT JOIN Teachers t ON ta.teacher_id = t.teacher_id " +
                     "ORDER BY s.schedule_id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", rs.getString("schedule_id"));
                row.put("className", rs.getString("class_name") + " (" + rs.getString("section") + ")");
                row.put("subjectName", rs.getString("subject_name"));
                row.put("roomName", rs.getString("room_name"));
                row.put("day", rs.getString("day_of_week"));
                row.put("time", rs.getTime("start_time").toString().substring(0, 5) + " - " + rs.getTime("end_time").toString().substring(0, 5));
                
                String teacher = rs.getString("teacher_name");
                row.put("teacherName", (teacher != null) ? teacher : "🔴 Unassigned (Open to Claim)");
                
                scheduleRows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        request.setAttribute("scheduleRows", scheduleRows);
        request.getRequestDispatcher("/WEB-INF/views/admin-schedule.jsp").forward(request, response);
    }

   
            @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    String action = request.getParameter("action");

    // 🌟 NEW: HANDLE ADMINISTRATIVE REMOVAL
    if ("delete".equals(action)) {
        int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));

        String findMappingSql = "SELECT class_id, subject_id FROM ClassSchedules WHERE schedule_id = ?";
        String deleteAssignmentSql = "DELETE FROM TeachingAssignments WHERE class_id = ? AND subject_id = ?";
        String deleteScheduleSql = "DELETE FROM ClassSchedules WHERE schedule_id = ?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false); // Enable safe transaction loop

            int classId = 0, subjectId = 0;
            
            // 1. Find the target class and subject IDs for this schedule
            try (PreparedStatement ps = conn.prepareStatement(findMappingSql)) {
                ps.setInt(1, scheduleId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        classId = rs.getInt("class_id");
                        subjectId = rs.getInt("subject_id");
                    }
                }
            }

            // 2. Clear out any active teacher claims first to avoid foreign key errors
            if (classId != 0 && subjectId != 0) {
                try (PreparedStatement ps = conn.prepareStatement(deleteAssignmentSql)) {
                    ps.setInt(1, classId);
                    ps.setInt(2, subjectId);
                    ps.executeUpdate();
                }
            }

            // 3. Delete the raw schedule block from the system
            try (PreparedStatement ps = conn.prepareStatement(deleteScheduleSql)) {
                ps.setInt(1, scheduleId);
                ps.executeUpdate();
            }

            conn.commit(); // Commit transaction cleanly
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/admin/schedule-dashboard");
        return;
    }
    
    String subjectName = request.getParameter("subjectName");
    String className = request.getParameter("className");
    String section = request.getParameter("section");
    String roomName = request.getParameter("roomName");
    String dayOfWeek = request.getParameter("dayOfWeek");
    String startTime = request.getParameter("startTime") + ":00";
    String endTime = request.getParameter("endTime") + ":00";

    try (Connection conn = DatabaseConfig.getConnection()) {
        conn.setAutoCommit(false); 
        // 1. 🌟 AUTO-CREATE SUBJECT IF IT DOESN'T EXIST
        int subjectId = 0;
        String subFind = "SELECT subject_id FROM Subjects WHERE subject_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(subFind)) {
            ps.setString(1, subjectName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { subjectId = rs.getInt("subject_id"); }
            }
        }
        if (subjectId == 0) {
            String subIns = "INSERT INTO Subjects (subject_name) VALUES (?)";
            try (PreparedStatement ps = conn.prepareStatement(subIns, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, subjectName);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) subjectId = rs.getInt(1); }
            }
        }

        // 2. 🌟 AUTO-CREATE ROOM IF IT DOESN'T EXIST
        int roomId = 0;
        String roomFind = "SELECT room_id FROM Rooms WHERE room_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(roomFind)) {
            ps.setString(1, roomName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { roomId = rs.getInt("room_id"); }
            }
        }
        if (roomId == 0) {
            String roomIns = "INSERT INTO Rooms (room_name, room_type) VALUES (?, 'CLASSROOM')";
            try (PreparedStatement ps = conn.prepareStatement(roomIns, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, roomName);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) roomId = rs.getInt(1); }
            }
        }

        // 3. 🌟 AUTO-CREATE CLASS IF IT DOESN'T EXIST
        int classId = 0;
        String classFind = "SELECT class_id FROM Classes WHERE class_name = ? AND section = ?";
        try (PreparedStatement ps = conn.prepareStatement(classFind)) {
            ps.setString(1, className);
            ps.setString(2, section);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { classId = rs.getInt("class_id"); }
            }
        }
        if (classId == 0) {
            String classIns = "INSERT INTO Classes (class_name, section, room_id) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(classIns, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, className);
                ps.setString(2, section);
                ps.setInt(3, roomId);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) classId = rs.getInt(1); }
            }
        }

        // 4. 🌟 FINAL STEP: DEPLOY THE TIME SEGMENT TO CLASSSCHEDULES
        String schedIns = "INSERT INTO ClassSchedules (class_id, subject_id, room_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(schedIns)) {
            ps.setInt(1, classId);
            ps.setInt(2, subjectId);
            ps.setInt(3, roomId);
            ps.setString(4, dayOfWeek.toUpperCase());
            ps.setString(5, startTime);
            ps.setString(6, endTime);
            ps.executeUpdate();
        }

        conn.commit(); // Save everything at once cleanly
    } catch (SQLException e) {
        e.printStackTrace();
        request.getSession().setAttribute("dbError", "❌ Time Block Conflict: That room is already booked at this exact time block!");
    }

    response.sendRedirect(request.getContextPath() + "/admin/schedule-dashboard");
}
}