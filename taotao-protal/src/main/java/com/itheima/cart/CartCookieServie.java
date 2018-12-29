package com.itheima.cart;

import com.itheima.pojo.Cart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by 85074 on 2018/12/27.
 */
public interface CartCookieServie {

    //未登录的状态添加购物车
    void addToCart(long itemId, int num, HttpServletRequest request, HttpServletResponse response);

    //从cookie里面查询购物车
    List<Cart> findCookie(HttpServletRequest request);

    void updateCartByCookie(long itemId ,int num ,HttpServletRequest request,HttpServletResponse response);

    void deleteByCookie(long itemId,HttpServletRequest request,HttpServletResponse response);

}
