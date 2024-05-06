package com.mfq;

import com.mfq.user.UserService;
import mybatis.spring.MybatisMapperScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@ComponentScan
@MybatisMapperScan("com.mfq.mapper")
public class App
{
    public static void main( String[] args )
    {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(App.class);

//        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
//        beanDefinition.setBeanClass(MybatisFactoryBean.class);
//        applicationContext.registerBeanDefinition("userMapper",beanDefinition);
        applicationContext.refresh();//必须要显示刷新容器


        UserService bean = applicationContext.getBean(UserService.class);
        bean.test();

//        Object obj1 = applicationContext.getBean("mybatisFactoryBean");
//        Object obj2 = applicationContext.getBean("&mybatisFactoryBean");
//        //Object obj3 = applicationContext.getBean("user");
//        //Object obj4 = applicationContext.getBean(User.class);
//        Object obj5 = applicationContext.getBean(MybatisFactoryBean.class);
//
//        System.out.println(obj1);
//        System.out.println(obj2);
//        //System.out.println(obj3);
//        //System.out.println(obj4);
//        System.out.println(obj5);


    }
}
