package com.itheima.service;


import com.itheima.pojo.Item;
import com.itheima.pojo.Page;

import java.util.List;

/**
 * Created by 85074 on 2018/12/19.
 */
public interface SearchService {

   Page<Item> search(String q, int page, int pageSize);

   void addItem(String message );
}
