package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by 85074 on 2018/11/9.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 检查给定的参数是否存在
     * @param param
     * @param type
     * @return true：表示存在  false：表示不存在
     */
    @Override
    public Boolean check(String param, int type) {

        User user=new User();

        switch(type){
            case 1://用户名
                user.setUsername(param);
                break;
            case 2://电话
                user.setPhone(param);
                break;
            case 3://邮箱
                user.setEmail(param);
                break;
            default://默认用户名
                user.setUsername(param);
                break;
        }

        //false：表示不能用了，已经被占用
        //true：表示可以使用
        List<User> list = userMapper.select(user);
        return list.size()<=0;
    }

    @Override
    public String selectUser(String ticket) {
        //加密ticket
        String key="gqd_"+ticket;

        //这里要从redis里面获取用户的信息
        redisTemplate.opsForValue().get(key);
        return null;
    }

    @Override
    public int register(User user) {

        user.setCreated(new Date());
        user.setUpdated(new Date());

       String nesPassword= DigestUtils.md5DigestAsHex(user.getPassword().getBytes());

       user.setPassword(nesPassword);
        return userMapper.insert(user);
    }
}
