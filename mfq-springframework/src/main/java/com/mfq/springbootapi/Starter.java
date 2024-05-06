package com.mfq.springbootapi;

import com.mfq.springbootapi.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Starter {


    public static void main(String[] args) {

        /*1、容器初始化*/
        //方法一：spring 3.0以前得到spring的上下文环境-根据xml配置文件生成容器
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        //方法二：spring 3.0之后可以用过 XXXConfig.class  配置文件的方式生成容器（代替xml文件）
        //AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        /*验证：查看容器内的bean*/
        UserService userService = (UserService) context.getBean("userService");
        System.out.println(userService);
        System.out.println(userService.getUserDao());
    }
}
