package com.booking.service;

import com.booking.dao.UserDao;
import com.booking.entity.User;

/**
 * 用户业务逻辑层
 */
public class UserService {

    private final UserDao userDao = new UserDao();

    /**
     * 用户登录验证
     * @return 登录成功返回 User 对象，失败返回 null
     */
    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user; // 明文密码比较（生产环境应使用 BCrypt 等加密方式）
        }
        return null;
    }

    /**
     * 用户注册
     * @return 注册结果消息
     */
    public String register(User user) {
        // 校验必填字段
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return "用户名不能为空";
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return "密码不能为空";
        }
        if (user.getRealName() == null || user.getRealName().trim().isEmpty()) {
            return "真实姓名不能为空";
        }

        // 检查用户名是否已存在
        if (userDao.existsByUsername(user.getUsername())) {
            return "该用户名已被注册，请换一个";
        }

        // 默认角色为普通用户
        if (user.getRole() == null) {
            user.setRole("user");
        }

        int id = userDao.insert(user);
        if (id > 0) {
            return null; // null 表示注册成功
        }
        return "注册失败，请稍后重试";
    }

    /**
     * 根据 ID 获取用户
     */
    public User getUserById(int id) {
        return userDao.findById(id);
    }
}
