package com.school_management.model;

import com.school_management.dto.RoomType;

public class Room {
    private int roomId;
    private String roomName;
    private RoomType roomType;

    public Room(String roomName, RoomType roomType) {
        this.roomName = roomName;
        this.roomType = roomType;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
}
