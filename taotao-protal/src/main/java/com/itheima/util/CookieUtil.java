package com.itheima.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.pojo.Cart;
import org.springframework.beans.propertyeditors.URLEditor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by 85074 on 2018/12/26.
 */
public class CookieUtil {

   public static String  findTicket(HttpServletRequest request){
       Cookie[] cookies = request.getCookies();
       if(cookies!=null){
           for (Cookie cookie : cookies) {
               String name = cookie.getName();
               if("ticket".equals(name)){
                 return   cookie.getValue();
               }
           }
       }

       return null;
   }

    /**
     * 返回值有可能为空
     * @param request
     * @return
     */
    public static List<Cart> findCartByCookie(HttpServletRequest request){
        List<Cart> list=null;

        Cookie[] cookies=request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                String name = cookie.getName();

                System.out.println("要从cookie里面获取购物车了，name==" + name);

                if("gqd_cart".equals(name)){
                    String json = cookie.getValue();

                    //解码
                    try {
                       json= URLDecoder.decode(json,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    list = new Gson().fromJson(json,new TypeToken<List<Cart>>(){}.getType());
                    break;
                }
            }
        }
        return list;
    }

}
