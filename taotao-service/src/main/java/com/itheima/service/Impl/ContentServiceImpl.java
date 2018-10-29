package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.mapper.ContentMapper;
import com.itheima.pojo.Content;
import com.itheima.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.text.AbstractDocument;
import java.util.Date;
import java.util.List;

/**
 * Created by 85074 on 2018/10/28.
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentMapper contentMapper;

    @Override
    public int add(Content content) {
        Date date = new Date();
        content.setCreated(date);
        content.setUpdated(date);
        return contentMapper.insert(content);
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
        return contentMapper.updateByPrimaryKeySelective(content);
    }
}