package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Item;
import com.itheima.pojo.ItemDesc;
import com.itheima.service.ItemDescService;
import com.itheima.service.ItemSercive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 85074 on 2018/12/23.
 */
@Controller
public class ItemController {

    @Reference
    private ItemSercive itemSercive;

    @Reference
    private ItemDescService itemDescService;

    @RequestMapping("/item/{id}.html")
    //@PathVariable截取数据   {id} 取数据
    public String item(@PathVariable long id, Model model){

        //1、获取商品数据
        Item itemById = itemSercive.getItemById(id);

        //2、查询商品的数据
        ItemDesc itemDesc = itemDescService.findDescById(id);
        //2、存储商品的数据
        model.addAttribute("item",itemById);
        model.addAttribute("itemDesc",itemDesc);
        return "item";
    }
}
