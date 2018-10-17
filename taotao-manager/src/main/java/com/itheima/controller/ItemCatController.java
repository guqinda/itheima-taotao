package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.ItemCat;
import com.itheima.service.ItemCatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by 85074 on 2018/10/17.
 */

@Controller
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    //默认请求的时候，不带id过来，所以给定一个默认值0.表示获取所有的一级分类数据
    @RequestMapping("/rest/item/cat")
    @ResponseBody//接收json数据
    public List<ItemCat> selectItemCat(@RequestParam(defaultValue = "0") long id){
         //查询到所有分类后，直接抛出去就好了
        List<ItemCat> list=itemCatService.selectItemCatByParentId(id);

        System.out.println("list==" + list);
        return list;
    }
}
