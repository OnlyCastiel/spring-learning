package com.mfq;

import com.mfq.entity.User;
import com.mfq.mapper.UserMapper;
import mybatis.spring.MapperProxyFactory;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        UserMapper mapper = MapperProxyFactory.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserList("王宝强",48);

        System.out.println(userList);

        User userById = mapper.getUserById(1);
        System.out.println(userById);

        System.out.println("执行结束");
    }
}
