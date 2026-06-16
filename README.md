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

## 如何运行

### 1. 准备环境
- JDK 1.8+
- MySQL 8.0+
- Git Bash（装了 Git 就会有）

> Maven 不需要单独装，项目自带了临时版本，配一下环境变量就能用。

### 2. 初始化数据库
执行 `sql/init.sql` 脚本：
```bash
mysql -u root -p < sql/init.sql
```

### 3. 修改数据库连接
编辑 `src/main/java/com/booking/util/DBUtil.java`：
```java
private static final String URL      = "jdbc:mysql://localhost:3306/meeting_room_booking?...";
private static final String USER     = "root";      // 改成你的数据库用户名
private static final String PASSWORD = "123456";    // 改成你的数据库密码
```

### 4. 配置 Maven（仅第一次）
把 Maven 写入 Git Bash 的环境变量，以后每次打开终端就能直接用：
```bash
echo 'export PATH="/c/Users/Zzh/.m2/wrapper/dists/apache-maven-3.8.5-bin/5i5jha092a3i37g0paqnfr15e0/apache-maven-3.8.5/bin:$PATH"' >> ~/.bashrc
```
> 路径里的那串乱码可能因人而异，如果不对可以去 `C:\Users\你的用户名\.m2\wrapper\dists\` 下面找一下 `apache-maven-*` 的实际目录。

### 5. 启动服务器
```bash
cd ~/meeting-room-booking
mvn tomcat7:run
```
> 一行命令就启动，不需要单独装 Tomcat。看到 `Starting ProtocolHandler ["http-bio-8080"]` 就说明启动成功了。

### 6. 访问系统
打开浏览器访问：`http://localhost:8080/meeting-room-booking`

### 测试账号
| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 123456 |
| 普通用户 | zhangsan | 123456 |
| 普通用户 | lisi | 123456 |

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
