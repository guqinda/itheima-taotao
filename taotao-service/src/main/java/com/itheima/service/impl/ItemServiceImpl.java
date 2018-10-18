package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.ItemDescMapper;
import com.itheima.mapper.ItemMapper;
import com.itheima.pojo.Item;
import com.itheima.pojo.ItemDesc;
import com.itheima.service.ItemSercive;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by 85074 on 2018/10/19.
 */
@Service
public class ItemServiceImpl implements ItemSercive {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemDescMapper itemDescMapper;

    @Override
    public int addItem(Item item, String desc) {
       //添加Item表
       //itemMapper.insert(item);//添加数据
        //itemMapper.insertSelective(item);//添加数据Selective：有选择性

       //从页面传过来的item还不完整
        //ID是自己控制的，不是自增的，用当前时间+随机数生成
        long id = System.currentTimeMillis()+(long)(Math.random()*100000);

        item.setStatus(1);
        item.setCreated(new Date());
        item.setUpdated(new Date());

       int result= itemMapper.insertSelective(item);

       //添加desc表
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(id);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDescMapper.insertSelective(itemDesc);

        return result;
    }
}
