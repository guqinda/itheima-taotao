package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.itheima.cart.CartCookieServie;
import com.itheima.pojo.Cart;
import com.itheima.pojo.User;
import com.itheima.service.CartService;
import com.itheima.util.CookieUtil;
import com.itheima.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by 85074 on 2018/12/26.
 */
@Controller
public class CartController {

    @Reference
    private CartService cartService;

    @Autowired
    private CartCookieServie cartCookieServie;

    @Autowired
    private RedisTemplate<String,String> template;

    @RequestMapping("cart/add/{id}.html")
    public String addToCart(@PathVariable long id, int num, HttpServletRequest request, HttpServletResponse response){

        //之前的登录系统，只有登录成功，那么用户的信息就会保存到redis中
        //在用户的cookie里面保存了redis的key，作为登录的凭证
        String ticket = CookieUtil.findTicket(request);

        if(ticket!=null){
            //从redis里面获取了用户的信息
            String json = template.opsForValue().get(ticket);

            //json  ---->user对象
           User user= new Gson().fromJson(json, User.class);
            cartService.addToCart(user.getId(),id,num);

        }else{
            //没有登录  --就不能使用redis --购物车的商品得放到cookie里面去
            cartCookieServie.addToCart(id,num,request,response);
        }

        return "cartSuccess";
    }

    //http://www.taotao.com/cart/cart.html
    @RequestMapping("/cart/cart.html")
    public String show(HttpServletRequest request, Model model) {

        String ticket = CookieUtil.findTicket(request);
        List<Cart> cartList=null;
        //1先获取用户id
        if(ticket!=null) {
            //从redis里面获取了用户的信息
            String json = template.opsForValue().get(ticket);

            //json  ---->user对象
            User user = new Gson().fromJson(json, User.class);

            cartList = cartService.queryCartByUserId(user.getId());

        }else{
            //没有登录
            cartList = cartCookieServie.findCookie(request);

        }

        model.addAttribute("cartList",cartList);

        //去redis中获取这个用户的购物车数据
        return "cart";
    }

    /**
     * 由于上面给的注解是：@Controller ，所以所有的方法不管有返回值还是没有返回值
     * 都会被认为是要跳转页面，如果有返回值就跳具体的页面，没有返回值就跳转默认的根路径 /
     * @param id
     * @param num
     * @param request
     */
    @RequestMapping("/service/cart/update/num/{id}/{num}")
    @ResponseBody
    public void updateNumByCart(@PathVariable long id ,@PathVariable int num,HttpServletRequest request,HttpServletResponse response){
        String ticket = CookieUtil.findTicket(request);
        if(ticket!=null){
            String json = template.opsForValue().get(ticket);

            //json  ---->user对象
            User user = new Gson().fromJson(json, User.class);
            cartService.updateNumByCart(user.getId(),id,num);
        }else {
            //没有登录
            cartCookieServie.updateCartByCookie(id,num,request,response);
        }
    }

    @RequestMapping("/cart/delete/{id}.shtml")
    public String deleteItemByCart(@PathVariable long id,HttpServletRequest request,HttpServletResponse response){
        String ticket = CookieUtil.findTicket(request);

        if(ticket!=null) {
            User user = RedisUtil.findUserByTicket(template, ticket);

            cartService.deleteItemByCart(user.getId(),id);
        }else {
            cartCookieServie.deleteByCookie(id,request,response);
        }

        //重新定位到查询购物车的方法
        return "redirect:/cart/cart.html";
    }
}
