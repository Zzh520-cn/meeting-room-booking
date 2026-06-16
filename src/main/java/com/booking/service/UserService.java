package com.booking.service;

import com.booking.dao.UserDao;
import com.booking.entity.User;
import com.booking.util.PasswordUtil;

/**
 * 用户业务逻辑层
 */
public class UserService {

    private final UserDao userDao = new UserDao();

    /**
     * 用户登录验证（BCrypt 哈希比较，兼容旧明文密码并自动升级）
     * @return 登录成功返回 User 对象，失败返回 null
     */
    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }

        PasswordUtil.VerifyResult vr = PasswordUtil.verify(password, user.getPassword());
        if (!vr.matched) {
            return null;
        }

        // 旧明文密码匹配成功 → 自动升级为 BCrypt 哈希
        if (vr.needsUpgrade) {
            String newHash = PasswordUtil.hash(password);
            userDao.updatePassword(user.getId(), newHash);
        }

        return user;
    }

    /**
     * 用户注册
     * @return 注册结果消息
     */
    public String register(User user) {
        // 强制角色为普通用户（防止抓包提权）
        user.setRole("user");

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

        // 密码哈希
        user.setPassword(PasswordUtil.hash(user.getPassword()));

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
