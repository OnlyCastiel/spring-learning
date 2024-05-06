package com.mfq.springbootapi.config;


import com.mfq.springbootapi.dao.AccountDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.mfq.springbootapi")
public class IocConfig02 {

    /**
     * @Bean 注解在方法上，有IOC负责进行调用并初始化；
     * bean的名字为方法名，区分大小写,也是一个单例bean
     *
     * 使用场景，spring集成第三方组件，数据库DataSource、json解析器，视图解析器
     * 本身是第三方的东西，需要显式的定义到spring容器中
     * 原本在spring中的xml中需要显示定义的bean，可以通过配置类来进行配置，节省xml，利于维护
     *
     * 第三方组件中，默认是没有配置注解（默认不生效，避免资源浪费，即便增加了注解也会因为包路径扫描问题无法扫到）
     * 因而此时当应用需要引入该组件时，则需要显示进行注入
     *
     * 思考：后续在springboot中，如何实现引入包则自动引入对应bean
     * @return
     */
    @Bean
    public AccountDao getAccountDao(){
        return new AccountDao();
    }

}
