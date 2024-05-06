package com.mfqspringboot.WebServer;

import org.springframework.web.context.WebApplicationContext;

public class JettyWebServer implements WebServer{

    @Override
    public void start(WebApplicationContext webApplicationContext) {

        System.out.println("jetty start");

        //TODO 启动jetty 容器

    }
}
