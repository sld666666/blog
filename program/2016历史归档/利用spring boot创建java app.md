#  [利用spring boot创建java app](http://www.cnblogs.com/sld666666/p/5439528.html)

# 利用spring boot创建java app

## 背景

在使用spring框架开发的过程中，随着功能以及业务逻辑的日益复杂，应用伴随着大量的XML配置和复杂的bean依赖关系，特别是在使用mvc的时候各种配置文件
错综复杂。随着spring3.0的发布，spring IO团队开始放弃使用XML配置文件，而使用"约定优先配租"的思想来代替。

spring boot 就是在这样的背景中抽象出来的开发框架。它和sping已经大量的常用第三方库集成在一起，几乎可以零配置使用，使开发流程更方便

## Hello world

使用传统的spring mvc,需要配置web.xml, applicationContext.xml,然后打包为war 在tomcat中运行， 而如果使用
spring boot， 一切都变得简单起来了。

下面使用Maven来创建spring boot 的web app工程

pom.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>org.springframework</groupId>
      <artifactId>gs-spring-boot</artifactId>
      <version>0.1.0</version>
      <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.3.3.RELEASE</version>
      </parent>
      <dependencies>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-test</artifactId>
          <scope>test</scope>
        </dependency>
      </dependencies>
      <properties>
        <java.version>1.8</java.version>
      </properties>
      <build>
        <plugins>
          <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
        </plugins>
      </build>
    </project>

HelloController

    package hello;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    @RestController
    public class HelloController {
        @RequestMapping("/")
        public String index() {
            return "Greetings from Spring Boot!";
        }
    }

其中：

  1. @RestController 表示使用spring mvc 来接收request请求
  2. @RequestMapping 映射到主页
  3. 当请求返回的时候，是纯文本，[那是因为@RestController是由@Controller](mailto:那是因为@RestController是由@Controller) 和 @ResponseBody组成

Application

    @SpringBootApplication
    public class Application {
        public static void main(String[] args) {
            ApplicationContext ctx = SpringApplication.run(Application.class, args);
            System.out.println("Let's inspect the beans provided by Spring Boot:");
        }
    }

其中：

  1. @SpringBootApplication 代表了其有四个注解组成： @Configuration， @EnableAutoConfiguration， @EnableWebMvc， @ComponentScan
  2. 在SpringApplication.run中会去自动启动tomcat
  3. run 方法返回上下文， 在这个上下文中可以拿到所有的bean

没有一行配置代码、也没有web.xml。基于Spring
Boot的应用在大多数情况下都不需要我们去显式地声明各类配置，而是将最常用的默认配置作为约定，在不声明的情况下也能适应大多数的开发场景。

总体而言spring boot 是对 java web app 开发的简化

## 单元测试

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = MockServletContext.class)
    @WebAppConfiguration
    public class HelloControllerTest {
        private MockMvc mvc;
    @Before
    public void before() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
    } 
    @After
    public void after() throws Exception { 
    } 
    /** 
    * 
    * Method: index() 
    * 
    */ 
    @Test
    public void testIndex() throws Exception { 
    //TODO: Test goes here...
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Greetings from Spring Boot!")));
    }
    } 

## 建立restfullweb服务器

接上， 使用srping boot建立web服务器就非常简单了， 首先建立一个pojo类

    public class Greeting {
        private final long id;
        private final String content;
    }

然后使用control来handle http请求

    @RestController
    public class GreetingController {
        private static final String template = "Hello, %s!";
        private final AtomicLong counter = new AtomicLong();
        @RequestMapping("/greeting")
        public Greeting greeting(@RequestParam(value="name", defaultValue="World")  String name) {
            return new Greeting(counter.incrementAndGet(),
                    String.format(template, name));
        }
    }

其中：

  1. @RequestParam 表明了参数要求，如果要必填则设置required=true
  2. 返回是一个对象，会被自动转换为json

当我们访问：

  1. <http://localhost:8080/greeting> 时候返回 {"id":1,"content":"Hello, World!"}
  2. <http://localhost:8080/greeting?name=User> 时候返回 {"id":2,"content":"Hello, User!"}

## 数据库访问

