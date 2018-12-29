package com.itheima.cart.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.itheima.cart.CartCookieServie;
import com.itheima.cart.CartMergeService;
import com.itheima.pojo.Cart;
import com.itheima.pojo.User;
import com.itheima.service.CartService;
import com.itheima.util.RedisUtil;
import com.sun.org.apache.bcel.internal.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by 85074 on 2018/12/27.
 *
 */
@Service
public class CartMergeServiceImpl implements CartMergeService{

    @Reference
    private CartService cartService;

    @Autowired
    private CartCookieServie cartCookieServie;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void mergeCart(String ticket, HttpServletRequest request, HttpServletResponse response) {

        //1、redis的购物车
       // System.out.println("拿到的ticket要去购物车：" + ticket);
        User user = RedisUtil.findUserByTicket(redisTemplate,ticket);
        Long userId = user.getId();
        List<Cart> redisList = cartService.queryCartByUserId(userId);
        //2、cookie的购物车
        List<Cart> cookieList = cartCookieServie.findCookie(request);

        //3、遍历cookie的购物车，然后合并到redis的集合中
        for (Cart cart : cookieList) {

            //询问再redis集合中是否有这样的商品
            boolean flag = redisList.contains(cart);
            if(flag){
                //从redis中里面取出那个对象，然后修改数量
                int index = redisList.indexOf(cart);
                Cart c = redisList.get(index);
                c.setNum(cart.getNum()+c.getNum());
                c.setUpdate(new Date());
            }else {
                //表示redis中没有这件商品，就直接把cookie里面的商品加到redis中
                redisList.add(cart);
            }
        }
        //再把合并的购物车数据保存到redis中
        String json = new Gson().toJson(redisList);
        redisTemplate.opsForValue().set("gqd_"+userId,json);

        //5、如果已经合并了购物车，那么cookie的购物车就没必要存在了，把cookie的购物车清空掉，直接干掉cookie

        Cookie cookie = new Cookie("gqd_cart" , "");
        cookie.setMaxAge(0); //设置为0的话浏览器会把cookie删掉
        cookie.setPath("/");
        response.addCookie(cookie);

    }
}
