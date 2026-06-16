package com.booking.filter;

import com.booking.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录过滤器
 * 拦截所有请求，未登录用户只能访问登录/注册相关资源
 */
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");

        String path = req.getRequestURI();
        String contextPath = req.getContextPath();

        // 放行静态资源
        if (path.startsWith(contextPath + "/css/")
                || path.startsWith(contextPath + "/js/")
                || path.startsWith(contextPath + "/images/")) {
            chain.doFilter(request, response);
            return;
        }

        // 放行登录和注册相关页面与请求
        if (path.endsWith("/login")
                || path.endsWith("/login.jsp")
                || path.endsWith("/register.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        // 检查是否已登录
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // 未登录，跳转到登录页
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        // 已登录：管理员页面权限检查
        User user = (User) session.getAttribute("user");
        if (path.contains("/admin") || path.contains("/room?action=manage")) {
            if (!user.isAdmin()) {
                // 非管理员访问管理页面，跳转到用户首页
                resp.sendRedirect(contextPath + "/room?action=list");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
