# Gamerr 游戏预约系统 - 后端

## 项目简介

Gamerr 是一个游戏预约系统后端服务，提供用户管理、游戏预约等基础功能。

## 技术栈

- Java 11
- Maven 3.5.3+
- Spring Boot 2.7.18
- MyBatis Plus 3.5.3.1
- MySQL 8.0

## 快速开始

### 1. 环境要求

- JDK 11+
- Maven 3.5.3+
- MySQL 8.0

### 2. 数据库初始化

1. 登录 MySQL：

```bash
mysql -u root -p
```

2. 创建数据库并执行初始化脚本：

```sql
CREATE DATABASE IF NOT EXISTS gamerr DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gamerr;
SOURCE sql/init.sql;
```

### 3. 配置数据库连接

编辑 `src/main/resources/application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gamerr?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 4. 启动服务

```bash
mvn spring-boot:run
```

或打包后运行：

```bash
mvn clean package
java -jar target/gamerr-backend-1.0.0.jar
```

### 5. 访问服务

- 基础地址：`http://localhost:8080`
- API文档：`http://localhost:8080/swagger-ui.html`（如已配置）

## 项目结构

```
src/main/java/com/gamerr/
├── GamerrApplication.java      # 启动类
├── config/                     # 配置类
├── controller/                 # 控制器层
├── dto/                        # 数据传输对象
├── entity/                     # 实体类
├── exception/                  # 异常处理
├── mapper/                     # 数据访问层
├── service/                    # 业务逻辑层
└── util/                       # 工具类

src/main/resources/
├── application.yml             # 应用配置
└── mapper/                     # MyBatis XML映射文件
```

## API 接口

### 用户模块

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/users/register | 用户注册 |
| POST | /api/users/login | 用户登录 |
| GET | /api/users/{id} | 获取用户信息 |
| PUT | /api/users/{id} | 更新用户信息 |

### 游戏模块

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/games | 获取游戏列表 |
| GET | /api/games/{id} | 获取游戏详情 |
| POST | /api/games | 新增游戏 |
| PUT | /api/games/{id} | 更新游戏 |
| DELETE | /api/games/{id} | 删除游戏 |

### 预约模块

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/reservations | 获取预约列表 |
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

## 开发说明

1. 本项目使用 MyBatis Plus 进行数据库操作
2. 所有接口返回统一格式的 JSON 响应
3. 使用 Lombok 简化实体类代码
4. 包含基础的异常处理机制
