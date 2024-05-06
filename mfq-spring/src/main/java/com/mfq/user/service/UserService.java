package com.mfq.user.service;


import com.mfq.spring.InitializingBean;
import com.mfq.spring.annotation.Autowired;
import com.mfq.spring.annotation.Component;
import com.mfq.spring.annotation.Scope;
import com.mfq.user.ServiceInterface;

import javax.annotation.PostConstruct;

@Component
@Scope("singleton")
public class UserService implements ServiceInterface , InitializingBean {


    @Autowired
    private OrderService orderService;


    public UserService() {
        System.out.println("UserService-构造方法执行");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("UserService-初始化方法执行");
    }


    public void test(){
        System.out.println("UserService-test方法执行");
        System.out.println(orderService);
    }


    public void testWithoutInterface(){
        System.out.println("UserService-非继承方法方法执行");
    }

}
