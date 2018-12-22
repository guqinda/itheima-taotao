package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.mapper.ItemDescMapper;
import com.itheima.mapper.ItemMapper;
import com.itheima.pojo.Item;
import com.itheima.pojo.ItemDesc;
import com.itheima.service.ItemSercive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.util.Date;
import java.util.List;

/**
 * Created by 85074 on 2018/10/24.
 */
@Service
public class ItemServiceImpl implements ItemSercive {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescMapper itemDescMapper;

    @Autowired
    private JmsMessagingTemplate template;

    @Override
    public int addItem(Item item, String desc) {
        //添加Item表
        //itemMapper.insert(item);//添加数据
        //itemMapper.insertSelective(item);//添加数据Selective：有选择性

        //从页面传过来的item还不完整
        //ID是自己控制的，不是自增的，用当前时间+随机数生成
        Long id = System.currentTimeMillis()+(long)(Math.random()*100000);

        item.setStatus(1);
        item.setId(id);
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

        //添加完商品，需要发送出来消息，然后让搜索系统去更新索引库  MQ
        template.convertAndSend("item-save",id);

        return result;
    }

    //查询单个商品的信息
    @Override
    public Item getItemById(long id) {
        return itemMapper.selectByPrimaryKey(id);
    }

    @Override
    public int deleteItem(long id) {
        return itemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateItem(Item item) {

        return itemMapper.updateByPrimaryKeySelective(item);
    }

    @Override
    public PageInfo<Item> list(int page, int rows) {

        //必须要设置这一行，才能执行分页查询
        PageHelper.startPage(page,rows);

        List<Item> list=itemMapper.select(null);

        return new PageInfo<Item>(list);
    }



}
