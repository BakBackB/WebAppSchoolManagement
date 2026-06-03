package com.school_management.service;

import java.sql.Time;
import java.util.List;

import com.school_management.dao.ClassScheduleDAO;
import com.school_management.model.ClassSchedule;

public class ClassScheduleService {

    private final ClassScheduleDAO classScheduleRepository = new ClassScheduleDAO();

    public List<ClassSchedule> getAllSchedules() {
        return classScheduleRepository.findAll();
    }

    public String addSchedule(ClassSchedule schedule) {
        if (schedule.getStartTime() == null || schedule.getEndTime() == null) {
            return "Error: Start time and end time cannot be empty.";
        }

        Time startTime = Time.valueOf(schedule.getStartTime());
        Time endTime = Time.valueOf(schedule.getEndTime());

        if (!endTime.after(startTime)) {
            return "Error: End time must be strictly after the start time.";
        }

        if (schedule.getClassId() <= 0 || schedule.getSubjectId() <= 0 || schedule.getRoomId() <= 0) {
            return "Error: Invalid structural reference IDs provided.";
        }

        boolean isConflict = classScheduleRepository.hasScheduleConflict(
            schedule.getRoomId(),
            schedule.getDayOfWeek().name(),
            startTime,
            endTime
        );

        if (isConflict) {
            return "Error: Room collision detected! This room is already occupied during the requested timeslot.";
        }

        classScheduleRepository.save(schedule);
        return "Success";
    }
}