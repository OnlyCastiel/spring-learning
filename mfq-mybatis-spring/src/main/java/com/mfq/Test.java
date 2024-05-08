package com.mfq;

import com.mfq.entity.User;
import com.mfq.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class Test {

    public static void main(String[] args) throws Exception {
        /**
         * mybatis执行sql的简要过程
         * spring-mybatis通过管理哪些bean来实现这一流程自动化
         *
         */


        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        //通过jdk动态代理获取mapper的代理对象，而spring-mybatis则要将这些bean注册到spring容器中，以供调用
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        User userById = mapper.getUserById(1);

        sqlSession.commit();
        sqlSession.flushStatements();
        sqlSession.close();

        return;
    }
}
