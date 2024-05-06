##springframework--对于容器的基本认知


springframework容器的使用
1、通过spring.xml配置，ApplicationContext读取改配置初始化bean容器
2、自动装配，即IOC，在springframework中，可以通过包扫描+注解+配置类的方式初始化bean容器


教程： https://www.bilibili.com/video/BV1ZV411q7xu

IOC/DO：（Inverse Of Control/Dependency Injection）控住反转/依赖注入
1、控制翻转：由开发人员显式new生成，变为声明式反射生成初始化对象(过程交给了SpringContext容器)
2、依赖注入：属性字段的自动赋值操作，分为自动注入（注解匹配，2个）、手动注入（set方法、工厂）


spring IOC自动注入  -(依然需要显式初始化bean)
1、准备环境（命名空间和规范）
xmlns:context="http://www.springframework.org/schema/context"

http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context-4.2.xsd

2、开启自动注入，注解声明bean

    <context:annotation-config/>

3、直接将注解声明在对象属性上
@Resource / @Autowired

    


spring IOC 扫描器的配置
作用：对bean对象统一进行管理，简化配置，提高开发效率
使用本注解则默认开启依赖注入，可以省略<context:annotation-config/>
1、准备环境（命名空间和规范）-同上
2、开启包扫描
<context:component-scan base-package="com.mfq.*"/>

3、类上增加生命注解
Dao层：@Repository -mybatis中为@Mapper
Service层：@Service
Controller层：@Controller
任意类：@Component

注：以上注解作用相同，都是声明一个类为spring管理的bean
但从规范上建议指定规则进行声明注解，因为我们往往通过这些注解区分不同类型的bean进行AOP切面

    <context:component-scan base-package="com.mfq.*"/>

    <bean id="userService" class="UserService"/>
    <bean id="userDao" class="UserDao"/>




AOP：Aspect Oriented Programming 面向切面的编程
知识点：
1、代理：为某一个对象（被代理类/委托类）提供一个代理（代理类），用来控制这个对象的访问。
委托类与代理类拥有共同的父类或父接口，代理类可以对请求（调用）进行预处理、过滤、分配给指定对象操作
2、代理抽象：房产中介、下午茶外卖
3、代理模式：
-静态代理（代理目标角色固定、程序执行之前得到目标角色、对目标方法进行增强）
-动态代理（JDK动态代理、CGLIB动态代理）-method.invoke(),
通过反射机制可以生成任意类的动态代理，代理的行为可以代理多个方法
满足生产需要的同时又达到代码复用的目的
（目标对象不固定，应用程序中动态创建代理对象，代理对象增强目标对象的能力）

代理的三个要素:
代理目标：结婚
代理角色：新人
代理对象：婚庆公司
婚庆公司协助新人完善结婚流程，新人只用做必要内容，其余由婚庆公司进行增强；



题目清单：
1、spring容器内管理的bean对象，与java new出来的对象有什么区别？

2、spring如何创建一个bean的，创建的过程？

3、AOP实现原理？