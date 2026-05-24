package com.school_management.model;

public class TeachingAssignment {
    private int assignmentId;
    private int classId;
    private int subjectId;
    private int teacherId;

    private SchoolClass schoolClass;
    private Subject subject;
    private Teacher teacher;
    public int getAssignmentId() {
        return assignmentId;
    }
    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }
    public int getClassId() {
        return classId;
    }
    public void setClassId(int classId) {
        this.classId = classId;
    }
    public int getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
    public int getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
    public SchoolClass getSchoolClass() {
        return schoolClass;
    }
    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }
    public Subject getSubject() {
        return subject;
    }
    public void setSubject(Subject subject) {
        this.subject = subject;
    }
    public Teacher getTeacher() {
        return teacher;
    }
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
