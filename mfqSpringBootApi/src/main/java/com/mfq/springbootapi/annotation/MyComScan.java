package com.mfq.springbootapi.annotation;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;


/**
 * 拥有元注解  Java 5 定义了 4 个注解 (@Target/@Retention/@Documented/@Inherited) + Java 8 (@Repeatable 和 @Native)
 * @Configuration
 * @ComponentScan ：需要覆盖value属性
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)  //核心注解，注解保留
@Documented
@Configuration
@ComponentScan
public @interface MyComScan {

    //通过value属性设置扫描包的范围
    String[] value() default {};
}
