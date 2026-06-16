package com.booking.dao;

import com.booking.entity.Reservation;
import com.booking.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 预约数据访问层 —— 操作 reservations 表
 */
public class ReservationDao {

    /**
     * 查询所有预约（联表带会议室名和用户名）
     */
    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.*, rm.name AS room_name, u.real_name AS user_name " +
                     "FROM reservations r " +
                     "JOIN rooms rm ON r.room_id = rm.id " +
                     "JOIN users u ON r.user_id = u.id " +
                     "ORDER BY r.create_time DESC";
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
     * 查询某用户的所有预约
     */
    public List<Reservation> findByUserId(int userId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.*, rm.name AS room_name, u.real_name AS user_name " +
                     "FROM reservations r " +
                     "JOIN rooms rm ON r.room_id = rm.id " +
                     "JOIN users u ON r.user_id = u.id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.start_time DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询待审批的预约（管理员审批用）
     */
    public List<Reservation> findPending() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.*, rm.name AS room_name, u.real_name AS user_name " +
                     "FROM reservations r " +
                     "JOIN rooms rm ON r.room_id = rm.id " +
                     "JOIN users u ON r.user_id = u.id " +
                     "WHERE r.status = 'pending' " +
                     "ORDER BY r.start_time ASC";
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
     * 根据 ID 查找单条预约
     */
    public Reservation findById(int id) {
        String sql = "SELECT r.*, rm.name AS room_name, u.real_name AS user_name " +
                     "FROM reservations r " +
                     "JOIN rooms rm ON r.room_id = rm.id " +
                     "JOIN users u ON r.user_id = u.id " +
                     "WHERE r.id = ?";
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
     * 新增预约（状态默认为 pending）
     */
    public int insert(Reservation res) {
        String sql = "INSERT INTO reservations (room_id, user_id, title, start_time, end_time, attendees, purpose, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'pending')";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, res.getRoomId());
            ps.setInt(2, res.getUserId());
            ps.setString(3, res.getTitle());
            ps.setTimestamp(4, res.getStartTime());
            ps.setTimestamp(5, res.getEndTime());
            ps.setInt(6, res.getAttendees());
            ps.setString(7, res.getPurpose());

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
     * 更新预约状态（审批/取消用）
     */
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检查时间冲突 —— 同一会议室在目标时间段内是否已有「已批准」的预约
     * @return true 表示存在冲突
     */
    public boolean hasConflict(int roomId, Timestamp startTime, Timestamp endTime) {
        return hasConflict(roomId, startTime, endTime, 0);
    }

    /**
     * 检查时间冲突（排除指定预约 ID，用于修改场景）
     */
    public boolean hasConflict(int roomId, Timestamp startTime, Timestamp endTime, int excludeId) {
        String sql = "SELECT COUNT(*) FROM reservations " +
                     "WHERE room_id = ? " +
                     "AND status = 'approved' " +
                     "AND id != ? " +
                     "AND NOT (end_time <= ? OR start_time >= ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ps.setInt(2, excludeId);
            ps.setTimestamp(3, startTime);
            ps.setTimestamp(4, endTime);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 出错时不阻塞，让上层决定
    }

    // ========== 工具方法 ==========

    private Reservation mapRow(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setId(rs.getInt("id"));
        r.setRoomId(rs.getInt("room_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setRoomName(rs.getString("room_name"));
        r.setUserName(rs.getString("user_name"));
        r.setTitle(rs.getString("title"));
        r.setStartTime(rs.getTimestamp("start_time"));
        r.setEndTime(rs.getTimestamp("end_time"));
        r.setAttendees(rs.getInt("attendees"));
        r.setPurpose(rs.getString("purpose"));
        r.setStatus(rs.getString("status"));
        r.setCreateTime(rs.getTimestamp("create_time"));
        return r;
    }
}
