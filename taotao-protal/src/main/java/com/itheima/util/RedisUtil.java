package com.itheima.util;

import com.google.gson.Gson;
import com.itheima.pojo.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

/**
 * Created by 85074 on 2018/12/27.
 */
public class RedisUtil {

    public static User findUserByTicket(RedisTemplate<String,String> template,String ticket){
        //从redis里面获取了用户的信息。
        String json = template.opsForValue().get(ticket);

      //  System.out.println("redis里面获取了用户的信息Json===" + json);
        User user = null;

        //user不为空才new出来
        if(!StringUtils.isEmpty(json)){
            user= new Gson().fromJson( json ,User.class);
        }

        //json ----> user对象
        return user;
    }
}
