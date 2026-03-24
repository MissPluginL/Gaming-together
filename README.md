# Gamerr 游戏预约系统

一个前后端分离的 Java 全栈项目，用于游戏预约管理。

## 项目简介

Gamerr 是一个游戏预约系统，提供用户注册登录、游戏浏览、游戏预约等基础功能。项目采用前后端分离架构，后端使用 Spring Boot，前端使用 Vue3。

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 11 | 开发语言 |
| Maven | 3.5.3+ | 项目构建工具 |
| Spring Boot | 2.7.18 | 应用框架 |
| MyBatis Plus | 3.5.3.1 | ORM框架 |
| MySQL | 8.0 | 数据库 |
| JWT | 0.9.1 | 身份认证 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Node.js | 22.19.0 | JavaScript运行时 |
| npm | 10.9.3 | 包管理器 |
| Vue | 3.3.4 | 前端框架 |
| Vue Router | 4.2.4 | 路由管理 |
| Axios | 1.4.0 | HTTP客户端 |
| Element Plus | 2.3.12 | UI组件库 |
| Vite | 4.4.9 | 构建工具 |

## 环境验证

在开始之前，请先验证本地环境配置。

### 1. 检查 Java 版本

```bash
java -version
```

**预期输出：** Java 11 或更高版本

```
java version "11.0.x"
Java(TM) SE Runtime Environment (build 11.0.x+xx-xxxx)
Java HotSpot(TM) 64-Bit Server VM
```

### 2. 检查 Maven 版本

```bash
mvn -version
```

**预期输出：** Maven 3.5.3 或更高版本

```
Apache Maven 3.5.3+
Maven home: /path/to/maven
Java version: 11.0.x
```

### 3. 检查 Node.js 版本

```bash
node -v
```

**预期输出：** v22.19.0

### 4. 检查 npm 版本

```bash
npm -v
```

**预期输出：** 10.9.3

### 5. 检查 MySQL 版本

```bash
mysql --version
```

**预期输出：** MySQL 8.0 或更高版本

## 数据库初始化

### 1. 启动 MySQL 服务

Windows:

```bash
net start mysql
```

或通过服务管理器启动 MySQL 服务。

### 2. 登录 MySQL

```bash
mysql -u root -p
```

输入您的 root 密码。

### 3. 执行初始化脚本

在 MySQL 命令行中执行：

```sql
SOURCE d:/Gamerr/Gamerr/backend/sql/init.sql;
```

或者在命令行中直接执行：

```bash
mysql -u root -p < d:/Gamerr/Gamerr/backend/sql/init.sql
```

### 4. 验证数据库创建

```sql
USE gamerr;
SHOW TABLES;
```

**预期输出：**

```
+-------------------+
| Tables_in_gamerr  |
+-------------------+
| t_game            |
| t_reservation     |
| t_user            |
+-------------------+
```

### 5. 验证测试数据

```sql
SELECT * FROM t_user;
SELECT * FROM t_game;
```

### MySQL 版本兼容说明

| MySQL 版本 | 兼容性 | 说明 |
|------------|--------|------|
| MySQL 8.0.33+ | ✅ 完全支持 | 推荐使用此版本 |
| MySQL 8.0.5+ | ✅ 支持 | 支持所有功能 |
| MySQL 5.7.36+ | ✅ 兼容 | 需要确保版本 >= 5.6.5 |

**注意：** 如果使用 MySQL 5.5，可能需要将 DATETIME 类型改为 TIMESTAMP。

## 后端启动

### 1. 修改数据库配置

编辑 `src/main/resources/application.yml`，修改以下配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gamerr?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
    username: your_username    # 修改为您的用户名
    password: your_password    # 修改为您的密码
```

### 2. 编译并启动

```bash
cd d:/Gamerr/Gamerr

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

或打包后运行：

```bash
# 打包
mvn clean package -DskipTests

# 运行jar包
java -jar target/gamerr-backend-1.0.0.jar
```

### 3. 验证后端启动

在浏览器访问：

```
http://localhost:8080/api/system/health
```

**预期响应：**

```json
{
    "code": 200,
    "message": "success",
    "data": {
        "status": "UP",
        "timestamp": "2026-03-23T20:00:00",
        "service": "gamerr-backend"
    }
}
```

