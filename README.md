# techtrainingcamp-security-12
抓到你了——具备安全防护能力的账号系统

#### 四、后端简介

后端项目存放在backend文件夹中，主要使用Spring Boot框架。

#### 五、后端技术

| 技术        | 说明             | 官网                                           |
| ----------- | ---------------- | ---------------------------------------------- |
| Spring Boot | 容器+MVC框架     | https://spring.io/projects/spring-boot         |
| MyBatis     | ORM框架          | http://www.mybatis.org/mybatis-3/zh/index.html |
| Redis       | 分布式缓存       | https://redis.io/                              |
| HikariCP    | 数据库连接池     | https://github.com/brettwooldridge/HikariCP    |
| MySQL       | 数据库系统       | https://www.mysql.com/                         |
| jBCrypt     | 加密算法工具库   | https://www.mindrot.org/projects/jBCrypt/      |
| JUnit       | 单元测试框架     | https://junit.org/junit5/                      |

#### 六、后端运行

```shell
cd backend
mvn spring-boot:run
```

或者打开IntelliJ IDEA，直接运行项目下的Spring Boot启动类com.catchyou.TechtrainingcampSecurity12Application
