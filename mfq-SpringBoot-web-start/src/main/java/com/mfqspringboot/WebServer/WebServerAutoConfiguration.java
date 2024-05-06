package com.mfqspringboot.WebServer;

import com.mfqspringboot.annotation.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebServerAutoConfiguration {

    //简单方式配置条件注解
//    @Bean
//    @Conditional(TomcatWebServerConditional.class)
//    public TomcatWebServer getTomcatWebServer(){
//
//        return new TomcatWebServer();
//    }


    @Bean
    @ConditionalOnClass("org.apache.catalina.startup.Tomcat")
    public TomcatWebServer getTomcatWebServer(){
        return new TomcatWebServer();
    }

    @Bean
    @ConditionalOnClass("org.eclipse.jetty.server.Server")
    public JettyWebServer getJettyWebServer(){
        return new JettyWebServer();
    }



}
