package com.user;


//import com.mfqspringboot.MfqSpringApplication;
//import com.mfqspringboot.MfqSpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //标准springboot启动注解
//@MfqSpringBootApplication
public class Starter {

    public static void main(String[] args) {

        //标准springboot启动方式
        SpringApplication.run(Starter.class);

        //自定义springboot启动类
        //MfqSpringApplication.run(Starter.class);


    }
}
