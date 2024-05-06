package com.mfq.springbootapi;

import com.mfq.springbootapi.config.IocConfig03;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 配置文件的读取
 */
public class Starter03 {


    public static void main(String[] args) {
        AnnotationConfigApplicationContext acContext = new AnnotationConfigApplicationContext(IocConfig03.class);

        IocConfig03 bean = acContext.getBean(IocConfig03.class);
        bean.showConfigInfo();
    }
}
