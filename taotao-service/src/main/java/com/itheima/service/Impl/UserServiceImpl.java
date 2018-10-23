package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.service.UserService;

/**
 * Created by 85074 on 2018/10/24.
 */
@Service//使用dubbo的注解
public class UserServiceImpl implements UserService {
    @Override
    public void save() {
        System.out.println("UserService save方法");
    }
}
