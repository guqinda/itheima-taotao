package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Item;
import com.itheima.service.ItemSercive;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 85074 on 2018/10/19.
 */
/*  restful api风格
    @DeleteMapping：删除
    @PutMapping：更新
    @PostMapping：添加
    @GetMapping：查询
 */


@Controller
public class ItemController {

    @Reference
    private ItemSercive itemSercive;

    //添加商品的时候，大部分的内容都会装载到item对象里面去，然后item对象要添加到item表里面去
    //商品的描述，使用desc来接收，然后添加到item_desc这张表里去
    @RequestMapping(value = "/rest/item",method = RequestMethod.POST)
    @ResponseBody
    public String addItem(Item item,String desc){

     int result = itemSercive.addItem(item,desc);
        System.out.println("result==" + result);
        return  "success";
    }

    @RequestMapping(value = "/rest/item",method = RequestMethod.GET)
    @ResponseBody
    public   Map<String ,Object> list(int page,int rows){

        PageInfo<Item> pageInfo = itemSercive.list(page, rows);

        //easyui的数据表格显示数据，要求有固定格式，json里面total属性和rows属性
        //total属性表示总共有多少条记录
        //rows属性表示当前这一页的集合数据，也就是list集合

        Map<String ,Object> map=new HashMap<>();
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());

        return map;
    }

}
