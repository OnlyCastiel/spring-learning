package com.mfq.springbootapi.service;


import com.mfq.springbootapi.dao.UserDao;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@Scope
public class UserService {

    @Autowired
    private UserDao userDao;


    public void test(){
        System.out.println("UserService 执行 test");
        userDao.test();
    }
}
