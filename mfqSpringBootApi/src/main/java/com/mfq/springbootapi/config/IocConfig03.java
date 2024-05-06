package com.mfq.springbootapi.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.mfq.springbootapi")
@PropertySource(value = {"classpath:jdbc.properties","classpath:jdbc.properties"})
public class IocConfig03 {

    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;


    public void showConfigInfo(){
        System.out.println("jdbc.driver=" + driver);
        System.out.println("jdbc.url=" + url);
        System.out.println("jdbc.username=" + username);
        System.out.println("jdbc.password=" + password);
    }
}
