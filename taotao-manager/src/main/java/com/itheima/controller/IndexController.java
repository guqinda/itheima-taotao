package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Item;
import com.itheima.service.ItemSercive;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 85074 on 2018/12/19.
 */
@RestController
public class IndexController {

    @Autowired
    private SolrClient solrClient;

    @Reference
    private ItemSercive itemSercive;

    @RequestMapping("initIndex")
    public String initIndex() throws Exception {
        int page = 1, rows = 500;
        do {
            List docList = new ArrayList();

            PageInfo<Item> pageInfo = itemSercive.list(page, rows);
            List<Item> list = pageInfo.getList();
            for (Item item : list) {
                //遍历一件商品，就得到一个solr的文档对象
                //一个文档对象就是一条索引数据
                SolrInputDocument doc = new SolrInputDocument();
                doc.addField("id", item.getId());
                doc.addField("item_title", item.getTitle());
                doc.addField("item_price", item.getPrice());
                doc.addField("item_image", item.getImage());
                doc.addField("item_cid", item.getCid());
                doc.addField("item_status", item.getStatus());

                docList.add(doc);

            }
            solrClient.add(docList);
            solrClient.commit();
            page++;
            rows=list.size();
        }while (rows==500);
        return "success" ;
    }
}
