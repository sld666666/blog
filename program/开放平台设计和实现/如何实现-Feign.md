## 如何实现 Feign

Feign是一个声明式的web服务客户端，它使得写web服务变得更简单。使用Feign,只需要创建一个接口并注解。它具有可插拔的注解特性，包括Feign 注解和JAX-RS注解。Feign同时支持可插拔的编码器和解码器。spring cloud对Spring mvc添加了支持，同时在spring web中次用相同的HttpMessageConverter。当我们使用feign的时候，spring cloud 整和了Ribbon和eureka去提供负载均衡。

简而言之：
- feign采用的是接口加注解
- feign 整合了ribbon


使用feign很简单
```
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-feign</artifactId>
</dependency>
```

```
@EnableFeignClients
```

定义接口
```
@FeignClient(value = "service-demo")
public interface IHiService {
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}

```
