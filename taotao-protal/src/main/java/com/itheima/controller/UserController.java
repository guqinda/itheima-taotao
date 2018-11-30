package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.User;


import com.itheima.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 85074 on 2018/11/20.
 * 用户相关的controller
 */
@RestController
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping("/user/doRegister")
    @ResponseBody
    public  Map<String,Integer> register(User user){

        Map<String,Integer> map=new HashMap<String ,Integer>();

        int result=userService.register(user);

        if(result>0){
            map.put("status",200);
        }else {
            map.put("status",500);
        }

        System.out.println("result==" + result);

        return map;
    }

    @RequestMapping("/user/doLogin")
    @ResponseBody
    public   Map<String ,String >  login(User user, HttpServletResponse response){

        Map<String ,String > map=new HashMap<String ,String>();

        String ticket = userService.login(user);

        if(ticket !=null){
            //登录成功了
            Cookie cookie=new Cookie("ticket",ticket);

            cookie.setMaxAge(60*60*24*7);
            cookie.setPath("/");

            response.addCookie(cookie);

            map.put("status","200");
            map.put("success","http://www.taotao.com");
        }
        return map;
    }


}
