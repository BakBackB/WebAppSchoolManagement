package com.school_management.model;

import java.math.BigDecimal;
import java.sql.Date;

import com.school_management.dto.FeeStatus;

public class StudentFee {
    private int feeId;
    private BigDecimal amount;
    private Date dueDate;
    private Date paymentDate;
    private FeeStatus status;

    private Student student;

    public StudentFee() {
        this.status = FeeStatus.UNPAID;
    }

    public StudentFee(int studentId, BigDecimal amount, Date dueDate, FeeStatus status) {
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = status;
    }

    public int getFeeId() {
        return feeId;
    }

    public void setFeeId(int feeId) {
        this.feeId = feeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public FeeStatus getStatus() {
        return status;
    }

    public void setStatus(FeeStatus status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "StudentFee{" +
                "feeId=" + feeId +
                ", studentId=" + student.getStudentId() +
                ", amount=" + amount +
                ", dueDate=" + dueDate +
                ", paymentDate=" + paymentDate +
                ", status=" + status +
                '}';
    }
}
