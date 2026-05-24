package com.school_management.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AuditLog {
    private int logId;
    private int userId;
    private String actionType;
    private String description;
    private String ipAddress;
    private Timestamp createdAt;

    private User user;

    public AuditLog(int userId, String actionType, String description, String ipAddress) {
        this.userId = userId;
        this.actionType = actionType;
        this.description = description;
        this.ipAddress = ipAddress;
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
