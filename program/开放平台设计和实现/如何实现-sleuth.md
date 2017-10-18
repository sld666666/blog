# 如何实现-sleuth
Spring Cloud Sleuth 主要功能就是在分布式系统中提供追踪解决方案，并且兼容支持了 zipkin，你只需要在pom文件中引入相应的依赖即可。

spring cloud sleuth与zipkin的关系，sleuth集成了zipkin。

　　zipkin的存储方式有多种，默认是保存在内存中，但是其不能持久保存，容器重启等一些其他情况可能导致数据的丢失，在本项目中，采用的保存到mysql数据库中。

## 搭建 sleuth server
```
<dependency>
         <groupId>io.zipkin.java</groupId>
         <artifactId>zipkin-server</artifactId>
     </dependency>

     <dependency>
         <groupId>io.zipkin.java</groupId>
         <artifactId>zipkin-autoconfigure-ui</artifactId>
     </dependency>
```

然后一个完整的后台服务跟踪系统就出来了。

## 如何使用
在服务中添加

```
<dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-zipkin</artifactId>
      </dependency>
```
