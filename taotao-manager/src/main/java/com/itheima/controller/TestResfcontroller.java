package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Item;
import com.itheima.service.ItemSercive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 85074 on 2018/11/8.
 * 测试resf风格的例子
 */
@Controller
public class TestResfcontroller {

    @Reference
    private ItemSercive itemSercive;

    //@PathVariable 的意思就是当上面的{id} 截取到地址路径的id值之后，赋值给参数id，{}里面的文字必须和参数名一样
    //@RequestMapping(value = "/item/{id}",method = RequestMethod.GET)
    @GetMapping("/item/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable long id){

        try {
            Item item=itemSercive.getItemById(id);

            //跟下面一样ResponseEntity.ok(item);
            return ResponseEntity.status(HttpStatus.OK).body(item);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            //返回内部错误
        }

    }

    @PostMapping("/item")
    public ResponseEntity<Void> addItem(Item item,String desc) {

        try {
            int result = itemSercive.addItem(item, desc);
            System.out.println("新增结果" + result);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping ("/item/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable long id) {

        try {
            int result = itemSercive.deleteItem(id);
            System.out.println("删除结果" + result);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/item")
    public ResponseEntity<Void> updateItem(Item item) {

        try {
            int result = itemSercive.updateItem(item);
            System.out.println("更新结果" + result);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