### 后端启动成功标志

控制台输出：

```
========================================
  Gamerr Backend Started Successfully!
  API Address: http://localhost:8080/api
========================================
```

## 前端启动

### 1. 安装依赖

```bash
cd d:/Gamerr/Gamerr/frontend
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

### 3. 访问前端

打开浏览器访问：

```
http://localhost:3000
```

## 演示说明

### 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 管理员 |
| testuser | test123 | 普通用户 |

### 功能演示

1. **用户注册/登录**
   - 访问登录页面：`http://localhost:3000/login`
   - 或注册页面：`http://localhost:3000/register`

2. **首页**
   - 登录后跳转到首页
   - 查看热门游戏
   - 查看统计数据

3. **游戏列表**
   - 访问：`http://localhost:3000/games`
   - 查看所有游戏
   - 搜索和筛选游戏
   - 新增、编辑、删除游戏

4. **游戏详情**
   - 点击游戏卡片查看详情
   - 进行游戏预约

5. **个人中心**
   - 访问：`http://localhost:3000/profile`
   - 修改个人信息
   - 查看我的预约
   - 取消预约

## 项目结构

```
d:/Gamerr/Gamerr/
├── backend/                    # 后端项目
│   ├── sql/
│   │   └── init.sql           # 数据库初始化脚本
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/gamerr/
│   │       │       ├── GamerrApplication.java
│   │       │       ├── config/           # 配置类
│   │       │       ├── controller/       # 控制器
│   │       │       ├── dto/              # 数据传输对象
│   │       │       ├── entity/           # 实体类
│   │       │       ├── exception/        # 异常处理
│   │       │       ├── interceptor/      # 拦截器
│   │       │       ├── mapper/           # 数据访问层
│   │       │       ├── service/          # 业务逻辑层
│   │       │       └── util/             # 工具类
│   │       └── resources/
│   │           └── application.yml      # 应用配置
│   └── pom.xml
│
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/               # API接口（已整合到utils）
│   │   ├── router/            # 路由配置
│   │   ├── utils/             # 工具函数
│   │   ├── views/             # 页面组件
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
│
├── .mvn/                       # Maven wrapper
├── pom.xml                     # 根pom配置
└── README.md
```

## API 接口列表

### 系统接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/system/health | 健康检查 |

### 用户接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/users/register | 用户注册 |
| POST | /api/users/login | 用户登录 |
| GET | /api/users/info | 获取当前用户信息 |
| GET | /api/users/{id} | 获取用户信息 |
| PUT | /api/users/{id} | 更新用户信息 |

### 游戏接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/games | 获取游戏列表（分页） |
| GET | /api/games/all | 获取所有游戏 |
| GET | /api/games/{id} | 获取游戏详情 |
| POST | /api/games | 新增游戏 |
| PUT | /api/games/{id} | 更新游戏 |
| DELETE | /api/games/{id} | 删除游戏 |

### 预约接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/reservations | 获取预约列表 |
| GET | /api/reservations/my | 获取我的预约 |
| POST | /api/reservations | 创建预约 |
| DELETE | /api/reservations/{id} | 取消预约 |

## 统一响应格式

```json
{
    "code": 200,
    "message": "success",
    "data": {}
}
```

错误响应：

```json
{
    "code": 400,
    "message": "错误信息",
    "data": null
}
```

## 常见问题

### 1. Maven 下载依赖慢

可以配置 Maven 镜像源。在 `settings.xml` 中添加：

```xml
<mirrors>
  <mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <name>Aliyun Maven</name>
    <url>https://maven.aliyun.com/repository/central</url>
  </mirror>
</mirrors>
```

### 2. npm 安装依赖慢

可以使用淘宝镜像：

```bash
npm config set registry https://registry.npmmirror.com
npm install
```

### 3. MySQL 连接被拒绝

1. 确认 MySQL 服务已启动
2. 检查用户名和密码是否正确
3. 检查 MySQL 是否允许远程连接

### 4. 端口被占用

- 后端端口：8080
- 前端端口：3000

如果端口被占用，可以修改配置文件中的端口号。

## 版本信息

- 创建日期：2026-03-23
- 项目版本：1.0.0
