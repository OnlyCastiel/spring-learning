###springbootApi工程简介

1、作为api工具，介绍springboot中各类注解的使用
其中部分注解为spring原生注解，部分为springboot注解

2、作为工程基础架构，基础的springboot项目，便于后期从0-1进行开发的快速脚手架



* @Configuration//（springframework注解）：标记当前类为一个配置类

* @ComponentScan("com.mfq.springbootapi") //（springframework注解）设置扫描包的路径

* @bean 详见IocConfig02

* @Autowired 装配顺序，  byType,byName,

* @PropertySource 加载配置文件，示例：
@PropertySource(value = {"classpath:jdbc.properties","classpath:jdbc.properties"})
在springboot中，会默认加载配置文件(名字固定) application.properties 或 application.yml 和 bootstrap.yml ---优先级问题？
配置文件优先级：application.properties>application.yml


3、组合注解
在spring2.x开始引入注解支持（jdk1.5推出注解功能），通过引入注解来消除大量xml配置，
完成bean的注入以及aop切面相关配置，但由于注解大量使用，造成了重复注解现象，一个类上需要添加多个注解，
spring为了消除这种重复注解，提高可维护性，引入了组合注解，可以理解为对代码的重构，
组合注解本质上是一系列元注解的组合，例如@Configuration，拥有@Component元注解，即配置类本身也是一个被IOC维护的bean
```
@Target(ElementType.TYPE)---定义改注解在何处使用
@Retention(RetentionPolicy.RUNTIME)---保存时效
@Documented---元注解：文档类型，javaDoc通过此注解生成文档信息
@Component---组件注解，被容器识别，生成bean实例
public @interface Configuration {
```

4、元注解
元注解是对其他注解进行说明的注解，一个自定义注解，必须要通过元注解的生命才会生效
 Java 5 定义了 4 个元注解 (@Target/@Retention/@Documented/@Inherited) 
 Java 8 定义了 2 个元注解 (@Repeatable 和 @Native)
* @Target 用来指定一个注解的使用范围，即被@Target修饰的注解，可以使用在什么地方；
* @Retention 用于描述注解的生命周期，也就是该注解被保留的时间长短
* @Documented 标记注解，没有成员变量，注明会被javaDoc工具识别，生成文档
* @Inherited 是一个标记注解，用来指定该注解可以被继承
* @Repeatable 允许在相同的程序元素中重复注解--只是一种简化思想，重复使用某个类的注解
* @Native 注解修饰成员变量，则表示这个变量可以被本地代码引用--不常使用

4、自定义组合注解
本质上，如果自己需要实现一套springboot的话，就依赖于组合注解，使用springframework中的元注解，
进行组合实现0-1的springboot框架


5、tomcat+springmvc的启动流程
* 详见类 WebInitializer.java 中的解释

###类加载机制
`双亲委派模型(优缺点)、破除双亲委派模型、java中的SPI机制
详见类 DataSourceConfig.java 中的解释`
核心流程：将一个.class文件，读入到内存，并为之创建一个class对象；初始化（静态代码）
后续进行实例化的过程中时，会根据此class对象，创建堆中的引用，并指向对应的class方法区

####双亲委派机制：
* 加载器：appClassLoader(应用类加载器)-extClassLoader（拓展类加载器）-bootstrapClassLoader（启动类加载器）
* 用户自定义类加载器：当用户应用有额外需求时，需要自定义类加载器，可以定义一些特定规则，比如tomcat中的类加载器（实现了哪些自定义功能）
* 判断本加载器是否已加载（缓存）
* 类优先让父加载器进行加载--一直到启动类加载器
* 父加载器无法加载，则自己进行尝试加载
* 全盘负责：当一个类加载器负责加载某个class时，该class所依赖的类都会由此加载器进行加载，除非显示的使用其他加载器

