package com.school_management.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.school_management.dto.DayOfWeekEnum;
import com.school_management.model.ClassSchedule;

public class TeachingAssignmentDAO {

    private Connection getConnection() throws SQLException {
        return DatabaseConfig.getConnection(); 
    }

   public List<ClassSchedule> getUnassignedSchedules() {
    List<ClassSchedule> list = new ArrayList<>();
    String sql = "SELECT s.*, sub.subject_name FROM ClassSchedules s " +
                 "LEFT JOIN Subjects sub ON s.subject_id = sub.subject_id " +
                 "LEFT JOIN TeachingAssignments t ON s.class_id = t.class_id AND s.subject_id = t.subject_id " +
                 "WHERE t.teacher_id IS NULL";

    try (Connection conn = getConnection(); 
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            ClassSchedule sch = new ClassSchedule();
            sch.setScheduleId(rs.getInt("schedule_id"));
            sch.setClassId(rs.getInt("class_id"));
            sch.setSubjectId(rs.getInt("subject_id"));
            sch.setSubjectName(rs.getString("subject_name"));
            sch.setRoomId(rs.getInt("room_id"));
            
            String dayStr = rs.getString("day_of_week");
            sch.setDayOfWeek(DayOfWeekEnum.valueOf(dayStr.toUpperCase()));
            
            sch.setStartTime(rs.getTime("start_time").toLocalTime());
            sch.setEndTime(rs.getTime("end_time").toLocalTime());
            
            list.add(sch);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

   
    public boolean claimClassBlock(int teacherId, int classId, int subjectId) {
        String sql = "INSERT INTO TeachingAssignments (class_id, subject_id, teacher_id) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, classId);
            ps.setInt(2, subjectId);
            ps.setInt(3, teacherId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
public List<ClassSchedule> getTeacherCurrentSchedules(int teacherId) {
    List<ClassSchedule> list = new ArrayList<>();
    String sql = "SELECT s.*, sub.subject_name FROM ClassSchedules s " +
                 "JOIN TeachingAssignments t ON s.class_id = t.class_id AND s.subject_id = t.subject_id " +
                 "LEFT JOIN Subjects sub ON s.subject_id = sub.subject_id " +
                 "WHERE t.teacher_id = ?";
                 
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, teacherId);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClassSchedule sch = new ClassSchedule();
                sch.setScheduleId(rs.getInt("schedule_id"));
                sch.setClassId(rs.getInt("class_id"));
                sch.setSubjectId(rs.getInt("subject_id"));
                sch.setSubjectName(rs.getString("subject_name"));
                sch.setRoomId(rs.getInt("room_id"));
                
                String dayStr = rs.getString("day_of_week");
                sch.setDayOfWeek(DayOfWeekEnum.valueOf(dayStr.toUpperCase()));
                
                sch.setStartTime(rs.getTime("start_time").toLocalTime());
                sch.setEndTime(rs.getTime("end_time").toLocalTime());
                
                list.add(sch);
            }
        }
    } catch (SQLException e) { 
        e.printStackTrace(); 
    }
    return list;
}


public boolean dropClassBlock(int classId, int subjectId) {
    String sql = "DELETE FROM TeachingAssignments WHERE class_id = ? AND subject_id = ?";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, classId);
        ps.setInt(2, subjectId);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
// 🌟 Fetches EVERY schedule row in the system with all joined metadata attributes
public List<com.school_management.model.ClassSchedule> getAllSchedulesWithMetadata() {
    List<com.school_management.model.ClassSchedule> list = new ArrayList<>();
    String sql = "SELECT s.schedule_id, s.class_id, s.subject_id, s.room_id, s.day_of_week, s.start_time, s.end_time, " +
                 "c.class_name, c.section, sub.subject_name, r.room_name, t.teacher_name " +
                 "FROM ClassSchedules s " +
                 "LEFT JOIN Classes c ON s.class_id = c.class_id " +
                 "LEFT JOIN Subjects sub ON s.subject_id = sub.subject_id " +
                 "LEFT JOIN Rooms r ON s.room_id = r.room_id " +
                 "LEFT JOIN TeachingAssignments ta ON s.class_id = ta.class_id AND s.subject_id = ta.subject_id " +
                 "LEFT JOIN Teachers t ON ta.teacher_id = t.teacher_id";

    try (Connection conn = getConnection(); 
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            com.school_management.model.ClassSchedule sch = new com.school_management.model.ClassSchedule();
            sch.setScheduleId(rs.getInt("schedule_id"));
            sch.setClassId(rs.getInt("class_id"));
            sch.setSubjectId(rs.getInt("subject_id"));
            sch.setRoomId(rs.getInt("room_id"));
            sch.setDayOfWeek(com.school_management.dto.DayOfWeekEnum.valueOf(rs.getString("day_of_week").toUpperCase()));
            sch.setStartTime(rs.getTime("start_time").toLocalTime());
            sch.setEndTime(rs.getTime("end_time").toLocalTime());
            
           

            list.add(sch);
        }
    } catch (Exception e) { e.printStackTrace(); }
    return list;
}
}