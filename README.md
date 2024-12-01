# repair-station-backend

`repair-station-backend` 是校园电脑维修管理系统的后端部分，基于 Spring Boot 构建，提供了 RESTful API 来支持前端部分的交互。该项目与前端部分[（`repair-station-frontend`）](https://gitee.com/gastronome-0_0/repair-station-frontend)结合使用，以实现完整的校园电脑维修管理功能。

## 项目架构

- **Spring Boot**: 用于构建后端服务，提供快速开发、部署和扩展能力。
- **MyBatis-Plus**: 用于简化数据库操作。
- **MySQL**: 用于存储系统的核心数据（如用户信息、维修订单等）。
- **MongoDB**: 用于存储日志信息和非关系型数据。
- **Redis**: 用于缓存会话数据和验证码信息，提高系统性能。

## 项目配置

### 1. 数据库配置

在 `application.yml` 文件中，配置数据库连接和 Redis 设置：

```yaml
#需要更改的选项
spring:
  application:
    name: repair-station
  data:
    mongodb: #mongodb的配置
      uri: mongodb://localhost:27017/repair_station
    redis: #redisd的配置
      host: localhost
      port: 6379
      database: 0
  datasource: #mysql的配置
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/repair_station
    username: root
    password: 123456
  mail:
    host: smtp.qq.com
    port: 587
    username: 邮箱地址              #发送消息的邮箱，确保开通POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务
    password: hlwvudhuvxcyddha    #邮箱授权码
    properties:
project-config: #自定义的配置
  web-url: http://localhost:5173 # 跨域链接，前端的 URL
  email: 邮箱地址                 # 给管理员发送邮箱提示的邮箱
  base-path: D:/                # 设置数据库备份，照片等的存储位置

```

### 2. 运行该项目

1. 使用 Maven 构建项目：

   - 使用 Maven 构建：

     ```sh
     mvn clean install
     ```

2. 运行项目：
    - 

   默认情况下，后端服务会启动在 `http://localhost:8099`，请确保该端口没有被占用。

## 前后端地址

- **前端项目**：`repair-station-app`，请在该项目的 `public/config.js` 文件中配置正确的后端 API 地址。
  - 前端项目地址：[https://gitee.com/gastronome-0_0/repair-station-app](https://gitee.com/gastronome-0_0/repair-station-app)

- **后端项目**：`repair-station-backend`，此项目提供后端服务支持。
  - 后端项目地址：[https://gitee.com/gastronome-0_0/repair-station-backend](https://gitee.com/gastronome-0_0/repair-station-backend)

## 联系

如有任何问题或建议，欢迎联系我：

- 电子邮件：[1305573134@qq.com](mailto:1305573134@qq.com)
