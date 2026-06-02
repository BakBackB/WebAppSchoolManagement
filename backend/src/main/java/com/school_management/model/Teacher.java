package com.school_management.model;

import java.math.BigDecimal;

public class Teacher {
    private int teacherId;
    private String teacherName;
    private String phone;
    private String email;
    private BigDecimal salary;

    // No-arg constructor
    public Teacher() {
    }

    public Teacher(String teacherName, String phone, String email, BigDecimal salary) {
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
}
