package com.mfq.springbootapi;


import org.springframework.context.annotation.ComponentScan;


/**
 * @ComponentScan 等于spring3.0前xml中的<context:annotation-config/>
 */
@ComponentScan("com.mfq.*")
public class SpringConfig {
}
