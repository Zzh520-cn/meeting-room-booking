package com.booking.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接工具类
 * 优先从 classpath 下的 db.properties 读取连接信息，找不到则使用默认值
 */
public class DBUtil {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
                System.out.println("[DBUtil] ✅ 已加载 db.properties 配置文件");
            } else {
                System.err.println("[DBUtil] ⚠️ 未找到 db.properties，使用默认配置（仅本地开发可用）");
            }
        } catch (Exception e) {
            System.err.println("[DBUtil] ⚠️ 读取 db.properties 失败，使用默认配置");
        }

        URL      = props.getProperty("db.url",
                     "jdbc:mysql://localhost:3306/meeting_room_booking?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8");
        USER     = props.getProperty("db.user",     "root");
        PASSWORD = props.getProperty("db.password", "");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC 驱动加载失败，请检查 pom.xml 中 mysql-connector 依赖", e);
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
