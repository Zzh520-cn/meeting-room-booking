package com.booking.entity;

import java.sql.Timestamp;

/**
 * 用户实体类 —— 对应 users 表
 */
public class User {

    private int       id;
    private String    username;
    private String    password;
    private String    realName;
    private String    role;       // "admin" 或 "user"
    private String    phone;
    private String    email;
    private Timestamp createTime;

    public User() {}

    public User(int id, String username, String realName, String role, String phone, String email) {
        this.id       = id;
        this.username = username;
        this.realName = realName;
        this.role     = role;
        this.phone    = phone;
        this.email    = email;
    }

    // ========== Getters & Setters ==========

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public boolean isAdmin() {
        return "admin".equals(this.role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
