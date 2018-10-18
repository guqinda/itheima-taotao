package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Item;
import com.itheima.service.ItemSercive;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 85074 on 2018/10/19.
 */

@Controller
public class ItemController {

    @Reference
    private ItemSercive itemSercive;

    //添加商品的时候，大部分的内容都会装载到item对象里面去，然后item对象要添加到item表里面去
    //商品的描述，使用desc来接收，然后添加到item_desc这张表里去
    @RequestMapping("/rest/item")
    @ResponseBody
    public String addItem(Item item,String desc){
     int result = itemSercive.addItem(item,desc);
        System.out.println("result==" + result);
        return  "success";
    }
}
