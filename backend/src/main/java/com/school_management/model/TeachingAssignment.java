package com.school_management.model;

public class TeachingAssignment {
    private int assignmentId;
    private int teacherId;
    private int scheduleId;

    public TeachingAssignment() {}

    public TeachingAssignment(int teacherId, int scheduleId) {
        this.teacherId = teacherId;
        this.scheduleId = scheduleId;
    }

    // Getters and Setters
    public int getAssignmentId() { return assignmentId; }
    public void setAssignmentId(int assignmentId) { this.assignmentId = assignmentId; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
}