package com.itheima.service;

import com.itheima.pojo.User;

/**
 * Created by 85074 on 2018/10/13.
 */
public interface UserService {

   Boolean  check(String param,int type);

  String selectUser(String ticket);

  //执行注册功能
  int register(User user);

  //登录功能
  String  login(User user);

  //登录成功显示用户名
  User findUser (String ticket);

}
