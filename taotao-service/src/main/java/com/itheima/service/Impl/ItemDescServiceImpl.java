package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.ItemDescMapper;
import com.itheima.pojo.ItemDesc;
import com.itheima.service.ItemDescService;
import com.itheima.service.ItemSercive;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 85074 on 2018/12/23.
 */
@Service
public class ItemDescServiceImpl implements ItemDescService {

    @Autowired
    private ItemDescMapper itemDescMapper;

    @Override
    public ItemDesc findDescById(long id) {
        return itemDescMapper.selectByPrimaryKey(id);

    }
}
