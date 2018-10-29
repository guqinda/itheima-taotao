package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Content;
import com.itheima.service.ContentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 85074 on 2018/10/28.
 */

@RestController
public class ContentController {

    @Reference
    private ContentService contentService;

   // @RequestMapping("/rest/content")
    @PostMapping("/rest/content")
    public String add(Content content){

        contentService.add(content);
        return "success";
    }

    @GetMapping("/rest/content")
    public  Map<String,Object> list(int categoryId,int page,int rows){

        PageInfo<Content> p = contentService.list(categoryId, page, rows);

        //easyui的格式参数为total，rows
        Map<String,Object> map=new HashMap<>();

        map.put("total",p.getTotal());
        map.put("rows",p.getList());

        return map;
    }

    @RequestMapping("/rest/content/edit")
    public Map<String ,Object> edit(Content content){

       int result= contentService.edit(content);

      //  System.out.println("result===" + result);

        Map<String ,Object> map=new HashMap<>();
        //表示更新成功
        if(result>0){
            map.put("status",200);
        }else {
            map.put("status",500);
        }

        return map;
    }

}