####双亲委派机制优缺点
* 优点：避免重复加载、安全，避免java核心类被外部篡改
* 缺点：父加载器加载的类无法加载用户自定义的类；
例如：java.sql.Driver，定义在java.sql包中，包所在的位置是：jdk\jre\lib\rt.jar中，java.sql包中还提供了其它相应的类和接口比如管理驱动的类:DriverManager类，很明显java.sql包是由BootstrapClassloader加载器加载的；而接口的实现类com.mysql.jdbc.Driver是由第三方实现的类库，由AppClassLoader加载器进行加载的，我们的问题是DriverManager在获取链接的时候必然要加载到com.mysql.jdbc.Driver类，这就是由BootstrapClassloader加载的类使用了由AppClassLoader加载的类，很明显和双亲委托机制的原理相悖，那它是怎么解决这个问题的？这就引申了我们第二个问题：如何打破双亲委派机制？
详见DataSourceConfig.java中的解释

```java
//  Worker method called by the public getConnection() methods.
private static Connection getConnection(String url, java.util.Properties info, Class<?> caller) throws SQLException {
    /*
     * When callerCl is null, we should check the application's
     * (which is invoking this class indirectly)
     * classloader, so that the JDBC driver class outside rt.jar
     * can be loaded from here.
     */
    //当没有显式指定classloader时,从线程上线文中获取，便可拿到application的应用类加载器，加载rt.jar的外部JDBC driver
    ClassLoader callerCL = caller != null ? caller.getClassLoader() : null;
    synchronized(DriverManager.class) {
        // synchronize loading of the correct classloader.
        if (callerCL == null) {
            callerCL = Thread.currentThread().getContextClassLoader();
        }
    }
}
```


####缺点处理办法：破除双亲委派-SPI
* 由此，Java的设计团队引入了一个不怎么优雅的设计：线程上下文类加载器;这个类加载器可以通过java.lang.Thread类的setContextClassLoader()方法进行设置，在创建线程时如果还未设置的话，他会从父线程中继承一个，如果在应用程序的全局范围内都没有设置过的话，那么这个类加载器默认就是应用程序类加载器。
* SPI（Service Provider Interface），通过线程上下文直接获取类加载器（非默认双亲委派机制）

####tomcat打破双亲委派机制
tomcat当启动多个war包服务的时候，共享同一个JVM，可能存在一些情况，这些情况会对运行造成问题
* tomcat自身引用的类如何确保不被用户类篡改
* 当一个tomcat启动多个服务时，多个服务之间引用了相同的包，版本也相同，如何避免重复引入造成的资源消耗
* 当包相同，但是版本不同，及包全路径相同，但代码不同，如何保证多个服务可以可以使用不同的类
* 实现热加载问题hotSwap



###spring自动配置原理
* @SpringBootConfiguration 
* @EnableAutoConfiguration 开启自动配置，扫描@Configuration类以及spi机制中定义的类，最后将这些配置类全部注册BeanDefinition，然后就可以交给接下来的Bean初始化过程去处理了。
* @ComponentScan 包扫描，将指定包路径下的，定义了bean的注解
* springboot中的自动配置类；@EnableAutoConfiguration
* 引入了诸多组件，当用户引入了相关依赖时，自动配置类会自动填充相关的bean;
* 批量自动导入，读到工程引入的所有jar包的默认配置文件，加载默认配置类--SPI机制
* 读取的是用户包路径下以及引入的lib包路径下的所有默认配置文件  /META-INF/spring.factories  文件中一般会有各个组件的自动配置类，负责初始化组件对应的bean


prifile配置
* 定义生效中的配置文件,根据定义spring.profiles.active=dev|test|prod 标识不同环境
* 根据环境标识，读取不同的配置文件 application-dev.yml / application-test.yml / application-prod.yml
* 



###springboot自定义启动类
* 业务包--实现基本业务功能代码
* 启动器--引入启动器（依赖管理，引入启动器，便引入一系列包）--参照mybatis-spring-boot-starter，一个包中，有大量依赖
* 自动配置类 spring-boot-autoconfigure、spring-boot-configuration-processor
* 写入spring.factories文件