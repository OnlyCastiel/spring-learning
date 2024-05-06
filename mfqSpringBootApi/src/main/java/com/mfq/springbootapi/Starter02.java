package com.mfq.springbootapi;


import com.mfq.springbootapi.config.IocConfig01;
import com.mfq.springbootapi.dao.AccountDao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Bean方式实例化bean
 */
public class Starter02 {


    public static void main(String[] args) {
        //基于java配置类，启动上下文容器
        AnnotationConfigApplicationContext acContext = new AnnotationConfigApplicationContext(IocConfig01.class);

        Object accountDao = acContext.getBean("getAccountDao");
        AccountDao accountDao1 = acContext.getBean(AccountDao.class);
        System.out.println(accountDao);
        System.out.println(accountDao1);
    }
}
