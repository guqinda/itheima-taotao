package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 85074 on 2018/11/9.
 */
@Controller
//@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    //注册时校验用户名
    //param：表示检查是否存在的值，type：这个值是什么类型，  1：用户名  2：手机  3：邮箱
    @RequestMapping("/user/check/{param}/{type}")
    public ResponseEntity<String> check(@PathVariable  String param,@PathVariable int type,String callback){

        try {
            System.out.println("要检测用户名是否可用" + param + ":" + type);
            Boolean exist = userService.check(param, type);
            // String result=callback+"("+exist+")";
            String result="";

            if(!StringUtils.isEmpty(callback)){
                result=callback+"("+exist+")";
            }else {
                result=exist+"";
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @GetMapping("/user/{ticket}")
    public ResponseEntity<String> selectUser(@PathVariable  String ticket){

        try {
            String result = userService.selectUser(ticket);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("该用户未登录");
        }
    }

}
