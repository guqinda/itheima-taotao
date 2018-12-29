package com.itheima.cart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 85074 on 2018/12/27.
 */
public interface CartMergeService {

    void mergeCart(String ticket,HttpServletRequest request, HttpServletResponse response);
}
