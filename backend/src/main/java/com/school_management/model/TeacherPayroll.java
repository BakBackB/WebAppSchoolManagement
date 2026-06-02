package com.school_management.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.school_management.dto.PayrollStatus;

public class TeacherPayroll {

    private Integer payrollId;
    private Teacher teacher;
    private String payPeriod; // Format: "MM-YYYY"
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
        // Khởi tạo giá trị mặc định để tránh lỗi NullPointerException khi tính toán
        this.allowances = BigDecimal.ZERO;
        this.deductions = BigDecimal.ZERO;
        this.status = PayrollStatus.PENDING;
    }
    public TeacherPayroll(Integer payrollId, String payPeriod, String baseSalary, String allowances, String deductions, PayrollStatus status) {
        this.payrollId = payrollId;
        this.payPeriod = payPeriod;
        this.baseSalary = new BigDecimal(baseSalary);
        this.allowances = new BigDecimal(allowances);
        this.deductions = new BigDecimal(deductions);
        this.status = status;
    }
    public TeacherPayroll(Integer payrollId, Teacher teacher, String payPeriod, BigDecimal baseSalary,
            BigDecimal allowances, BigDecimal deductions, BigDecimal netAmount,
            Date paymentDate, PayrollStatus status) {
        this.payrollId = payrollId;
        this.teacher = teacher;
        this.payPeriod = payPeriod;
        this.baseSalary = baseSalary;
        this.allowances = allowances;
        this.deductions = deductions;
        this.netAmount = netAmount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    // --- Getters and Setters ---
    public Integer getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(Integer payrollId) {
        this.payrollId = payrollId;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
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

    @Override
    public String toString() {
        return "TeacherPayroll{" +
                "payrollId=" + payrollId +
                ", teacherId=" + teacher.getTeacherId() +
                ", payPeriod='" + payPeriod + '\'' +
                ", baseSalary=" + baseSalary +
                ", allowances=" + allowances +
                ", deductions=" + deductions +
                ", netAmount=" + netAmount +
                ", paymentDate=" + paymentDate +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}