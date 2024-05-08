package com.mfq.proxy;


import com.mfq.user.UserService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.Method;

/**
 * 在spring中，直接通过 ProxyFactory 封装好了代理类的生成，使用起来更加方便
 *
 * 而ProxyFactoryBean 则代表着告诉spring,生成的代理对象为一个bean
 */
public class SpringPorxyTest {


    public static void main(String[] args) {
        UserService userService = new UserService();

        ProxyFactory factory = new ProxyFactory();

        factory.setTarget(userService);

        factory.addAdvice(new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                System.out.println("before...");
            }
        });


        factory.addAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                System.out.println("方法执行around前");
                Object proceed = invocation.proceed();
                System.out.println("方法执行around后");
                return proceed;//返回方法执行结果
            }
        });

        //方法返回后advice
        factory.addAdvice(new AfterReturningAdvice() {
            @Override
            public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
                System.out.println("返回后执行....");
            }
        });

        //方法异常时advice--例如事务回滚
        factory.addAdvice(new ThrowsAdvice() {
            public void afterThrowing(Method method,Object[] args,  Object target,Exception e){
                System.out.println("方法抛出异常时执行....");
            }
        });


        UserService proxy =(UserService) factory.getProxy();
        proxy.printA();
        proxy.test(); //可以执行非接口定义的方法并增强（对比jdk动态代理的优势）
    }
}
