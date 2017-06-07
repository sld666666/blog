# spring-bean-问题集

## 背景
记录spring bean加载的问题

## 1. spring boot 找不到组件

错误信息： spring boot A component required a bean of type that could not be found

解决： @ComponentScan("com.sld")

## 2. junit4 加载 注解组件
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/bean-biz.xml"})

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="com.sld"/>

</beans>

```

## 3. junit 设置application.properties

@TestPropertySource(locations="classpath:test.properties")

在一般情况下， 会写一个基类来包装所有的

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/bean-biz.xml"})
@TestPropertySource(locations="classpath:test.properties")
public class BizBaseTest {
}

```