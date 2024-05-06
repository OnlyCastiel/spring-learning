package com.mfq.mapper;

import com.mfq.entity.User;
import mybatis.spring.Param;
import mybatis.spring.Select;

import java.util.List;

public interface UserMapper {

    @Select("select * from base_user where name = #{name} and age = #{age}")
    List<User> getUserList(@Param("name")String name,@Param("age") Integer age);

    @Select("select * from base_user where id = #{id}")
    User getUserById(@Param("id") Integer id);
}
