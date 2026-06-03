package com.school_management.dto;

import java.sql.Date;
import java.time.LocalDate;

public enum FeeStatus {
    PAID,
    UNPAID;


public static FeeStatus resolve(String dbValue, Date dueDate) {
        if ("PAID".equalsIgnoreCase(dbValue)) {
            return PAID;
        }
        // UNPAID — check if past due
        if (dueDate != null && dueDate.toLocalDate().isBefore(LocalDate.now())) {
            return UNPAID;
        }
        return UNPAID;
    }
}