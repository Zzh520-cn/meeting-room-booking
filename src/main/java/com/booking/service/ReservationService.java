package com.booking.service;

import com.booking.dao.ReservationDao;
import com.booking.entity.Reservation;
import com.booking.entity.Room;

import java.sql.Timestamp;
import java.util.List;

/**
 * 预约业务逻辑层
 */
public class ReservationService {

    private final ReservationDao reservationDao = new ReservationDao();
    private final RoomService     roomService     = new RoomService();

    /**
     * 获取所有预约
     */
    public List<Reservation> getAllReservations() {
        return reservationDao.findAll();
    }

    /**
     * 获取某用户的所有预约
     */
    public List<Reservation> getUserReservations(int userId) {
        return reservationDao.findByUserId(userId);
    }

    /**
     * 获取待审批的预约列表
     */
    public List<Reservation> getPendingReservations() {
        return reservationDao.findPending();
    }

    /**
     * 提交预约申请
     * @return 错误消息，null 表示成功
     */
    public String submitReservation(Reservation res) {
        // 1. 参数校验
        if (res.getTitle() == null || res.getTitle().trim().isEmpty()) {
            return "会议主题不能为空";
        }
        if (res.getStartTime() == null || res.getEndTime() == null) {
            return "请选择会议时间";
        }
        if (res.getStartTime().after(res.getEndTime()) || res.getStartTime().equals(res.getEndTime())) {
            return "结束时间必须晚于开始时间";
        }
        if (res.getAttendees() <= 0) {
            return "参会人数必须大于0";
        }

        // 2. 检查会议室是否存在且可用
        Room room = roomService.getRoomById(res.getRoomId());
        if (room == null) {
            return "会议室不存在";
        }
        if (!room.isAvailable()) {
            return "该会议室正在维护中，暂不可用";
        }

        // 3. 检查人数是否超限
        if (res.getAttendees() > room.getCapacity()) {
            return "参会人数超过会议室容量（最大" + room.getCapacity() + "人）";
        }

        // 4. 时间冲突检查（只检查已批准的预约）
        if (reservationDao.hasConflict(res.getRoomId(), res.getStartTime(), res.getEndTime())) {
            return "该时间段已被占用，请选择其他时间";
        }

        // 5. 写入数据库（状态为 pending，等待管理员审批）
        int id = reservationDao.insert(res);
        if (id > 0) {
            return null; // 预约提交成功，等待审批
        }
        return "预约提交失败，请稍后重试";
    }

    /**
     * 批准预约（synchronized 防止并发竞态条件）
     * @return 错误消息，null 表示成功
     */
    public synchronized String approve(int reservationId) {
        Reservation res = reservationDao.findById(reservationId);
        if (res == null) {
            return "预约记录不存在";
        }
        if (!res.isPending()) {
            return "该预约无法批准（当前状态：" + res.getStatusText() + "）";
        }

        // 再次检查时间冲突（防止在 pending 期间被其他审批占用）
        if (reservationDao.hasConflict(res.getRoomId(), res.getStartTime(), res.getEndTime(), reservationId)) {
            return "该时间段已被占用，无法批准";
        }

        boolean success = reservationDao.updateStatus(reservationId, "approved");
        if (success) {
            return null;
        }
        return "操作失败，请稍后重试";
    }

    /**
     * 拒绝预约
     */
    public String reject(int reservationId) {
        Reservation res = reservationDao.findById(reservationId);
        if (res == null) {
            return "预约记录不存在";
        }
        if (!res.isPending()) {
            return "该预约无法拒绝（当前状态：" + res.getStatusText() + "）";
        }

        boolean success = reservationDao.updateStatus(reservationId, "rejected");
        if (success) {
            return null;
        }
        return "操作失败，请稍后重试";
    }

    /**
     * 用户取消预约（只能取消 pending 或 approved 状态的）
     */
    public String cancel(int reservationId, int userId) {
        Reservation res = reservationDao.findById(reservationId);
        if (res == null) {
            return "预约记录不存在";
        }
        if (res.getUserId() != userId) {
            return "您只能取消自己的预约";
        }
        if (!res.canCancel()) {
            return "该预约无法取消（当前状态：" + res.getStatusText() + "）";
        }

        boolean success = reservationDao.updateStatus(reservationId, "cancelled");
        if (success) {
            return null;
        }
        return "操作失败，请稍后重试";
    }
}
