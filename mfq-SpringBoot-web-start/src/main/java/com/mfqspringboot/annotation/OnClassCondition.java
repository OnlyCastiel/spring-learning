package com.mfqspringboot.annotation;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 自定义条件类实现
 * 根据项目中是否存在对应class实例化来对应的bean
 */
public class OnClassCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //获取注解上的metadata
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnClass.class.getName());
        //获取注解的参数
        String[] name =(String[])  annotationAttributes.get("value");
        //生命的条件类
        List<String> list = Arrays.asList(name);
        for(String className : list){
            try {
                context.getBeanFactory().getBeanClassLoader().loadClass(className);
            } catch (ClassNotFoundException e) {
                return false;
            }
        }
        return true;
    }
}
