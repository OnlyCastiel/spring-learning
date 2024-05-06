package com.mfq.springbootapi;

import com.mfq.springbootapi.config.IocConfig04;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Starter04 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext acContext = new AnnotationConfigApplicationContext(IocConfig04.class);

        IocConfig04 bean = acContext.getBean(IocConfig04.class);
        bean.showConfigInfo();

        //BeanPostProcessor

    }
}
