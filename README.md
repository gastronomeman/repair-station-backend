# repair-station-backend

`repair-station-backend` 是校园电脑维修管理系统的后端部分，基于 Spring Boot 构建，提供了 RESTful API 来支持前端部分的交互。该项目与前端部分[（`repair-station-frontend`）](https://gitee.com/gastronome-0_0/repair-station-frontend)结合使用，以实现完整的校园电脑维修管理功能。

## 项目架构

- **Spring Boot**: 用于构建后端服务，提供快速开发、部署和扩展能力。
- **Spring Security**: 用于实现用户身份验证和权限控制。
- **MyBatis-Plus**: 用于简化数据库操作。
- **MySQL**: 用于存储系统的核心数据（如用户信息、维修订单等）。
- **MongoDB**: 用于存储日志信息和非关系型数据。
- **Redis**: 用于缓存会话数据和验证码信息，提高系统性能。

## 项目配置

### 1. 数据库配置

在 `application.yml` 文件中，配置数据库连接和 Redis 设置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/repair_station?useUnicode=true&characterEncoding=utf8
    username: your-username
    password: your-password
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      pool-name: HikariCP
      maximum-pool-size: 10

  redis:
    host: localhost
    port: 6379
    password: your-redis-password
    database: 0
```

### 2. 后端 API 地址

前端项目需要与后端进行数据交互，请确保后端 API 地址正确配置。你可以在 `public/config.js` 中配置后端的 API 地址：

```js
window.config = {
  apiBaseUrl: "https://your-backend-api-url"  // 这里是后端的实际 API 地址
};
```

### 3. 运行该项目

1. 克隆该项目：

   ```sh
   git clone https://gitee.com/gastronome-0_0/repair-station-backend.git
   ```

2. 进入项目目录：

   ```sh
   cd repair-station-backend
   ```

3. 使用 Maven 或 Gradle 构建项目：

   - 使用 Maven 构建：

     ```sh
     mvn clean install
     ```

   - 使用 Gradle 构建：

     ```sh
     gradle build
     ```

4. 运行项目：

   ```sh
   mvn spring-boot:run
   ```

   或者通过生成的 `jar` 文件启动：

   ```sh
   java -jar target/repair-station-backend-1.0.0.jar
   ```

   默认情况下，后端服务会启动在 `http://localhost:8080`，请确保该端口没有被占用。

## 前后端地址

- **前端项目**：`repair-station-app`，请在该项目的 `public/config.js` 文件中配置正确的后端 API 地址。
  - 前端项目地址：[https://gitee.com/gastronome-0_0/repair-station-app](https://gitee.com/gastronome-0_0/repair-station-app)

- **后端项目**：`repair-station-backend`，此项目提供后端服务支持。
  - 后端项目地址：[https://gitee.com/gastronome-0_0/repair-station-backend](https://gitee.com/gastronome-0_0/repair-station-backend)

## 贡献

欢迎提出问题、提交 bug 或者贡献代码。我们鼓励社区成员积极参与项目的改进。请通过 GitHub 或 Gitee 提交问题或 PR。

## 联系

如有任何问题或建议，欢迎联系我：

- 项目源码：[Gitee 仓库](https://gitee.com/gastronome-0_0/repair-station-backend)
- 电子邮件：[your-email@example.com](mailto:your-email@example.com)

---

这个 `README` 更加简洁地说明了项目的基本信息、配置方法和前后端连接。你可以根据实际需求进一步调整。