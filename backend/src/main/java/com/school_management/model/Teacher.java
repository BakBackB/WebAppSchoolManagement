package com.school_management.model;

import java.math.BigDecimal;

public class Teacher {
    private int teacherId;
    private String teacherCode;
    private String teacherName;
    private String phone;
    private String email;
    private BigDecimal salary;

    private User user;
    // No-arg constructor
    public Teacher() {
    }

    public Teacher(String teacherCode, String teacherName, String phone, String email, BigDecimal salary) {
        this.teacherCode = teacherCode;
        this.teacherName = teacherName;
        this.phone = phone;
        this.email = email;
        this.salary = salary;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
