package com.itheima.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 85074 on 2018/10/25.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public  String  index(){

        return "index";
    }
}
