package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Item;

/**
 * Created by 85074 on 2018/10/19.
 */
public interface ItemSercive {
    int addItem(Item item,String desc);

    //查询商品信息分页
    PageInfo<Item> list(int page , int rows);

    Item getItemById(long id);

    int deleteItem(long id);

    int updateItem(Item item);

}
