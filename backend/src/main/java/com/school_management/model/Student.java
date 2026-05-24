package com.school_management.model;

public class Student {
    private int studentId;
    private String rollNo;
    private String studentName;
    private int classId;

    private SchoolClass classes;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public SchoolClass getClasses() {
        return classes;
    }

    public void setClasses(SchoolClass classes) {
        this.classes = classes;
    }
}
