## 基于微服务的spring cloud框架
## 背景
整个百望是基于微服务的分布式系统，每个服务都规定了必须遵守的规则，并提供了demo， 以便能直接在这基础上使用。
## 框架架构
真个程序框架如下图所示：
![](http://7ktqp9.com1.z0.glb.clouddn.com/image2017-10-21%2011_21_46.png)
- web层：包含controller，过滤器，拦截器等，是服务发布的唯一出口
- service层：实现业务逻辑编排
- dao层：实现数据库访问功能
- integration层：实现对外部接口的访问，是外部接口的唯一入口
- common层：通用util工具类和通用 bean，VO定义
## pom.xml
整体架构使用spring cloud作为整个系统的基础框架。所有的配置如框架中的pom.xml所示。版本号也必须按照其进行设置。
## 配置文件
配置文件放在resources下，利用文件名进行环境的隔离：
- application.properties
- application-dev.properties
- application-prd.properties
- application-pre.properties
- application-test.properties

其必要设置如下：
```
#启动端口号
server.port=8001
#服务注册标识
spring.application.name=service-demo-demo
#注册中心开发环境地址
eureka.client.serviceUrl.defaultZone=http://registerserver-pool.dev.baiwang-inner.com:8889/eureka
#注册中心IP显示设置
eureka.instance.preferIpAddress=true
eureka.instance.instance-id=${spring.cloud.client.ipAddress}:${server.port}
```
其中eureka.client.serviceUrl.defaultZone是 euraka服务器，如果需要测试就在本机部署一个即可。
## 代码规范
代码规范见附件<百望java编码规范>
## log文件
用logback

```
  <dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
  </dependency>
  <dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
  </dependency>
```
所有Log配置在logback.xml中
