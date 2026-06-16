package com.booking.entity;

import java.sql.Timestamp;

/**
 * 会议室实体类 —— 对应 rooms 表
 */
public class Room {

    private int       id;
    private String    name;
    private int       capacity;
    private String    equipment;
    private String    location;
    private String    status;     // "available" 或 "maintenance"
    private Timestamp createTime;

    public Room() {}

    public Room(int id, String name, int capacity, String equipment, String location, String status) {
        this.id        = id;
        this.name      = name;
        this.capacity  = capacity;
        this.equipment = equipment;
        this.location  = location;
        this.status    = status;
    }

    // ========== Getters & Setters ==========

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public boolean isAvailable() {
        return "available".equals(this.status);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", status='" + status + '\'' +
                '}';
    }
}
