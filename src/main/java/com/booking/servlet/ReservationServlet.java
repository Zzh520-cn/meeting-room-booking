package com.booking.servlet;

import com.booking.entity.Reservation;
import com.booking.entity.Room;
import com.booking.entity.User;
import com.booking.service.ReservationService;
import com.booking.service.RoomService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 预约管理 Servlet
 * 用户：查看自己的预约、提交预约、取消预约
 */
public class ReservationServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();
    private final RoomService        roomService        = new RoomService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("book".equals(action)) {
            // 显示预约表单，附带会议室信息
            String roomIdStr = req.getParameter("roomId");
            if (roomIdStr != null) {
                try {
                    int roomId = Integer.parseInt(roomIdStr);
                    Room room = roomService.getRoomById(roomId);
                    req.setAttribute("room", room);
                } catch (NumberFormatException ignored) {}
            }
            List<Room> rooms = roomService.getAvailableRooms();
            req.setAttribute("rooms", rooms);
            req.getRequestDispatcher("/jsp/user-booking.jsp").forward(req, resp);
        } else {
            // 默认：查看我的预约
            List<Reservation> list = reservationService.getUserReservations(user.getId());
            req.setAttribute("reservations", list);
            req.getRequestDispatcher("/jsp/user-reservations.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("book".equals(action)) {
            handleBook(req, resp, user);
        } else if ("cancel".equals(action)) {
            handleCancel(req, resp, user);
        } else {
            resp.sendRedirect(req.getContextPath() + "/reservation");
        }
    }

    /**
     * 提交预约申请
     */
    private void handleBook(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        String roomIdStr    = req.getParameter("roomId");
        String title        = req.getParameter("title");
        String startTimeStr = req.getParameter("startTime");
        String endTimeStr   = req.getParameter("endTime");
        String attendeesStr = req.getParameter("attendees");
        String purpose      = req.getParameter("purpose");

        try {
            int roomId    = Integer.parseInt(roomIdStr);
            int attendees = Integer.parseInt(attendeesStr);

            // 将 HTML datetime-local 格式转换为 Timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Timestamp startTime = new Timestamp(sdf.parse(startTimeStr).getTime());
            Timestamp endTime   = new Timestamp(sdf.parse(endTimeStr).getTime());

            Reservation res = new Reservation();
            res.setRoomId(roomId);
            res.setUserId(user.getId());
            res.setTitle(title);
            res.setStartTime(startTime);
            res.setEndTime(endTime);
            res.setAttendees(attendees);
            res.setPurpose(purpose);

            String error = reservationService.submitReservation(res);
            if (error == null) {
                req.getSession().setAttribute("message", "预约已提交，等待管理员审批");
                resp.sendRedirect(req.getContextPath() + "/reservation");
            } else {
                req.getSession().setAttribute("error", error);
                // 保留表单数据
                Room room = roomService.getRoomById(roomId);
                req.setAttribute("room", room);
                req.setAttribute("rooms", roomService.getAvailableRooms());
                req.setAttribute("formData", res);
                req.getRequestDispatcher("/jsp/user-booking.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("error", "提交失败，请检查输入格式");
            resp.sendRedirect(req.getContextPath() + "/reservation?action=book");
        }
    }

    /**
     * 用户取消预约
     */
    private void handleCancel(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        try {
            int id = Integer.parseInt(idStr);
            String error = reservationService.cancel(id, user.getId());
            if (error == null) {
                req.getSession().setAttribute("message", "预约已取消");
            } else {
                req.getSession().setAttribute("error", error);
            }
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "参数错误");
        }
        resp.sendRedirect(req.getContextPath() + "/reservation");
    }

}
