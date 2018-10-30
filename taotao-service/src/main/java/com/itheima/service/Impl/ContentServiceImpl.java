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

    @Override
    public int delete(String ids) {

        //传多个id时用逗号分开
        String[] idArray = ids.split(",");
        int result=0;
        //遍历分隔符id数组
        for (String id:idArray){
             result+=  contentMapper.deleteByPrimaryKey(Long.parseLong(id));
        }
        return result;
    }

    //查看大广告位
    @Override
    public List<Content> selectByCategoryId(long cid) {

        Content content=new Content();
        //给当前对象赋值分类id
        content.setCategoryId(cid);

        return    contentMapper.select(content);

    }
}
