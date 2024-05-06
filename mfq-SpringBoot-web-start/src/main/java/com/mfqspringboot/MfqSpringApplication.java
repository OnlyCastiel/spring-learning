package com.mfqspringboot;

import com.mfqspringboot.WebServer.WebServer;
import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Map;

/**
 * 自定义springboot启动类，模拟真实的springboot以了解真实的springboot的启动流程
 *
 * 1、一个run方法，支持传入一个配置类，根据配置类，启动对应的内容
 * 2、根据包引入情况，启动tomcat容器或者jetty容器
 * 3、读取用户自定义的配置文件，如果存在则按照用户定义的参数启动，不存在则按照默认参数启动
 */
public class MfqSpringApplication {


    /**
     * 传入类，添加注解，自定义的springboot启动类注解，
     * 是一个组合注解，组合注解包含：包扫描
     * @param clazz
     */
    public static void run(Class clazz){


        //容器启动--创建一个spring容器
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        //指定配置类，包扫描路径
        applicationContext.register(clazz);
        //容器注入bean
        applicationContext.refresh();


        //启动tomcat
        //startTomcat(applicationContext);
        //切换jetty---排除tomcat依赖，引入jetty依赖
        //classpath判断包依赖，选择tomcat/jetty
        Map<String, WebServer> webServers = applicationContext.getBeansOfType(WebServer.class);
        if(webServers.isEmpty()){
            throw new NullPointerException();
        }
        if(webServers.size() > 1){
            throw new NullPointerException();
        }
        webServers.values().stream().findFirst().get().start(applicationContext);



        //编译期间，过程中，找到对应类写入对应文件中--》默认配置文件 --》启动根据此默认配置启动
        //端口号，applicationName路径、等等
        //AbstractProcessor

    }


    //容器传入，tomcat启动DispatcherServlet(父容器) 需要spring容器（子容器）作为参数
    //绑定映射关系，请求路径
    private static void startTomcat(WebApplicationContext webApplicationContext){


//        Tomcat tomcat = new Tomcat();
//
//        Server server = tomcat.getServer();
//        Service serviec = server.findService("Tomcat");
//
//        Connector connector = new Connector();
//        connector.setPort(8080);
//
//        Engine engine = new StandardEngine();
//        engine.setDefaultHost("localhost");
//
//        Host host = new StandardHost();
//        host.setName("localhost");
//
//
//        Context context = new StandardContext();
//        context.setPath("");
//        context.addLifecycleListener(new Tomcat.FixContextListener());
//
//        host.addChild(context);
//        engine.addChild(host);
//
//        serviec.setContainer(engine);
//        serviec.addConnector(connector);
//
//        DispatcherServlet servlet = new DispatcherServlet(webApplicationContext);
//        tomcat.addServlet(context.getPath(),"dispatcher",servlet);
//        context.addServletMappingDecoded("/*","dispatcher");
//
//        try {
//            tomcat.start();
//        } catch (LifecycleException e) {
//            e.printStackTrace();
//        }

    }

    private Class configClass;


    public MfqSpringApplication(Class configClass) {
        this.configClass = configClass;
    }
}
