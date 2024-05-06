package com.mfq.springbootapi.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {


    @RequestMapping(value = "/hello")
    public ModelAndView hello(){
        //生成ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();
        //设置数据模型
        modelAndView.addObject("hello","hello world!");
        //设置视图，根据视图名称查找
        modelAndView.setViewName("hello");

        return modelAndView;
    }
}
