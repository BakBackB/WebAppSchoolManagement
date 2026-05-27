package com.school_management.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.school_management.dto.PayrollStatus;

public class TeacherPayroll {
    
    private Integer payrollId;
    private Integer teacherId; 
    private String payPeriod;  // Format: "MM-YYYY"
    private BigDecimal baseSalary;
    private BigDecimal allowances;
    private BigDecimal deductions;
    private BigDecimal netAmount; // DB auto generate value. In Java, only be used for retriving data by SELECT
    private Date paymentDate;     
    private PayrollStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // --- Constructors ---
    public TeacherPayroll() {
        this.allowances = BigDecimal.ZERO;
        this.deductions = BigDecimal.ZERO;
        this.status = PayrollStatus.PENDING;
    }

    public TeacherPayroll(Integer payrollId, Integer teacherId, String payPeriod, BigDecimal baseSalary,
                          BigDecimal allowances, BigDecimal deductions, BigDecimal netAmount, 
                          Date paymentDate, PayrollStatus status, Timestamp createdAt, Timestamp updatedAt) {
        this.payrollId = payrollId;
        this.teacherId = teacherId;
        this.payPeriod = payPeriod;
        this.baseSalary = baseSalary;
        this.allowances = allowances;
        this.deductions = deductions;
        this.netAmount = netAmount;
        this.paymentDate = paymentDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- Getters and Setters ---
    public Integer getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(Integer payrollId) {
        this.payrollId = payrollId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(String payPeriod) {
        this.payPeriod = payPeriod;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getAllowances() {
        return allowances;
    }

    public void setAllowances(BigDecimal allowances) {
        this.allowances = allowances;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }

    public void setDeductions(BigDecimal deductions) {
        this.deductions = deductions;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PayrollStatus getStatus() {
        return status;
    }

    public void setStatus(PayrollStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
