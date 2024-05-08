package com.mfq.proxy;

import com.mfq.user.UserServiceInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxyTest {

    public static void main(String[] args) {
        //jdk动态代理，根据接口实现，增加接口中的方法；局限在于仅能够通过接口的class进行方法调用
        UserServiceInterface instance =(UserServiceInterface) Proxy.newProxyInstance(JdkProxyTest.class.getClassLoader(),
                new Class[]{UserServiceInterface.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("before...");
                        //method.invoke(proxy,args);
                        System.out.println("after...");
                        return null;
                    }
                });
        instance.printA();
    }
}
