package com.mfqspringboot.WebServer;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class TomcatWebServerConditional implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            Class<?> aClass = context.getBeanFactory().getBeanClassLoader().loadClass("org.apache.catalina.startup.Tomcat");
            //存在类
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
