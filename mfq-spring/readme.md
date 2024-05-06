###spring底层原理--源码解析--容器启动过程

####spring容器（spring-framework）启动过程
* 加载配置类/配置文件
* 读取配置中的包扫描路径，完成扫描，
* 基于包扫描，遍历完成bean的创建
* bean创建完成后，统一放入单例池中进行维护
```
详见MfqSpringApplicationContext中的模拟实现
```

####bean的生命周期
* 推断构造方法（无参、有参、注解声明）
* 执行构造方法，获取普通对象
* 进行依赖注入（DI/IOC）
* 初始化前操作：BeanPostProcessor
* 初始化方法：实现InitializingBean接口，重写afterPropertiesSet()方法
* 初始化后操作：BeanPostProcessor--AOP
* 放入单例池

###其他技术要点
1、三级缓存解决循环依赖
2、spring中各种Aware的实现方式，ApplicationContextAware/BeanNameAware
3、spring通过cglib集成实现代理类的生成

###细化知识点
####1、扫描的细化过程
* 扫描启动类，解析启动类`@ComponentScan`中声明的路径，或者默认为启动类所在路径，并基于此解析到target包的绝对路径，获取到文件路径下的所有class结尾的文件，处理子文件递归的情况
* 基于class文件列表，解析到className,并通过启动类AppClassLoader加载，解析class的定义
* 分析class的定义,例如`scope`的类型，单例`singleton`或者原型`prototype`等，beanName、beanClass等等，维护Map<beanName,BeanDifinition> banDifinitionMap 
* 维护List<BeanPostProcessor> ，判断类是否为BeanPostProcessor的实现类，这些组件直接进行实例化（不放入beanDifinitionMap中），参加后续bean实例化过程中的操作



####2、创建bean的细化过程
* 遍历banDifinitionMap，创建bean,判断scope,仅创建`singleton`的bean----此处省略了推断构造方法的过程，而是使用默认的无参构造
* 遍历class的filed[],对有`@Autowired`注解的成员变量进行赋值，调用getBean(String beanName)方法
* 执行所有BeanPostProcessor的初始化前的处理----spring中各种Aware的实现方式(ApplicationContextAwareProcessor implements BeanPostProcessor中调用)
* 执行bean自行定义的InitializingBean的初始化操作
* 执行所有BeanPostProcessor的初始化后的处理----AOP的实现方式
* 执行完成后，讲bean放入到单例池中`Map<String,Object> singletonObjects`----此处略过三级缓存的细节实现


1、BeanPostProcessor 其中包含了两个方法
```java
public interface BeanPostProcessor {
    //初始化前对bean进行处理
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    //初始化后对bean进行处理
    default Object postProcessAfterInitialization(Object bean, String beanName){
        return bean;
    }
}
```
spring会在容器启动时，遍历所有的`@Component`组件，如果是`BeanPostProcessor`的实现类，则代表这个组件是特殊的，
会在所有bean完成参数注入（DI/IOC）之后，对bean进行处理
```java
    //创建容器内的bean
    public Object createBean(BeanDefinition beanDefinition){
        try {
            Class clazz = beanDefinition.getClazz();
            //获取构造方法 ,TODO 后续需要调整为推断构造方法
            Object instance = clazz.newInstance();
            //设置属性值(DI/IOC)
            for(Field field : clazz.getDeclaredFields()){
                if(field.isAnnotationPresent(Autowired.class)){
                    field.setAccessible(true);
                    field.set(instance,getBean(field.getType().getSimpleName()));
                }
            }

            //实现postProcessor机制--例如AOP
            for(BeanPostProcessor beanPostProcessor : beanPostProcessorList){
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanDefinition.getBeanName());
            }

            //实现initializing机制--接口实现
            if(instance instanceof InitializingBean){
                ((InitializingBean) instance).afterPropertiesSet();
            }

            for(BeanPostProcessor beanPostProcessor : beanPostProcessorList){
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanDefinition.getBeanName());
            }
            
            //放入单例池 TODO 单例池可以通过多种方式获取，比如 name/type
            singletonObjects.put(beanDefinition.getBeanName(),instance);

            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
```

####3、获取bean方法
以getBean(String beanName)举例
* 根据beanName获取Bean的BeanDifinition定义
* 判断`scope`作用域，如果是单例的，从`singletonObjects`单例池中获取；如果获取为空，证明还未创建，则进行创建
* 如果是非单例的，比如原型模式，则根据BeanDifinition直接创建一个新的bean进行返回



####4.怎么理解spring容器
* springContext/BeanFactory/BeanDifinitionMap/单例池singletonObjects都可以理解为spring容器
* 核心思想是，通过一整套设计，实现并维护了bean的容器，对外提供了bean的生命周期，bean的注册，获取等实现
* 



####5.BeanFactory底层原理
getObject()
getType()

applicationContext.getBean()方法中，做了什么样的特殊处理，
判断一个类是否继承于BeanFactory，是则替换为getObject()返回的对象

作用举例 实现mapper的动态代理，获取到实际的mapper对象，可以通过数据库连接执行sql,而用户自定义的mapper只是一个接口+sql
getObject()返回的对象 不经过完整的bean的生命周期
```java
public class MybatisFactoryBean implements FactoryBean {
    
    @Override
    public Object getObject() throws Exception {
        return null;
    }
    
    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
```

* getBean("mybatisFactoryBean");  //获取getObject()返回的bean,type为getType()，而该type也会作为bean提供给其他bean进行依赖注入
* getBean("&mybatisFactoryBean"); //获取实际的BeanFactory的bean对象














