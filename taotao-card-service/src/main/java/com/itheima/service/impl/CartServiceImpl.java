package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.pojo.Cart;
import com.itheima.pojo.Item;
import com.itheima.service.CartService;
import com.itheima.service.ItemSercive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 85074 on 2018/12/26.
 */
@Service
public class CartServiceImpl implements CartService{

    @Reference
    private ItemSercive itemSercive;

    @Autowired
    private RedisTemplate<String ,String> template;

    @Override
    public void addToCart(long userId, long itemId, int num) {

        //1、根据itemid查询要添加的商品数据
        Item item = itemSercive.getItemById(itemId);

        //2、根据userId 去查询redis数据库，遍历购物车，判断是否有重复数据，进而修改数量
        //如果没有，就算是全新添加的商品
        List<Cart> cartList = queryCartByUserId(userId);

        //3、遍历购物车 这个for循环目的就算为了找出来有没有重复的商品
        Cart c=null;
        for (Cart cart:cartList) {
           if(itemId== cart.getItemId()) {
               //有一样的商品就先标记下来，然后就跳出
                c=cart;
                break;
           }
        }

        if(c!=null){
            //有重复的商品,泽数量叠加
            c.setNum(c.getNum()+num);
        }else {
            //没有重复的商品
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

        //4、再把购物车添加到redis中去
        String cardjson = new Gson().toJson(cartList);
        template.opsForValue().set("gqd_"+userId,cardjson);
    }



    @Override
    public List<Cart> queryCartByUserId(long userId) {

        //根据用户id，从redis中获取该用户的购物车数据，回来的还是json字符串
        String json = template.opsForValue().get("gqd_"+userId);

        //拿到的购物车数据是json  要转换成list集合把它们遍历出来
        List<Cart> list= new Gson().fromJson(json,new TypeToken<List<Cart>>(){}.getType());

        //第一次来的时候，redis里面购物车是没有的，所以这个list集合就是null
       if(list==null){
           list=new ArrayList<>();
       }
        return list;
    }

    @Override
    public void updateNumByCart(long userId, long itemId, int num) {
        //1、根据用户id先查出来这个用户的购物车数据
        String json = template.opsForValue().get("gqd_"+userId);

        List<Cart> list= new Gson().fromJson(json,new TypeToken<List<Cart>>(){}.getType());

        for (Cart cart:list) {
            if(itemId==cart.getItemId()){
                cart.setNum(num);//设置购物车商品数量
                cart.setUpdate(new Date());//设置购物车更新时间
                break;
            }
        }
        json=new Gson().toJson(list);

        template.opsForValue().set("gqd_"+userId,json);
        //2、遍历购物车的数据，找到需要更新数量的商品，然后修改数量

        //3.再把数据回填到redis中去
    }

    @Override
    public void deleteItemByCart(long userId, long itemId) {

        //1、先查询购物车的数据
        String json = template.opsForValue().get("gqd_" + userId);
        List<Cart> list= new Gson().fromJson(json,new TypeToken<List<Cart>>(){}.getType());

        //2、移除购物车数据
        for (Cart cart:list) {
            if(cart.getItemId()==itemId){
                list.remove(cart);
                break;
            }
        }

        //3.重新存到redis中
        json=new Gson().toJson(list);
        template.opsForValue().set("gqd_"+userId,json);

    }


}
