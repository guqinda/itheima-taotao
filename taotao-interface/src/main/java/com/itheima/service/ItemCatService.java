package com.itheima.service;

import com.itheima.pojo.ItemCat;

import java.util.List;

/**
 * Created by 85074 on 2018/10/17.
 */
public interface ItemCatService {

   List<ItemCat> selectItemCatByParentId(long parentId);
}
