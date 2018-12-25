package com.itheima.freemarker;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Item;
import com.itheima.pojo.ItemDesc;
import com.itheima.service.ItemDescService;
import com.itheima.service.ItemSercive;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 85074 on 2018/12/24.
 */
@Component //功能和service差不多，就是把这个类交给spring来托管，后续会调用下面的方法
public class FreeMarkerService {

    @Reference
    private ItemSercive itemSercive;

    @Reference
    private ItemDescService itemDescService;

    //只要后台新增了商品，那么就会执行这个方法，用于创建商品详情页的静态页面
    @JmsListener(destination = "item-save")
    public void addItem(String message) throws Exception {
        System.out.println("商品详情页收到的消息是：" + message);

        Item item = itemSercive.getItemById(Long.parseLong(message));
        ItemDesc itemDesc = itemDescService.findDescById(Long.parseLong(message));
        //创建配置对象
        Configuration configuration=new Configuration(Configuration.VERSION_2_3_27);

        String path=System.getProperty("user.dir")+"/src/main/webapp/ftl";
        //设置加载模板的文件路径
        configuration.setDirectoryForTemplateLoading(new File(path));

        //获取模板
        Template template = configuration.getTemplate("item.ftl");

        //准备数据
        Map<String,Object> root=new HashMap<>();

        root.put("item",item);
        root.put("itemDesc",itemDesc);
       //静态页面输出的位置
        Writer out=new FileWriter("D:/taotao/item/"+message+".html");

        //输出html页面
        template.process(root,out);

        out.close();
    }


}
