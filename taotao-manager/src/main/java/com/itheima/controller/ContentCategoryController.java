package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.ContentCategory;
import com.itheima.service.ContentCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by 85074 on 2018/10/25.
 */
@Controller
public class ContentCategoryController {

    @Reference
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/rest/content/category")
    @ResponseBody
    public  List<ContentCategory>  getCategoryByParentId(@RequestParam(defaultValue = "0") Long id){

        List<ContentCategory> list = contentCategoryService.getCategoryByParentId(id);

        return list;

    }

    @RequestMapping("rest/content/category/add")
    @ResponseBody
    public ContentCategory add(ContentCategory contentCategory){

        contentCategory= contentCategoryService.add(contentCategory);
        return contentCategory;
    }

    @RequestMapping("rest/content/category/update")
    @ResponseBody
    public String  update(ContentCategory contentCategory){// id  name
        contentCategoryService.update(contentCategory);
        return "success";
    }

    @RequestMapping("rest/content/category/delete")
    @ResponseBody
    public String  delete(ContentCategory contentCategory){// id  name

        int result = contentCategoryService.delete(contentCategory);
        System.out.println("result：" + result);
        return "success";
    }

}
