package com.booking.service;

import com.booking.dao.RoomDao;
import com.booking.entity.Room;

import java.util.List;

/**
 * 会议室业务逻辑层
 */
public class RoomService {

    private final RoomDao roomDao = new RoomDao();

    /**
     * 获取所有会议室
     */
    public List<Room> getAllRooms() {
        return roomDao.findAll();
    }

    /**
     * 获取所有可用会议室
     */
    public List<Room> getAvailableRooms() {
        return roomDao.findAvailable();
    }

    /**
     * 根据 ID 获取会议室
     */
    public Room getRoomById(int id) {
        return roomDao.findById(id);
    }

    /**
     * 添加会议室
     * @return 错误消息，null 表示成功
     */
    public String addRoom(Room room) {
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            return "会议室名称不能为空";
        }
        if (room.getCapacity() <= 0) {
            return "容纳人数必须大于0";
        }

        int id = roomDao.insert(room);
        if (id > 0) {
            return null;
        }
        return "添加会议室失败";
    }

    /**
     * 修改会议室
     */
    public String updateRoom(Room room) {
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            return "会议室名称不能为空";
        }
        if (room.getCapacity() <= 0) {
            return "容纳人数必须大于0";
        }

        boolean success = roomDao.update(room);
        if (success) {
            return null;
        }
        return "更新会议室信息失败";
    }

    /**
     * 删除会议室
     */
    public String deleteRoom(int id) {
        boolean success = roomDao.delete(id);
        if (success) {
            return null;
        }
        return "删除会议室失败";
    }
}
