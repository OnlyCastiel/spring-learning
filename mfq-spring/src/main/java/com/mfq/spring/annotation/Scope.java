package com.mfq.spring.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 模拟spring中bean的作用域的定义，
 * 此处简化仅实现 原型(prototype)  单例(singleton)
 * 	 * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE
 * 	 * @see ConfigurableBeanFactory#SCOPE_SINGLETON
 * 	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST
 * 	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {

    String value() default "singleton";
}
