package com.booking.entity;

import java.sql.Timestamp;

/**
 * 预约实体类 —— 对应 reservations 表
 */
public class Reservation {

    private int       id;
    private int       roomId;
    private int       userId;
    private String    roomName;   // 联表查询字段（非数据库列）
    private String    userName;   // 联表查询字段（非数据库列）
    private String    title;
    private Timestamp startTime;
    private Timestamp endTime;
    private int       attendees;
    private String    purpose;
    private String    status;     // "pending", "approved", "rejected", "cancelled"
    private Timestamp createTime;

    public Reservation() {}

    // ========== Getters & Setters ==========

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getAttendees() {
        return attendees;
    }

    public void setAttendees(int attendees) {
        this.attendees = attendees;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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

    // ========== 状态判断辅助方法 ==========

    public boolean isPending() {
        return "pending".equals(this.status);
    }

    public boolean isApproved() {
        return "approved".equals(this.status);
    }

    public boolean canCancel() {
        return "pending".equals(this.status) || "approved".equals(this.status);
    }

    /**
     * 获取状态的中文显示
     */
    public String getStatusText() {
        switch (this.status) {
            case "pending":   return "待审批";
            case "approved":  return "已批准";
            case "rejected":  return "已拒绝";
            case "cancelled": return "已取消";
            default:          return "未知";
        }
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", roomName='" + roomName + '\'' +
                ", userName='" + userName + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
