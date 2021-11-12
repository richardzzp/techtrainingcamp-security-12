# techtrainingcamp-security-12

### 《抓到你了——具备安全防护能力的账号系统》

#### 一、 团队成员

曾鹏程、陈雨生、陈诺、廉建华、孙博文

#### 二、前端简介

前端项目存放在frontend文件夹中，采用vue.js框架进行编写，包含系统常用的登录、注册、验证码等功能，首页可以查看用户的登录记录

#### 三、前端技术

| 技术           | 说明               | 官网                                           |
| -------------- | ------------------ | ---------------------------------------------- |
| Node.js        | JavaScript运行环境 | https://nodejs.org/                            |
| Vue.js         | 前端框架           | https://cn.vuejs.org/index.html                |
| Element UI     | 组件库             | https://element.eleme.io/                      |
| fingerprintjs2 | 设备指纹采集器     | https://github.com/fingerprintjs/fingerprintjs |
| axios          | http库             | http://www.axios-js.com/                       |
| Vue-Router     | 路由管理器         | https://router.vuejs.org/                      |

#### 四、前端运行

```shell
cd frontend
npm i      #安装依赖
npm run serve
```



#### 五、后端简介

后端项目存放在backend文件夹中，主要使用Spring Boot框架。

#### 六、后端技术

| 技术        | 说明             | 官网                                           |
| ----------- | ---------------- | ---------------------------------------------- |
| Spring Boot | 容器+MVC框架     | https://spring.io/projects/spring-boot         |
| MyBatis     | ORM框架          | http://www.mybatis.org/mybatis-3/zh/index.html |
| Redis       | 分布式缓存       | https://redis.io/                              |
| HikariCP    | 数据库连接池     | https://github.com/brettwooldridge/HikariCP    |
| MySQL       | 数据库系统       | https://www.mysql.com/                         |
| jBCrypt     | 加密算法工具库   | https://www.mindrot.org/projects/jBCrypt/      |
| JUnit       | 单元测试框架     | https://junit.org/junit5/                      |

#### 七、后端运行

```shell
cd backend
mvn spring-boot:run
```

或者打开IntelliJ IDEA，直接运行项目下的Spring Boot启动类com.catchyou.TechtrainingcampSecurity12Application

#### 八、数据库表

##### Redis

Redis版本: 6.2.6

| 键                                 | 值                     | 说明                                                         |
| ---------------------------------- | ---------------------- | ------------------------------------------------------------ |
| verify_code_{手机号}               | 手机验证码             | 记录手机请求的随机验证码，3分钟后失效                        |
| verify_code_limit_{手机号}         | 1                      | 记录下刚进行了验证码请求的手机号，1分钟后失效，用于防止同一个手机号在1分钟内进行多次验证码请求，其值永远为1 |
| verify_code_request_count_{手机号} | 手机号请求验证码的次数 | 记录一个手机号进行验证码请求的累计次数                       |
