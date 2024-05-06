package com.mfq.springbootapi;


import com.mfq.springbootapi.config.IocConfig01;
import com.mfq.springbootapi.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * 基于spring 5.x版本，可以实现无xml开发，由配置类指定包扫描路径---简化xml-替换了spring.xml
 * 0配置获取bean对象
 */
public class Starter01 {

    public static void main(String[] args) {
        //基于java配置类，启动上下文容器
        AnnotationConfigApplicationContext acContext = new AnnotationConfigApplicationContext(IocConfig01.class);

        //得到bean对象
        UserService bean = acContext.getBean(UserService.class);
        //执行方法
        bean.test();
    }
}
