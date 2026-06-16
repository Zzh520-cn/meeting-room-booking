-- ============================================
-- 会议室预约管理系统 - 数据库初始化脚本
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS meeting_room_booking
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE meeting_room_booking;

-- ============================================
-- 1. 用户表
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE COMMENT '登录账号',
    password    VARCHAR(100) NOT NULL COMMENT '登录密码',
    real_name   VARCHAR(50)  NOT NULL COMMENT '真实姓名',
    role        VARCHAR(10)  NOT NULL DEFAULT 'user' COMMENT '角色: admin-管理员, user-普通用户',
    phone       VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    email       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 2. 会议室表
-- ============================================
CREATE TABLE IF NOT EXISTS rooms (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL COMMENT '会议室名称',
    capacity    INT          NOT NULL DEFAULT 10 COMMENT '容纳人数',
    equipment   VARCHAR(255) DEFAULT NULL COMMENT '设备（投影仪、白板、视频会议等）',
    location    VARCHAR(100) DEFAULT NULL COMMENT '位置（如：3楼A区）',
    status      VARCHAR(20)  NOT NULL DEFAULT 'available' COMMENT '状态: available-可用, maintenance-维护中',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议室表';

-- ============================================
-- 3. 预约表
-- ============================================
CREATE TABLE IF NOT EXISTS reservations (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    room_id     INT          NOT NULL COMMENT '会议室ID',
    user_id     INT          NOT NULL COMMENT '预约用户ID',
    title       VARCHAR(200) NOT NULL COMMENT '会议主题',
    start_time  DATETIME     NOT NULL COMMENT '开始时间',
    end_time    DATETIME     NOT NULL COMMENT '结束时间',
    attendees   INT          NOT NULL DEFAULT 1 COMMENT '参会人数',
    purpose     TEXT         DEFAULT NULL COMMENT '会议用途/说明',
    status      VARCHAR(20)  NOT NULL DEFAULT 'pending' COMMENT '状态: pending-待审批, approved-已批准, rejected-已拒绝, cancelled-已取消',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- 为常见查询创建索引
CREATE INDEX idx_reservations_room_time ON reservations(room_id, start_time, end_time);
CREATE INDEX idx_reservations_user ON reservations(user_id);
CREATE INDEX idx_reservations_status ON reservations(status);

-- ============================================
-- 测试数据
-- ============================================

-- 测试账号: 密码均为 123456（BCrypt 哈希存储）
INSERT INTO users (username, password, real_name, role, phone, email) VALUES
('admin',   '$2a$10$LzOuo41rnAW5XVYox2tpgum6eZesO8.ygAfTsyUwu3Qpn3UkBmOgC', '系统管理员', 'admin', '13800000001', 'admin@company.com'),
('zhangsan','$2a$10$LzOuo41rnAW5XVYox2tpgum6eZesO8.ygAfTsyUwu3Qpn3UkBmOgC', '张三',       'user',  '13800000002', 'zhangsan@company.com'),
('lisi',    '$2a$10$LzOuo41rnAW5XVYox2tpgum6eZesO8.ygAfTsyUwu3Qpn3UkBmOgC', '李四',       'user',  '13800000003', 'lisi@company.com'),
('wangwu',  '$2a$10$LzOuo41rnAW5XVYox2tpgum6eZesO8.ygAfTsyUwu3Qpn3UkBmOgC', '王五',       'user',  '13800000004', 'wangwu@company.com');

-- 会议室数据
INSERT INTO rooms (name, capacity, equipment, location, status) VALUES
('星辰厅', 20, '投影仪,白板,视频会议系统',     '3楼A区301', 'available'),
('云海厅', 10, '投影仪,白板',                  '3楼A区302', 'available'),
('朝阳厅', 30, '投影仪,白板,音响系统,视频会议', '5楼B区501', 'available'),
('竹韵阁', 6,  '白板',                         '2楼C区201', 'available'),
('聚贤堂', 50, '投影仪,白板,音响系统,视频会议,同声传译', '1楼大厅', 'available'),
('听风轩', 8,  '投影仪',                       '2楼C区202', 'maintenance');

-- 测试预约数据（过去 + 待审批 + 已批准）
INSERT INTO reservations (room_id, user_id, title, start_time, end_time, attendees, purpose, status) VALUES
(1, 2, '项目周会',       '2026-06-15 09:00:00', '2026-06-15 11:00:00', 15, '讨论本周项目进度与风险', 'approved'),
(2, 3, '需求评审',       '2026-06-16 14:00:00', '2026-06-16 16:00:00', 8,  '评审新版本产品需求文档', 'approved'),
(3, 2, '技术分享会',     '2026-06-17 10:00:00', '2026-06-17 12:00:00', 25, 'AI技术内部分享',         'pending'),
(1, 4, '客户演示',       '2026-06-18 15:00:00', '2026-06-18 17:00:00', 10, '向客户演示产品原型',     'pending'),
(4, 3, '小组讨论',       '2026-06-19 09:00:00', '2026-06-19 10:30:00', 5,  '讨论设计方案',            'pending');
