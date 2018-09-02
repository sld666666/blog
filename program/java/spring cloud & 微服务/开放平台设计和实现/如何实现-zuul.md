#如何实现-zuul
服务网关作为对外服务的统一出口， 起到了路由和负载均衡的作用。

## 使用

使用起来非常简单

1. dependency
```
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-zuul</artifactId>
</dependency>
```
2. 加注解： @EnableZuulProxy
3. 做写配置：
```
eureka:
  client:
    serviceUrl:
      defaultZone: http://register:8889/eureka

zuul:
  ignoredServices: '*'
  host:
    connect-timeout-millis: 20000
    socket-timeout-millis: 20000
  routes:
    api-demo:
      path: /api-demo/**
      serviceId: service-demo
```
只要配置了 euraka, 其会自动向 euraka注册服务。

进入如上的配置， 访问 http://localhost:8890/api-demo/sayhi?name=lili 就可以自动找到 service-demo 进行调用了

## 服务过滤
zuul不仅只是路由，并且还能过滤，做一些安全验证。入在进行开发平台接口验证的时候最重要的一步是对身份做校验。
