package com.booking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 * 使用 JDBC 直接连接 MySQL，返回 Connection 对象
 */
public class DBUtil {

    // 数据库连接参数（根据你的实际环境修改）
    private static final String URL      = "jdbc:mysql://localhost:3306/meeting_room_booking?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
    private static final String USER     = "root";
    private static final String PASSWORD = "z520520zzh";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC 驱动加载失败，请检查 pom.xml 中 mysql-connector 依赖", e);
        }
    }

    /**
     * 获取数据库连接
     * 调用方需自行关闭连接（使用 try-with-resources）
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
