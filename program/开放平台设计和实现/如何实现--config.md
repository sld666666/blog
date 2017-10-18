# config

在分布式系统中，spring cloud config 提供一个服务端和客户端去提供可扩展的配置服务。我们可用用配置服务中心区集中的管理所有的服务的各种环境配置文件。配置服务中心采用Git的方式存储配置文件，因此我们很容易部署修改，有助于对环境配置进行版本管理。

## config server
使用非常简单， 第一步加依赖：

```
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
```

第二部分：
```
@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConfigApplication.class, args);
  }
}
```

第三部分：
```
spring:
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/shared
  profiles:
     active: native

server:
  port: 8888

security:
  user:
    password: ${CONFIG_SERVICE_PASSWORD}
```
其中为登录的密码， 可以在程序启动的时候压入， 用Idea的时候可以在环境变量中输入

## config client
在config server中， 我们的目录结构是这样的： resources/shared/register.yml
我们可以这样访问： http://localhost:8888/shared/register

而在程序中，需要这样配置：

```
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

```
spring:
  application:
    name: registry
  cloud:
    config:
      uri: http://config:8888
      fail-fast: true
      profile: dev
      password: ${CONFIG_SERVICE_PASSWORD}
      username: user
```

注意 profiles: dev ,它会知道dev下的配置文件，如果要变为prd，就这几换这个值就可以了。 当在 config server 中配置
```
server:
  port: 8761
```
自动会把配置拉倒当前运行系统

**特别注意一点** config的获取要放在bootstrap.yml中， 它在application加载之前就进行

## 把 config server 配置到git中
```
spring:
  config:
    name: configserver
  cloud:
    config:
      server:
        git:
          uri: http://192.168.0.247/baiwang/config-files.git
          search-paths: shared
          username: shenluodoong
          password: sld

server:
  port: 8888
```

然后我们就可以这么访问了：

http://localhost:8888/registerserver/dev

结果为：

```
{"name":"registerserver","profiles":["dev"],"label":null,"version":"4f43766a2c3a0ff2259faedae5f45d40adfeacb5","propertySources":[{"name":"http://192.168.0.247/baiwang/config-files.git/shared/registerserver-dev.properties","source":{"test":"abcde"}}]}
```
