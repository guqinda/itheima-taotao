package com.itheima.cart.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.itheima.cart.CartCookieServie;
import com.itheima.pojo.Cart;
import com.itheima.pojo.Item;
import com.itheima.service.ItemSercive;
import com.itheima.util.CookieUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 85074 on 2018/12/27.
 */
@Service
public class CartCookieServiceImpl implements CartCookieServie {

    @Reference
    private ItemSercive itemSercive;

    /**
     * 未登录做的添加购物车  重载的方法
     * @param itemId
     * @param num
     * @param request
     */
    @Override
    public void addToCart(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {

            //1、从request里面获取cookie，然后遍历cookie，取出以前的购物车商品
            List<Cart> cartList = CookieUtil.findCartByCookie(request);
            //没有购物车
            if(cartList==null) {
                //创建购物车集合
                cartList=new ArrayList<>();
            }

            //2、遍历购物车，然后判断是否有重复的商品，如果有，则增加数量，没有，则新建一个购物车条目对象
            Cart c=null;
            //遍历购物车，看看是否有这件要添加的商品
            for (Cart cart : cartList) {
                if(itemId==cart.getItemId()){
                    c=cart;
                    break;
                }

            }

            if(c !=null){
                //表示购物车里面有这件商品
                c.setNum(c.getNum()+num);
                c.setUpdate(new Date());
            }else {
                //表示没有这件商品
                Item item = itemSercive.getItemById(itemId);
                Cart cart=new Cart();
                cart.setItemId(itemId);
                cart.setItemTitle(item.getTitle());
                cart.setItemPrice(item.getPrice());
                cart.setItemImage(item.getImages()[0]);
                cart.setNum(num);
                cart.setCreate(new Date());
                cart.setUpdate(new Date());

                cartList.add(cart);
            }
            //3.再把整理好的购物车放回到cookie里面

            String json = new Gson().toJson(cartList);
            //json.replaceAll(" ","");

        try {
            //对字符串进行URL的编码，里面的空格，、，百分号都会转译成 %XX
            json= URLEncoder.encode(json,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("现在购物车里的内容有：" + json);
        Cookie cookie = new Cookie("gqd_cart" , json);
        cookie.setMaxAge(60*60*24*7);
        /*
        默认情况。cookie的path路径就是当前这个请求的路径
        现在添加的购物车的时候，地址是http：//www.taotao.com/cart/cart.html
        cookied的地址是/cart/add
        一会要显示购物车的时候，访问的地址是http://www.taotao.com/cart/cart.html
        那么这时候cookie不会被传递过来。

        /  的意思是在http://www.taotao.com/的下面任意地址，都会携带这个cookie过来
         */
        cookie.setPath("/");

            response.addCookie(cookie);

        }

    @Override
    public List<Cart> findCookie(HttpServletRequest request) {

        return CookieUtil.findCartByCookie(request);
    }

    @Override
    public void updateCartByCookie(long itemId, int num, HttpServletRequest request,HttpServletResponse response) {
        //1、查询购物车
        List<Cart> cartList = findCookie(request);

        //2、遍历购物车找到对应的商品，然后修改数量
        for (Cart cart : cartList) {
            if(itemId==cart.getItemId()){
                //找到了这件商品
                cart.setNum(num);
                cart.setUpdate(new Date());
                break;
            }
        }

        //3.再返回cookie给浏览器
        String json = new Gson().toJson(cartList);

        try {
            //对字符串进行URL的编码，里面的空格，、，百分号都会转译成 %XX
            json= URLEncoder.encode(json,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Cookie cookie = new Cookie("gqd_cart", json);
        cookie.setMaxAge(60*60*24*7);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    @Override
    public void deleteByCookie(long itemId,HttpServletRequest request,HttpServletResponse response) {
        //1、查询购物车
        List<Cart> cartList = findCookie(request);

        //2、遍历购物车找到对应的商品，然后修改数量
        for (Cart cart : cartList) {
            if(itemId==cart.getItemId()){
                //找到了这件商品
               cartList.remove(cart);
                break;
            }
        }

        //3.再返回cookie给浏览器
        String json = new Gson().toJson(cartList);

        try {
            //对字符串进行URL的编码，里面的空格，、，百分号都会转译成 %XX
            json= URLEncoder.encode(json,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Cookie cookie = new Cookie("gqd_cart", json);
        cookie.setMaxAge(60*60*24*7);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

}
