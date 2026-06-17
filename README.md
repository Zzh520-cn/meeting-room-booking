# 会议室预约管理系统

软件工程期末大作业 —— 基于 Java Servlet + JSP + JDBC + MySQL 的 Web 应用

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | JSP + JSTL + HTML5 + CSS3 + 原生 JavaScript |
| 后端 | Java Servlet（原生，无框架） |
| 数据库 | MySQL 8.0 |
| 连接 | JDBC（原生，无 ORM） |
| 构建 | Maven（仅依赖管理） |
| 服务器 | Apache Tomcat 9+ |

## 功能清单

### 用户端
- ✅ 注册 / 登录
- ✅ 查看可用会议室列表（名称、容量、设备、位置）
- ✅ 提交预约申请（选择会议室、时间、人数、用途）
- ✅ 查看自己的预约记录
- ✅ 取消未审批或已批准的预约

### 管理员端
- ✅ 添加、修改、删除会议室信息
- ✅ 查看待审批的预约列表
- ✅ 批准预约（自动锁定时段，防止时间冲突）
- ✅ 拒绝预约
- ✅ 查看全部预约记录

## 如何运行（VSCode）

### 1. 准备环境

你需要安装以下软件：

| 软件 | 版本要求 | 下载地址 |
|------|---------|---------|
| JDK | 1.8+（推荐 JDK 8） | https://adoptium.net |
| MySQL | 8.0+ | https://dev.mysql.com/downloads |
| Maven | 3.6+ | https://maven.apache.org/download.cgi |

**VSCode 推荐插件**（在扩展商店搜索安装）：
- **Extension Pack for Java**（微软官方，包含所有 Java 开发必备功能）
- **Maven for Java**（让 VSCode 识别 Maven 项目）

> ⚠️ Maven 装好后，需要在终端里能用 `mvn` 命令。Windows 上装完记得把 Maven 的 `bin` 目录加到系统环境变量 `PATH` 里。

---

### 2. 初始化数据库

用 MySQL 客户端（Navicat、DataGrip、或命令行）执行 `sql/init.sql`：

**命令行方式**：
```bash
mysql -u root -p < sql/init.sql
```

**或者**：打开 `sql/init.sql`，复制全部内容到 MySQL 客户端中执行。

> 脚本会自动创建 `meeting_room_booking` 数据库、三张表（users/rooms/reservations）并插入测试数据。

---

### 3. 修改数据库连接信息

打开 [src/main/java/com/booking/util/DBUtil.java](src/main/java/com/booking/util/DBUtil.java)，把下面两行改成你自己的 MySQL 用户名和密码：

```java
private static final String USER     = "root";      // 改成你的 MySQL 用户名
private static final String PASSWORD = "123456";    // 改成你的 MySQL 密码
```

---

### 4. 启动项目

在 VSCode 终端（快捷键 `Ctrl + `` `）中运行：

```bash
mvn tomcat7:run
```

> 🎉 **不需要单独安装 Tomcat！** Maven 插件会自动下载并启动内嵌 Tomcat。

看到这行就说明启动成功了：
```
[INFO] Starting ProtocolHandler ["http-bio-8080"]
```

---

### 5. 访问系统

浏览器打开：**http://localhost:8080/meeting-room-booking**

---

### 6. 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 123456 |
| 普通用户 | zhangsan | 123456 |
| 普通用户 | lisi | 123456 |

---

### 常见问题

| 问题 | 解决 |
|------|------|
| `mvn` 命令找不到 | Maven 没装或没配环境变量，检查 `PATH` |
| 数据库连接失败 | 检查 MySQL 是否启动，用户名密码是否正确 |
| 端口 8080 被占用 | 先关掉占用 8080 的程序，或改 `pom.xml` 中的端口配置 |
| JDK 版本不对 | 项目需要 JDK 8，检查 `java -version` |

## 项目结构

```
meeting-room-booking/
├── pom.xml                                  # Maven 依赖配置
├── sql/init.sql                             # 数据库建表 + 测试数据
├── src/main/java/com/booking/
│   ├── entity/                              # 实体层
│   │   ├── User.java                        #   用户实体
│   │   ├── Room.java                        #   会议室实体
│   │   └── Reservation.java                 #   预约实体
│   ├── dao/                                 # 数据访问层
│   │   ├── UserDao.java                     #   用户 DAO
│   │   ├── RoomDao.java                     #   会议室 DAO
│   │   └── ReservationDao.java              #   预约 DAO（含冲突检测）
│   ├── service/                             # 业务逻辑层
│   │   ├── UserService.java                 #   登录/注册逻辑
│   │   ├── RoomService.java                 #   会议室 CRUD
│   │   └── ReservationService.java          #   预约审批/冲突检测
│   ├── servlet/                             # 控制器层
│   │   ├── LoginServlet.java                #   登录/注册/退出
│   │   ├── RoomServlet.java                 #   会议室管理
│   │   ├── ReservationServlet.java          #   预约处理
│   │   └── AdminServlet.java                #   审批管理
│   ├── filter/
│   │   └── LoginFilter.java                 # 登录拦截器
│   └── util/
│       └── DBUtil.java                      # 数据库连接工具
└── src/main/webapp/
    ├── WEB-INF/web.xml                      # Servlet 部署描述
    ├── css/style.css                        # 全局样式
    ├── js/main.js                           # 前端交互
    └── jsp/
        ├── login.jsp                        # 登录页
        ├── register.jsp                     # 注册页
        ├── user-rooms.jsp                   # 用户-会议室列表
        ├── user-booking.jsp                 # 用户-预约表单
        ├── user-reservations.jsp            # 用户-我的预约
        ├── admin-rooms.jsp                  # 管理员-会议室管理
        └── admin-approval.jsp               # 管理员-审批管理
```

## 架构说明

```
浏览器 → Servlet(Controller) → Service(业务) → DAO(数据) → MySQL
                ↕
            JSP(View)
```

- **三层架构**：表现层(JSP) → 业务逻辑层(Service) → 数据访问层(DAO)
- **MVC 模式**：Servlet 作为 Controller，JSP 作为 View，Service+DAO+Entity 作为 Model
- **原生实现**：不使用 Spring/MyBatis 等框架，深入理解 Servlet、JDBC 底层原理
