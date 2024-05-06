package com.mfq.spring;


public interface BeanPostProcessor {
    //初始化前对bean进行处理
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    //初始化后对bean进行处理
    default Object postProcessAfterInitialization(Object bean, String beanName){
        return bean;
    }
}
