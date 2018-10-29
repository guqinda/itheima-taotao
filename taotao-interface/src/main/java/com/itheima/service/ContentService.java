package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Content;

/**
 * Created by 85074 on 2018/10/28.
 */

public interface ContentService {

    int add(Content content);

   PageInfo<Content> list(int categoryId, int page, int rows);

   int  edit(Content content);
}
