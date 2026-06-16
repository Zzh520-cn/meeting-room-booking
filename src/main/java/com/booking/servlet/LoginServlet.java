package com.booking.servlet;

import com.booking.entity.User;
import com.booking.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * 登录 / 注册 / 退出 Servlet
 * 处理 GET（显示页面）和 POST（表单提交）
 */
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String action = req.getParameter("action");

        // 退出登录
        if ("logout".equals(action)) {
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 默认显示登录页面
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String action = req.getParameter("action");

        if ("register".equals(action)) {
            handleRegister(req, resp);
        } else {
            handleLogin(req, resp);
        }
    }

    /**
     * 处理登录
     */
    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userService.login(username, password);

        if (user != null) {
            // 登录成功，存入 session
            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            // 根据角色跳转不同页面
            if (user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin?action=approval");
            } else {
                resp.sendRedirect(req.getContextPath() + "/room?action=list");
            }
        } else {
            // 登录失败
            req.setAttribute("error", "用户名或密码错误");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        }
    }

    /**
     * 处理注册
     */
    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String realName = req.getParameter("realName");
        String phone    = req.getParameter("phone");
        String email    = req.getParameter("email");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(realName);
        user.setPhone(phone);
        user.setEmail(email);

        String error = userService.register(user);
        if (error == null) {
            // 注册成功，跳转到登录页
            req.setAttribute("message", "注册成功，请登录");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        } else {
            // 注册失败
            req.setAttribute("error", error);
            req.setAttribute("regUser", user); // 保留已填信息
            req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
        }
    }
}
