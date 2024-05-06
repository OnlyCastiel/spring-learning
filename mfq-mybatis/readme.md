###mybatis 模拟实现

####使用到的包
* mysql-connector-java 实现了 java.sql.Driver 数据库驱动类 com.mysql.cj.jdbc.Driver

```xml
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.29</version>
    </dependency>
```

####核心实现流程
实现一个Mapper工厂类，通过jdk动态代理，生成mapper代理类，实际获取的对象为代理对象，执行的接口方法会执行到代理类重写的代理方法
```java
public class MapperProxyFactory {
    public static <T> T getMapper(Class<T> mapper){
   
        Object instance = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{mapper}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //mapper执行的时候实际调用的方法
                return null;
            }
        });
        return (T) instance;
    }
}
```
* 获取原始sql：通过注解获取方法method上的sql语句
* 解析表达式：原始sql表达式中的#{} 以及 ${} 需要被解析为List<String> params 记录每个参数的code
* 替换占位符：原始sql表达式中的#{} 以及 ${}替换为 ’?‘占位符
* 获取参数映射：记录方法method中的各个参数code,无法通过反射拿到，只能通过@Param注解，得到 Map<code,value> 映射关系 paramsMap
* 编译sql： prepareStatement预编译可以有效防止sql注入，提升sql执行效率
* 占位符赋值：对应每个占位符，设置参数，占位符个数为 params.size(),值从 paramsMap 中获取
* 类型处理器：对于statement.setString(index,value); 值的设置方法，需要借助 TypeHandler<T> 类型处理器进行处理
* 执行：statement.execute(); 后获取ResultSet resultSet = statement.getResultSet();
* 解析执行结果： resultSet.getMetaData().getColumnName(),获取sql返回的column列名
* 解析返回对象:通过反射判断method返回的是 Object / List<T> / Map 等类型，并找到对应的setterMethod方法 ,得到字段名与method的映射关系 Map<String,Method> methodMap
* 赋值封装结果：依次调用对应的并通过 TypeHandler<T> 类型处理器进行字段的赋值


###mybatis其他实现
* mapper.xml文件映射-方便了复杂sql的管理


###缺点
* 每个mapper都要进行定义配置，不够便捷



###在mybatis-spring中得到实现 ( TODO )
* 基于包路径扫描 @MapperScan
* 注册器通过包扫描，定了一组 BeanDefinition，注册到sprign容义器中
* 事务管理
* 连接池管理