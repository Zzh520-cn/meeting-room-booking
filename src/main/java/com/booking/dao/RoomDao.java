package com.booking.dao;

import com.booking.entity.Room;
import com.booking.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 会议室数据访问层 —— 操作 rooms 表
 */
public class RoomDao {

    /**
     * 查询所有会议室
     */
    public List<Room> findAll() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询所有可用会议室
     */
    public List<Room> findAvailable() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = 'available' ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据 ID 查找会议室
     */
    public Room findById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增会议室
     */
    public int insert(Room room) {
        String sql = "INSERT INTO rooms (name, capacity, equipment, location, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, room.getName());
            ps.setInt(2, room.getCapacity());
            ps.setString(3, room.getEquipment());
            ps.setString(4, room.getLocation());
            ps.setString(5, room.getStatus() != null ? room.getStatus() : "available");

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 更新会议室信息
     */
    public boolean update(Room room) {
        String sql = "UPDATE rooms SET name=?, capacity=?, equipment=?, location=?, status=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, room.getName());
            ps.setInt(2, room.getCapacity());
            ps.setString(3, room.getEquipment());
            ps.setString(4, room.getLocation());
            ps.setString(5, room.getStatus());
            ps.setInt(6, room.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除会议室（因为有外键约束，关联的预约也会被级联删除）
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ========== 工具方法 ==========

    private Room mapRow(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getInt("id"));
        room.setName(rs.getString("name"));
        room.setCapacity(rs.getInt("capacity"));
        room.setEquipment(rs.getString("equipment"));
        room.setLocation(rs.getString("location"));
        room.setStatus(rs.getString("status"));
        room.setCreateTime(rs.getTimestamp("create_time"));
        return room;
    }
}
