package com.mfq.proxy;

import com.mfq.user.UserService;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

public class CglibPorxyTest {




    public static void main(String[] args) {
        //基础方式生成UserService的对象
        UserService target = new UserService();
        target.printA();

        //cglib方式生成代理对象，对UserService的方法进行增强
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserService.class);
        enhancer.setCallbacks(new Callback[] {new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                //可以区分 method上的注解等内容，方法名等等，判定需要执行的增强内容；
                //method.isAnnotationPresent()

                System.out.println("before....");
                Object invoke = methodProxy.invokeSuper(o, objects);//实际调用被代理类的方法；
                System.out.println("after....");
                return invoke;
            }
        }});
        enhancer.setCallbackFilter(new CallbackFilter() {
            @Override
            public int accept(Method method) {
                //可以根据method相关信息，判断返回对应的拦截器（数组角标）
                return 0;
            }
        });

        UserService userService = (UserService) enhancer.create(); //生成代理类
        userService.printA();
    }
}