另一个非常常用的问题。在传统开发中， 我们需要配置：

  1. 类路径上添加数据访问驱动
  2. 实例化DataSource对象， 指定url, username, password
  3. 注入JdbcTemplate对象，如果使用Mybatis,还要配置框架信息

下面一个例子讲述用用spring boot来代替。 数据访问层我们将使用Spring Data JPA和Hibernate（JPA的实现之一）。

开始之前先介绍两个概念

## spring data

为了简化程序与数据库交互的代码，spring提供了一个现成的dao层框架，spring家族提供的spring-data适用于关系型数据库和nosql数据库
；  
例如 Spring Data JPA, Spring Data Hadoop, Spring Data MongoDB ，Spring Data Solr
等；  
具体的可以参考官网：<http://projects.spring.io/spring-data/>；  
他们的共同特点是给我们提供了框架代码，spring Data能自动创建实体dao的实现类和自定义查询，不再需要我们自己去实现了

## jpa

JPA全称为Java持久性API（Java Persistence API），JPA是Java EE
5标准之一，是一个ORM规范，由厂商来实现该规范，目前有hibernate、OpenJPA、TopLink、EclipseJPA等实现

然后我们进入配置环节， 配置很简单：

一： pom.xml映入 data-jpa 和mysql-connetor

     <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

二： 配置application.properties

    spring.datasource.url=jdbc:mysql://localhost:3306/testor
    spring.datasource.username=root
    spring.datasource.driverClassName = com.mysql.jdbc.Driver
    spring.datasource.password = xxx
    # Specify the DBMS
    spring.jpa.database = MYSQL
    # Show or not log for each sql query
    spring.jpa.show-sql = true
    # Hibernate ddl auto (create, create-drop, update)
    spring.jpa.hibernate.ddl-auto = update
    # Naming strategy
    spring.jpa.hibernate.naming-strategy = org.hibernate.cfg.ImprovedNamingStrategy
    # stripped before adding them to the entity manager)
    spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect

其中， hibernate的ddl-auto=update配置表名，数据库的表和列会自动创建

写下实体类：

    @Entity
    @Table(name="student")
    public class Student {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private  long id;
        @NotNull
        private String name;
        private String age;
    }

  1. @Entity，说明被这个注解修饰的类应该与一张数据库表相对应，表的名称可以由类名推断，当然了，也可以明确配置，[只要加上@Table](mailto:只要加上@Table)(name = "books")即可。需要特别注意，每个Entity类都应该有一个protected访问级别的无参构造函数，用于给Hibernate提供初始化的入口。
  2. @Id and @GeneratedValue：@Id注解修饰的属性应该作为表中的主键处理、@GeneratedValue修饰的属性应该由数据库自动生成，而不需要明确指定。
  3. @ManyToOne, @ManyToMany表明具体的数据存放在其他表中，在这个例子里，书和作者是多对一的关系，书和出版社是多对一的关系，因此book表中的author和publisher相当于数据表中的外键；[并且在Publisher中通过@OneToMany](mailto:并且在Publisher中通过@OneToMany)（mapped = "publisher"）定义一个反向关联（1——>n），表明book类中的publisher属性与这里的books形成对应关系。
  4. @Repository 用来表示访问数据库并操作数据的接口，同时它修饰的接口也可以被component scan机制探测到并注册为bean，[这样就可以在其他模块中通过@Autowired织入](mailto:这样就可以在其他模块中通过@Autowired织入)。

dao:

    @Repository
    public interface CustomerRepository extends CrudRepository<Customer, Long> {
        List<Customer> findByLastName(String lastName);
    }

详细的可以看 spring jpa的具体介绍。

最后使用：

    @RestController
    public class DbController {
        @Autowired
        private StudentDao dao;
        @RequestMapping("/get-student-counts")
        @ResponseBody
        public String getAllStudents(){
            List<Student> students = (List<Student>) dao.findAll();
            return String.format("%d", students.size());
        }
    }

主要一点是： 我在CustomerRepository 实现中每天添加方法：findByLastName， @Autowired 就会一直报错。

posted @ 2016-04-27 17:00 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=5439528) 收藏

##备注 
 @post in:2016-04-27 17:00