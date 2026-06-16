package com.booking.servlet;

import com.booking.entity.Room;
import com.booking.entity.User;
import com.booking.service.RoomService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * 会议室管理 Servlet
 * 用户：查看可用会议室列表
 * 管理员：增删改会议室
 */
public class RoomServlet extends HttpServlet {

    private final RoomService roomService = new RoomService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String action = req.getParameter("action");

        // 用户查看会议室列表（默认行为）
        if ("list".equals(action) || action == null) {
            List<Room> rooms = roomService.getAvailableRooms();
            req.setAttribute("rooms", rooms);
            req.getRequestDispatcher("/jsp/user-rooms.jsp").forward(req, resp);
            return;
        }

        // 管理员-管理会议室页面
        if ("manage".equals(action)) {
            User user = (User) req.getSession().getAttribute("user");
            if (user == null || !user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            List<Room> rooms = roomService.getAllRooms();
            req.setAttribute("rooms", rooms);
            req.getRequestDispatcher("/jsp/admin-rooms.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String action = req.getParameter("action");

        if ("add".equals(action)) {
            handleAdd(req, resp);
        } else if ("edit".equals(action)) {
            handleEdit(req, resp);
        } else if ("delete".equals(action)) {
            handleDelete(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin?action=rooms");
        }
    }

    /**
     * 管理员-添加会议室
     */
    private void handleAdd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name      = req.getParameter("name");
        String capacityStr = req.getParameter("capacity");
        String equipment = req.getParameter("equipment");
        String location  = req.getParameter("location");

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
        } catch (NumberFormatException e) {
            capacity = 0;
        }

        Room room = new Room();
        room.setName(name);
        room.setCapacity(capacity);
        room.setEquipment(equipment);
        room.setLocation(location);
        room.setStatus("available");

        String error = roomService.addRoom(room);
        if (error == null) {
            req.getSession().setAttribute("message", "会议室添加成功");
        } else {
            req.getSession().setAttribute("error", error);
        }
        resp.sendRedirect(req.getContextPath() + "/room?action=manage");
    }

    /**
     * 管理员-修改会议室
     */
    private void handleEdit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr       = req.getParameter("id");
        String name        = req.getParameter("name");
        String capacityStr = req.getParameter("capacity");
        String equipment   = req.getParameter("equipment");
        String location    = req.getParameter("location");
        String status      = req.getParameter("status");

        int id;
        int capacity;
        try {
            id = Integer.parseInt(idStr);
            capacity = Integer.parseInt(capacityStr);
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "参数格式错误");
            resp.sendRedirect(req.getContextPath() + "/room?action=manage");
            return;
        }

        Room room = new Room();
        room.setId(id);
        room.setName(name);
        room.setCapacity(capacity);
        room.setEquipment(equipment);
        room.setLocation(location);
        room.setStatus(status != null ? status : "available");

        String error = roomService.updateRoom(room);
        if (error == null) {
            req.getSession().setAttribute("message", "会议室信息已更新");
        } else {
            req.getSession().setAttribute("error", error);
        }
        resp.sendRedirect(req.getContextPath() + "/room?action=manage");
    }

    /**
     * 管理员-删除会议室
     */
    private void handleDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "参数错误");
            resp.sendRedirect(req.getContextPath() + "/room?action=manage");
            return;
        }

        String error = roomService.deleteRoom(id);
        if (error == null) {
            req.getSession().setAttribute("message", "会议室已删除");
        } else {
            req.getSession().setAttribute("error", error);
        }
        resp.sendRedirect(req.getContextPath() + "/room?action=manage");
    }
}
