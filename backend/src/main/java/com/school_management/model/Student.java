package com.school_management.model;

public class Student {
    private int studentId;
    private String studentCode;
    private String studentName;
    private String major;
    private String email;

    private SchoolClass classes;
    private User user;

    public Student(String studentCode, String studentName, String major, String email) {
        this.studentCode = studentCode;
        this.studentName = studentName;
        this.major = major;
        this.email = email;
    }

    public Student() {
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public SchoolClass getClasses() {
        return classes;
    }

    public void setClasses(SchoolClass classes) {
        this.classes = classes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
