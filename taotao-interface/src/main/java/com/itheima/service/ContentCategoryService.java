package com.itheima.service;

import com.itheima.pojo.ContentCategory;

import java.util.List;

/**
 * Created by 85074 on 2018/10/25.
 */
public interface ContentCategoryService {
    List<ContentCategory> getCategoryByParentId(Long id);

    ContentCategory  add(ContentCategory contentCategory);

    int update(ContentCategory contentCategory);

    int delete(ContentCategory contentCategory);
}
