### Spring整合Mybatis的核心底层原理
mybatis-spring的实现，包含的技术有spring-context / mybatis / mysql-connector-java

####使用到的包
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.3.10</version>
</dependency>

<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.9</version>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.29</version>
</dependency>
```

###核心技术
```java
@Component
public class MybatisFactoryBean implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        //TODO 反射生成mapper代理类
        return new UserMapper();
    }

    @Override
    public Class<?> getObjectType() {
        return UserMapper.class;
    }
}
```
* @Component + FactoryBean 可以在容器中注册两个对象，特别是 UserMapper 的代理对象，此对象封装了我们需要的mapper实现
