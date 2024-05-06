package com.mfq.springbootapi.service;

import com.mfq.springbootapi.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Scope()
public class UserService {

    @Resource //按照名称装配，id,然后根据type装配
    private UserDao userDao;

    @Autowired //按照类型匹配，匹配不上则用名称
    private UserDao userDao1;

    public UserDao getUserDao() {
        return userDao;
    }
}
