package com.school_management.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.school_management.dto.DayOfWeekEnum;
import com.school_management.model.ClassSchedule;

public class ClassScheduleDAO {

    private Connection getConnection() throws SQLException {
        // Update this line to use Docker hostname instead of localhost
        String url = "jdbc:mysql://db:3306/school_management?allowPublicKeyRetrieval=true&useSSL=false";
        String username = "root";
        String password = "shared_development_password_2026";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
        return DriverManager.getConnection(url, username, password);
    }

    public boolean hasScheduleConflict(int roomId, String dayOfWeek, Time startTime, Time endTime) {
        String sql = "SELECT COUNT(*) FROM ClassSchedules WHERE room_id = ? AND day_of_week = ? " +
                     "AND ((start_time < ? AND end_time > ?) OR (start_time >= ? AND start_time < ?))";
                     
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            stmt.setString(2, dayOfWeek);
            stmt.setTime(3, endTime);
            stmt.setTime(4, startTime);
            stmt.setTime(5, startTime);
            stmt.setTime(6, endTime);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(ClassSchedule schedule) {
        String sql = "INSERT INTO ClassSchedules (class_id, subject_id, room_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, schedule.getClassId());
            stmt.setInt(2, schedule.getSubjectId());
            stmt.setInt(3, schedule.getRoomId());
            stmt.setString(4, schedule.getDayOfWeek().name());
            stmt.setTime(5, Time.valueOf(schedule.getStartTime()));
            stmt.setTime(6, Time.valueOf(schedule.getEndTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ClassSchedule> findAll() {
        List<ClassSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM ClassSchedules";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
           while (rs.next()) {
    ClassSchedule schedule = new ClassSchedule();
    schedule.setScheduleId(rs.getInt("schedule_id"));
    schedule.setClassId(rs.getInt("class_id"));
    schedule.setSubjectId(rs.getInt("subject_id"));
    schedule.setRoomId(rs.getInt("room_id"));
      schedule.setDayOfWeek(DayOfWeekEnum.valueOf(rs.getString("day_of_week").toUpperCase()));
    
    schedule.setStartTime(rs.getTime("start_time").toLocalTime());
    schedule.setEndTime(rs.getTime("end_time").toLocalTime());
    schedules.add(schedule);
}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }
}