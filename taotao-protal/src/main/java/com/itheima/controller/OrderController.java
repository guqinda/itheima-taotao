package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Cart;
import com.itheima.pojo.User;
import com.itheima.service.CartService;
import com.itheima.util.CookieUtil;
import com.itheima.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.soap.Addressing;
import java.util.List;

/**
 * Created by 85074 on 2018/12/29.
 */
@Controller
public class OrderController {

    @Reference
    private CartService cartService;

    @Autowired
    private RedisTemplate<String ,String> template;

    @RequestMapping("/order/order-cart.shtml")
    public String create(HttpServletRequest request, Model model){

        //判断登录


        //1、获取购物车的 商品
//        String ticket = CookieUtil.findTicket(request);
//        User user = RedisUtil.findUserByTicket(template, ticket);
        //不需要再查了，直接从拦截器那里拿
        User user = (User) request.getAttribute("user");
        List<Cart> carts = cartService.queryCartByUserId(user.getId());

        //2、存储到model中
        model.addAttribute("carts",carts);

        //3、到页面显示即可

        return "order-cart";

    }
}
