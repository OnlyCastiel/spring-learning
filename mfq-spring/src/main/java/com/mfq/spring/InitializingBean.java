package com.mfq.spring;

/**
 * 模拟spring中bean的初始化的环节
 */
public interface InitializingBean {

    void afterPropertiesSet();
}
