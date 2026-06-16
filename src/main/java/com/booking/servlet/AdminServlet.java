package com.booking.servlet;

import com.booking.entity.Reservation;
import com.booking.entity.User;
import com.booking.service.ReservationService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * 管理员操作 Servlet
 * 处理审批预约、查看管理页面等
 */
public class AdminServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("rooms".equals(action)) {
            // 委托给 RoomServlet 处理会议室管理页面
            req.getRequestDispatcher("/room?action=manage").forward(req, resp);
        } else {
            // 默认：审批预约页面
            List<Reservation> pendingList = reservationService.getPendingReservations();
            req.setAttribute("pendingList", pendingList);

            // 同时加载所有预约记录供查看
            List<Reservation> allReservations = reservationService.getAllReservations();
            req.setAttribute("allReservations", allReservations);

            req.getRequestDispatcher("/jsp/admin-approval.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        String idStr   = req.getParameter("id");

        if (idStr == null) {
            session.setAttribute("error", "参数错误");
            resp.sendRedirect(req.getContextPath() + "/admin?action=approval");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            session.setAttribute("error", "参数错误");
            resp.sendRedirect(req.getContextPath() + "/admin?action=approval");
            return;
        }

        String error = null;
        if ("approve".equals(action)) {
            error = reservationService.approve(id);
            if (error == null) {
                session.setAttribute("message", "预约已批准，该时段已锁定");
            }
        } else if ("reject".equals(action)) {
            error = reservationService.reject(id);
            if (error == null) {
                session.setAttribute("message", "预约已拒绝");
            }
        } else {
            error = "未知操作";
        }

        if (error != null) {
            session.setAttribute("error", error);
        }

        resp.sendRedirect(req.getContextPath() + "/admin?action=approval");
    }
}
