package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Content;

import java.util.List;

/**
 * Created by 85074 on 2018/10/28.
 */

public interface ContentService {

    //前面四个方法都是后台的内容查询
    int add(Content content);

   PageInfo<Content> list(int categoryId, int page, int rows);

   int  edit(Content content);

   int delete(String ids);

   //商城首页的大广告位查询
    List<Content>  selectByCategoryId(long cid);

}
