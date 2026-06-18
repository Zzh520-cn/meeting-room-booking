# 会议室预约管理系统

软件工程期末大作业 —— 基于 Java Servlet + JSP + JDBC + MySQL 的 Web 应用

## 下载项目

### 方法一：浏览器直接下载（推荐，不需要装任何东西）

1. 打开 https://github.com/Zzh520-cn/meeting-room-booking
2. 点击绿色 **Code** 按钮 → **Download ZIP**
3. 解压后拖进 VSCode 即可

### 方法二：用 Git 克隆（装了 Git 的话）

```bash
git clone https://github.com/Zzh520-cn/meeting-room-booking.git
```

### 方法三：用 VSCode 自带功能

1. `Ctrl + Shift + P` → 输入 `Git: Clone`
2. 粘贴仓库地址：`https://github.com/Zzh520-cn/meeting-room-booking.git`
3. 选一个文件夹保存即可

> ⚠️ 方法三也需要电脑上装了 Git，VSCode 只是调用它。

---

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

> 💡 **关于终端**：下面的命令是在 VSCode 自带的终端里运行的。打开 VSCode 后按 **`Ctrl + `` `**（Tab 上面那个键）就能弹出终端窗口，不需要额外装 Git Bash。Windows 上终端默认是 PowerShell，完全够用。
>
> ⚠️ **关于目录**：用 VSCode 打开项目文件夹后，终端**默认就在项目根目录**，不需要再 `cd`。如果终端显示的不是项目目录，用 `cd` 加你解压后的实际文件夹名（GitHub ZIP 下载的通常是 `meeting-room-booking-master`）。

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

执行项目里的 `sql/init.sql` 脚本。任选一种方式：

**方式 A — 用 MySQL 客户端（最简单）**：
打开 Navicat / DataGrip 等工具，直接运行 `sql/init.sql` 文件。

**方式 B — 用 VSCode 终端**（确保终端当前在项目根目录，然后）：
```bash
# 登录 MySQL（输入密码后回车）
mysql -u root -p

# 然后在 MySQL 里面执行
source sql/init.sql;
```

> 脚本会自动创建 `meeting_room_booking` 数据库、三张表（users/rooms/reservations）并插入测试数据。

---

### 3. 配置数据库连接

1. 找到 `src/main/resources/db.properties.example`，复制一份，重命名为 **`db.properties`**
2. 打开 `db.properties`，填上你的 MySQL 用户名和密码：

```properties
db.url=jdbc:mysql://localhost:3306/meeting_room_booking?useSSL=false&...    # 一般不用改
db.user=root                  # 改成你的 MySQL 用户名
db.password=你的MySQL密码      # 改成你的 MySQL 密码
```

> ⚠️ `db.properties` 包含你的数据库密码，**已加入 .gitignore，不会被提交到 GitHub**。复制的时候注意文件名是 `db.properties` 不是 `db.properties.example`。

---

### 4. 启动项目

在 VSCode 终端（快捷键 `Ctrl + `` `）中运行：

```bash
# 确保终端当前在项目根目录（VSCode 打开项目后默认就在这）
# 如果不在，先 cd 到你的项目文件夹，例如：
# cd meeting-room-booking
# 或 cd meeting-room-booking-master（GitHub ZIP 下载的）

# 启动
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
| 登录提示"用户名或密码错误" | **99% 是数据库没连上**，看 Tomcat 控制台的红色报错信息：<br>• 如果报 `[UserDao] ❌ 数据库查询失败` → MySQL 没启动，或 `DBUtil.java` 里的用户名密码不对<br>• 如果报 `[UserService] 用户不存在` → 没执行 `init.sql`，数据库里没有用户<br>• 如果报 `密码不匹配` → 数据库中密码哈希和代码库不一致，重新执行 `init.sql` |
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
