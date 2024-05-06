package com.mfq.user;

import com.mfq.mapper.OrderMapper;
import com.mfq.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper OrderMapper;

    public void test(){
        userMapper.getUserById(11);
        OrderMapper.getOrder();
    }
}
