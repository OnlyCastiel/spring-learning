package com.mfq.springbootapi.webConfig;


import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


/**
 * WebInitializer实现了WebApplicationInitializer
 * 而WebApplicationInitializer由 tomcat启动时，实现的spi机制，扫描包路径下的制定文件，加载类 （Servlet容器都有此约定机制）
 * 扫描到spring-web.jar包下/META-INF/services/javax.servlet.ServletContainerInitializer文件
 * 其中定义了ServletContainerInitializer的实现类org.springframework.web.SpringServletContainerInitializer
 * 而这个实现类，运行过程会通过加载 web.xml 或 WebApplicationInitializer的实现类 来进行web容器启动；可以单独使用其中一种方式或同时结合使用
 *
 * servlet容器，例如tomcat会加载 ServletContainerInitializer实现类，具体到spring-web中就是SpringServletContainerInitializer
 * 然后执行其 onStartup方法，传递参数有  WebApplicationInitializer实现类 + servletContext容器
 * 然后循环调用WebApplicationInitializer实现类的onStartup方法；
 *
 * 疑问来了，tomcat如何收集WebApplicationInitializer实现类的信息，作为参数进行传递呢，解决办法是  @HandlesTypes
 * @HandlesTypes 注解有什么作用呢？
 * Servlet容器在启动应用的时候，会将@HandlesTypes注解里面指定的类型下面的子类，
 * 包括实现类或者子接口等，全部给我们传递过来。
 *
 */
public class WebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        AnnotationConfigWebApplicationContext acwContext = new AnnotationConfigWebApplicationContext();
        //基于java配置类，加载指定的spring上下文，sprign容器
        acwContext.register(MvcConfig01.class);
        //设置Servlet上下文信息
        acwContext.setServletContext(servletContext);
        //定义转发器Dispatcher
        DispatcherServlet servlet = new DispatcherServlet(acwContext);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", servlet);
        //设置映射路径
        dispatcher.addMapping("/");
        //启动时实例化Bean
        dispatcher.setLoadOnStartup(1);

    }
}
