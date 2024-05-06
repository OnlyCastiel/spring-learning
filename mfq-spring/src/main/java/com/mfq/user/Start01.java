package com.mfq.user;

import com.mfq.spring.MfqSpringApplicationContext;
import com.mfq.spring.annotation.ComponentScan;
import com.mfq.user.service.UserService;


@ComponentScan("com.mfq.user")
public class Start01 {

    public static void main(String[] args) {


        MfqSpringApplicationContext applicationContext = new MfqSpringApplicationContext(Start01.class);

        applicationContext.run();

        ServiceInterface userService = (ServiceInterface) applicationContext.getBean("UserService");
        userService.test();

        //报错情况
        UserService userService01 = (UserService) applicationContext.getBean("UserService");
        //com.sun.proxy.$Proxy5 cannot be cast to com.mfq.user.service.UserService
        userService01.test();//报错，此处容器内，实际bean为通过jdk-proxy动态代理生成的ServiceInterface
        userService01.testWithoutInterface();

    }
}
