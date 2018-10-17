package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 85074 on 2018/10/13.
 */
@RestController
public class UserController {

    //它只能在当前项目的Spring容器中寻找UserService这个接口的实现类，注入进来
    //@Autowired
    //@Reference
    private UserService userService;

    @RequestMapping("save")
    public  String save(){
        System.out.println("save方法");

        userService.save();
        return  "save successful";
    }
}
