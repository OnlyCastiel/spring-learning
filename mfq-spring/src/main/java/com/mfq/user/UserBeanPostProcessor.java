package com.mfq.user;

import com.mfq.spring.BeanPostProcessor;
import com.mfq.spring.annotation.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


@Component
public class UserBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        /**
         * TODO 实际spring中，使用的cglib实现的动态代理，此处通过jdk-proxy实现的动态代理有诸多局限
         * 例如：jdk-proxy通过接口实现的代理，只能通过接口进行引用，只能对接口方法进行增强
         * 而通过cglib继承关系实现的代理，可以使用本类进行引用，对本类中定义的方法进行aop增强
         */
        //此处仅以UserService进行特殊举例
        if(!beanName.equals("UserService")){
            return bean;
        }
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        //所有bean的后置处理器，spring通过这个机制实现AOP
        Object instance = Proxy.newProxyInstance(UserBeanPostProcessor.class.getClassLoader(), interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("切面方法执行");
                //执行实际普通方法，特别注意不要执行proxy方法，否则会产生递归调用
                return method.invoke(bean,args);
            }
        });

        return instance;
    }
}
