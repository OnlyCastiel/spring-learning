package com.mfq.springbootapi.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration//（springframework注解）：标记当前类为一个配置类
@ComponentScan("com.mfq.springbootapi") //（springframework注解）设置扫描包的路径
public class IocConfig01 {
}
