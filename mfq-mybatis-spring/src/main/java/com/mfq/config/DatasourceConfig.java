package com.mfq.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class DatasourceConfig {


    /**
     *获得sessionFactory
     * 1.getSqlSessionFactory()获得SqlSessionFactory
     * 2.sqlSessionFactory.openSession()获得session
     * 3.sqlSession.getMapper(DepartmentMapper.class)获得mapper
     * 4.mapper.getDepartmentById(1) 执行查询操作。
     */
    @Bean
    public  SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return  sqlSessionFactory;
    }

}
