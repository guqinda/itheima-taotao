package com.itheima.service;

import com.itheima.pojo.Cart;

import java.util.List;

/**
 * Created by 85074 on 2018/12/26.
 */
public interface CartService {

    /**
     * 添加商品到购物车
     * @param userId 用户id
     * @param itemId 商品id
     * @param num 购买数量
     */
    void addToCart(long userId,long itemId,int num);


    /**
     * 根据用户id查询对应的购物车商品，一个用户的购物车可以有多件商品，所以返回值是list
     * @param userId
     * @return
     */
    List<Cart> queryCartByUserId(long userId);

    void updateNumByCart(long userId,long itemId ,int num);

    //删除购物车商品，带上用户和商品id
    void deleteItemByCart(long userId,long itemId);


}
