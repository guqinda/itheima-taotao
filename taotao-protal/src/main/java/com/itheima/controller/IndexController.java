package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.itheima.pojo.Content;
import com.itheima.service.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 85074 on 2018/10/25.
 */
@Controller
public class IndexController {

   @Reference
    private ContentService contentService;

   @RequestMapping("/page/{pageName}")
    public  String  page(@PathVariable String pageName){

       return pageName;
    }

    @RequestMapping("/")
    public  String  index(Model model){

        //要把大广告位的6张图片给查询出来,按分类id查，所以把大广告id赋值进来
        int categoryId=89;
        //这里查询回来的是集合，里面装的是content对象，但是页面显示6张图片，要求的数据格式不是这样的
        //List<Content> contents=contentService.selectByCategoryId(categoryId);
        String json=contentService.selectByCategoryId(categoryId);

        System.out.println("json==" + json);
       /*
               List<Map<String,Object>> list=new ArrayList<>();
        //把从数据库查询出来的集合，遍历，一个content就对应一个map集合
        for(Content content:contents){
            Map<String,Object> map=new HashMap<>();
            map.put("src",content.getPic());
            map.put("width",670);
            map.put("heigh",240);
            map.put("href",content.getUrl());
            //没遍历一个map数组就添加进去
            list.add(map);
        }
        */


        //把list---Gson  |  Fastjson ----> json字符串转化
        //String json=new Gson().toJson(list);

        model.addAttribute("list",json);

        return "index";
    }
}
