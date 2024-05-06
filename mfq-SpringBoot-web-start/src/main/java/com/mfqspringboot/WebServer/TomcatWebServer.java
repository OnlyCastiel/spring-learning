package com.mfqspringboot.WebServer;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class TomcatWebServer implements WebServer{

    @Override
    public void start(WebApplicationContext webApplicationContext) {
        System.out.println("tomate start");

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
}
