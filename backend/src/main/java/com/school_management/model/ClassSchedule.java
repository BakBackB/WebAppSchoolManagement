package com.school_management.model;

import java.sql.Time;

public class ClassSchedule {
    private int scheduleId;
    private int classId;
    private int subjectId;
    private int roomId;
    private String dayOfWeek;
    private Time startTime;
    private Time endTime;

    private SchoolClass schoolClass;
    private Subject subject;
    private Room room;
}
