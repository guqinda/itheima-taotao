package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.itheima.mapper.ContentMapper;
import com.itheima.pojo.Content;
import com.itheima.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

import javax.swing.text.AbstractDocument;
import java.util.*;

/**
 * Created by 85074 on 2018/10/28.
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private RedisTemplate<String ,String > template;

    @Override
    public int add(Content content) {
        Date date = new Date();
        content.setCreated(date);
        content.setUpdated(date);
       int result=contentMapper.insert(content);

        //添加操作后，清空缓存
        ValueOperations<String, String> opsForValue = template.opsForValue();
        opsForValue.set("bingAd","");
        return result;
    }

    @Override
    public PageInfo<Content> list(int categoryId, int page, int rows) {

        //设置分页
        PageHelper.startPage(page,rows);

        //查询
        Content content=new Content();
        content.setCategoryId((long) categoryId);
        List<Content> list=contentMapper.select(content);

        return new PageInfo<>(list);
    }

    @Override
    public int edit(Content content) {

        content.setUpdated(new Date());
        int result= contentMapper.updateByPrimaryKeySelective(content);
        //更新操作后，清空缓存
        ValueOperations<String, String> opsForValue = template.opsForValue();
        opsForValue.set("bingAd","");

        return result;
    }

    @Override
    public int delete(String ids) {

        //传多个id时用逗号分开
        String[] idArray = ids.split(",");
        int result=0;
        //遍历分隔符id数组
        for (String id:idArray){
             result+=  contentMapper.deleteByPrimaryKey(Long.parseLong(id));
        }

        //删除mysql完毕后，也要去删除redis的数据
        ValueOperations<String, String> opsForValue = template.opsForValue();
        opsForValue.set("bingAd","");
        return result;
    }

    //查看大广告位
/*
1先从redis里面拿
2.有就直接返回，没有，就去数据库查
3查询完毕，需要把查询到的数据缓存到redis里面，并且返回这份数据给页面显示
 */

    @Override
    public String selectByCategoryId(long cid) {

        System.out.println("======");
        ValueOperations<String ,String > operations=template.opsForValue();
        String json=operations.get("bingAd");
        System.out.println("从缓存里面获取广告数据"+json);
        if(!StringUtils.isEmpty(json)){
           //如果不是空，表示有缓存
            System.out.println("缓存里面有广告的数据，直接返回");
            return json;
        }
        System.out.println("缓存里面没有广告的数据，要去查询数据库");


        //代码执行到这里，表示缓存里面没有，要去查数据库了
        Content c=new Content();
        //给当前对象赋值分类id
        c.setCategoryId(cid);
        //从mysql数据库里面查询出来广告的数据
        List<Content> contents = contentMapper.select(c);
        //自己组拼页面要求的字段信息
        List<Map<String,Object>> list=new ArrayList<>();
        //把从数据库查询出来的集合，遍历，一个content就对应一个map集合
        for(Content content:contents){
            Map<String,Object> map=new HashMap<>();
            map.put("src",content.getPic());
            map.put("width",670);
            map.put("heigh",240);
            map.put("href",content.getUrl());
            //每遍历一个map数组就添加进去
            list.add(map);
        }

        //把这个list集合变成json字符串然后存进去
        json =new Gson().toJson(list);
        //存到redis中去
        operations.set("bingAd",json);
        System.out.println("从mysql查询出来的数据要存到redis数据库去");
        return    json;

    }


//    @Override
//    public List<Content> selectByCategoryId(long cid) {
//
//        Content content=new Content();
//        //给当前对象赋值分类id
//        content.setCategoryId(cid);
//
//        return    contentMapper.select(content);
//
//    }
}
