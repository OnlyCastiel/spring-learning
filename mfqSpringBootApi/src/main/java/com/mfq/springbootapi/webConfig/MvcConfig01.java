package com.mfq.springbootapi.webConfig;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * MVC 控制类
 */

@Configuration
//在@Configuration注解的配置类中添加，用于为改应用添加springMVC的功能
@EnableWebMvc
@ComponentScan("com.mfq.springbootapi")
public class MvcConfig01 {

    /**
     * 配置JSP视图解析器，属于第三方组件，必须要通过@Bean方式显示注入容器--单例
     */
    @Bean
    public InternalResourceViewResolver viewResolver(){
        //实例化视图解析器
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //设置前缀
        viewResolver.setPrefix("/WEB-INF/views");
        //设置后缀
        viewResolver.setSuffix(".jsp");
        //返回视图解析器，交给IOC容器进行管理
        return viewResolver;
    }

    //通过bean定义了视图解析器，也需要定义一个bean去实现servlet容器-替换web.xml
    //WebApplicationInitializer
    //javax.servlet.ServletContainerInitializer



}
