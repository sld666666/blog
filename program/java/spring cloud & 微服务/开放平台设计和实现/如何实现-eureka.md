# eureka

eureka 是一个服务注册和发现模块。在分布式系统用，服务和服务器的同学需要各自rpc接，如何暴露和使用这些接口是一个大问题。 euraka就是为了管理是使用这些接口而生的。


## 搭建 euraka 服务器

要是用 euraka 第一步要搭建 euraka 服务器。 在spring cloud中一切配置就非常简单了。

首先dependency:
```
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-eureka-server</artifactId>
</dependency>
```
然后配置：

```
eureka:
  instance:
    prefer-ip-address: true
  client:
    registerWithEureka: false
    fetchRegistry: false
    server:
      waitTimeInMsWhenSyncEmpty: 0
```

eureka是一个高可用的组件，它没有后端缓存，每一个实例注册之后需要向注册中心发送心跳（因此可以在内存中完成），在默认情况下erureka server也是一个eureka client ,必须要指定一个 server。通过eureka.client.registerWithEureka：false和fetchRegistry：false来表明自己是一个eureka server.

最后只要加入@EnableEurekaServer 就可以去启动一个euraka 服务器了

## 后台管理页面
eureka server 是有界面的，启动工程,打开浏览器访问： http://localhost:8889/ 就可以访问后台管理页面了

## 服务提供者
在 eureka 中提供服务很简单， 只需要 在Applicaion中加入@EnableEurekaClient 注解 就可以，然后我们就euraka可以发现我们的服务了， 服务名是应用名。
