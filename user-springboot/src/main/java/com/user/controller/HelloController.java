package com.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class HelloController {


    @RequestMapping("/hello")
    public String helloWorld(String name){
        return "helloWorld" + name;
    }


    public static void main(String[] args) {
        List<Integer> nums = new ArrayList<>();
        nums.add(1);
        nums.add(2);
        nums.add(3);
        nums.add(4);
        nums.add(5);

        List<Integer> integers = nums.subList(0, 5);
        System.out.println(Arrays.toString(integers.toArray()));
    }

}
