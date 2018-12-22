package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.ItemMapper;
import com.itheima.pojo.Page;
import com.itheima.service.SearchService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.itheima.pojo.Item;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

/**
 * Created by 85074 on 2018/12/19.
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrClient solrClient;

    @Autowired
    private ItemMapper itemMapper;

    @JmsListener(destination = "item-save")
    @Override
    public void addItem(String message) {

        try {
            System.out.println("搜索系统收到的消息是：" + message);
            //1、查询最新的添加的那件商品
            Item item = itemMapper.selectByPrimaryKey(Long.parseLong(message));

            //2、吧商品添加到索引库中
            SolrInputDocument doc=new SolrInputDocument();
            doc.addField("id",message);
            doc.addField("item_title",item.getTitle());
            doc.addField("item_image",item.getImage());
            doc.addField("item_cid",item.getCid());
            doc.addField("item_price",item.getPrice());
            doc.addField("item_status",item.getStatus());

            solrClient.add(doc);
            solrClient.commit();

            System.out.println("索引庫更新完畢"+message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Page<Item> search(String q,int page ,int pageSize) {


        try {
            //int page=1,rows=16;
            SolrQuery param = new SolrQuery();
            //设置高亮显示
            param.setHighlight(true);

            //设置高亮的域
            param.addHighlightField("item_title");

            //设置高亮的前缀和后缀
            param.setHighlightSimplePre("<font color='red'>");
            param.setHighlightSimplePost("</font>");

            //设置查询的参数
            param.setQuery("item_title:" + q);

            //设置分页
            param.setStart((page - 1) * pageSize);//跳过前面的多少条
            param.setRows(pageSize);//返回多少条
            QueryResponse response = solrClient.query(param);
            //获取到查询结果
            SolrDocumentList results = response.getResults();
            //当前这一页的集合数据
            List<Item> list = new ArrayList<>();

            //获取高亮的数据
            //外层map集合的key  就是商品的id值
            //外层map集合的value  就是高亮的数据

            //因为高亮的数据可以有很多个，所以又是一个map集合存储。比如：title 可以高亮
            //里层map集合的key 就是高亮的域名
            //里层map集合的value  就是高亮的域值，这里其实就是item_title
            Map<String, Map<String, List<String>>> map = response.getHighlighting();
            for (SolrDocument document : results) {
                Item item = new Item();

                long id = Long.parseLong((String) document.getFieldValue("id"));
                String item_title = (String) document.getFieldValue("item_title");

                item.setId(id);
                //从高亮那里去拿标题数据,这里的id放进map是String类型，所以就后面+“”
                Map<String, List<String>> map1 = map.get(id+"");
                List<String> highLight =map1.get("item_title");
                //表示这个商品有高亮的标题
                if(highLight!=null&&highLight.size()>0){
                    item_title= highLight.get(0);
                }

                //标题是要高亮的，高亮的不是整个标题而是搜索的关键字。
                item.setTitle(item_title);
                item.setPrice((Long) document.getFieldValue("item_price"));
                item.setImage((String) document.getFieldValue("item_image"));
                item.setCid((Long) document.getFieldValue("item_cid"));

                list.add(item);
            }

            //page对象需要封装什么数据
            Page<Item> pageItem = new Page<Item>(results.getNumFound(),page,pageSize);
            pageItem.setList(list);//设置这一页的集合数据
//            pageItem.setSize(pageSize);//设置每页的个数
//            pageItem.setCurrentpage(page);//设置当前页
//            pageItem.setTotal(results.getNumFound());//查到的总记录数

            return  pageItem;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
