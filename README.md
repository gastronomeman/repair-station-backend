# repair-station-backend

`repair-station-backend` 是校园电脑维修管理系统的后端部分，基于 Spring Boot 构建，提供 RESTful API，支持与前端部分[`repair-station-frontend`](https://gitee.com/gastronome-0_0/repair-station-frontend)的交互，实现完整的校园电脑维修管理功能。

## 项目简介

本项目是广东农工商职业技术学院“ITeam基地维修站”的合作项目，专注于校内电脑维修管理，通过前后端分离的架构实现高效、可靠的服务。

## 项目架构

- **Spring Boot**: 构建后端服务，提供快速开发、部署和扩展能力。
- **MyBatis-Plus**: 简化数据库操作。
- **MySQL**: 存储系统核心数据（如用户信息、维修订单等）。
- **MongoDB**: 存储日志信息和非关系型数据。
- **Redis**: 缓存会话数据和验证码信息，提高系统性能。

## 数据库部署

### 1. MySQL 数据库部署

在 `src/main/resources/sql/MySQL` 目录下，提供了数据库脚本。执行以下 SQL 脚本来初始化 MySQL 数据库和表结构：

1. **创建数据库**：首先创建数据库 `repair_station`：

   ```sql
   CREATE DATABASE repair_station;
   ```

2. **执行数据表结构脚本**：然后使用以下命令导入数据库表结构：

   ```sh
   mysql -u root -p repair_station < /path/to/backup
   ```

   替换 `/path/to/backup` 为你备份文件所在的路径。

### 2. MongoDB 数据库部署

MongoDB 是非关系型数据库，不需要创建数据库和表结构。只需确认 MongoDB 服务正在运行。

1. **导入数据**：使用以下命令将备份的数据导入 MongoDB：

   ```sh
   mongorestore --drop --db repair_station /path/to/backup
   ```

   替换 `/path/to/backup` 为你备份文件所在的路径。

## 项目配置
### 1. 把项目文件整体拖入IDEA，更新pom.xml

### 2. 设置运行配置

在 `application.yml` 文件中配置数据库连接设置，及部分文件存储位置：

```yaml
spring:
  application:
    name: repair-station
  data:
    mongodb:
      uri: mongodb://localhost:27017/repair_station  # MongoDB 配置
      
    redis:
      host: localhost
      port: 6379
      database: 0  # Redis 配置
      
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/repair_station  # MySQL 配置
    username: root
    password: 123456
    
  mail:
    host: smtp.qq.com
    port: 587
    username: your_email@example.com  # 发送邮件的邮箱地址
    password: your_email_authorization_code  # 邮箱授权码
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true

project-config:  # 自定义配置
  web-url: http://localhost:5173  # 前端应用的 URL
  email: your_admin_email@example.com  # 管理员邮箱
  base-path: D:/  # 数据库备份、照片等存储路径
```

### 3. 项目运行
 
1. **运行前,编译器需要安装Lombok插件**：
   - 如果使用的是 IntelliJ IDEA：打开 IDE，点击 File -> Settings -> Plugins。
   - 搜索 Lombok，然后点击 Install 安装插件。

2. **运行项目**：运行RepairStationApplication.java文件

   默认情况下，后端服务会启动在 `http://localhost:8099`，请确保该端口没有被占用。

## 前后端地址

- **前端项目**：`repair-station-frontend`，需在前端项目的 `public/config.js` 文件中配置正确的后端 API 地址。
    - 前端项目地址：[https://gitee.com/gastronome-0_0/repair-station-frontend](https://gitee.com/gastronome-0_0/repair-station-frontend)

- **后端项目**：`repair-station-backend`，提供后端服务支持。
    - 后端项目地址：[https://gitee.com/gastronome-0_0/repair-station-backend](https://gitee.com/gastronome-0_0/repair-station-backend)

## 联系方式

如有任何问题或建议，欢迎联系我：

- 电子邮件：[1305573134@qq.com](mailto:1305573134@qq.com)
