package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Item;
import com.itheima.pojo.Page;
import com.itheima.service.SearchService;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by 85074 on 2018/12/19.
 */
@Controller
public class SearchController {

    @Reference
    private SearchService searchService;

    @RequestMapping("search.shtml")
    public String search(String q, @RequestParam(defaultValue = "1") int page, Model model){

        int pageSize=16;
        //从索引库查询数据
        Page<Item> pageInfo = searchService.search(q, page, pageSize);
        //把数据保存到model里面去，以便到页面上显示
        model.addAttribute("page",pageInfo);
        model.addAttribute("query",q);
        model.addAttribute("totalPages",pageInfo.getLast());
        return "search";
    }
}
