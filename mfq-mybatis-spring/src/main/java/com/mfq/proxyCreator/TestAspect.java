package com.mfq.proxyCreator;


import org.springframework.stereotype.Component;

@Aspect
@Component
public class TestAspect {

    @Before("execution(public void com.zhouyu.service.UserService.test())")
    public void zhouyuBefore(JoinPoint joinPoint) {
        System.out.println("Before...");
    }
}
