package com.mfqspringboot;


import com.mfqspringboot.WebServer.WebServerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动注解标注启动类
 * 对应springboot中 @SpringBootApplication
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration //springframework中注解，标志为一个自动配置类
@ComponentScan //springframework中注解，开启自动bean扫描
//对于非用户包路径下的自动配置类，需要显示引用，------或者通过SPI机制进行引入(spring通过spi引入大量默认配置类)
@Import(WebServerAutoConfiguration.class)
public @interface MfqSpringBootApplication {
}
