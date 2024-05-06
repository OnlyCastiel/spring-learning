package com.mfq.user.service;


import com.mfq.spring.annotation.Component;
import com.mfq.spring.annotation.Scope;
import com.mfq.user.ServiceInterface;

@Component
@Scope("prototype")
public class OrderService implements ServiceInterface {

    private Integer num;


    public void test(){
        System.out.println("OrderService-test方法执行");
    }
}
