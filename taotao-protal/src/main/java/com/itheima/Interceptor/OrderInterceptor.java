package com.itheima.Interceptor;

import com.itheima.pojo.User;
import com.itheima.util.CookieUtil;
import com.itheima.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 85074 on 2019/1/1.
 */

@Component
public class OrderInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String ,String > redisTemplate;

    /*
    用户登录了之后，会把用户的信息存储在redis里面，并且把凭证(redis的key)
    存储到cookie里面去，返回给浏览器

        1. 从cookie里面获取ticket。----> 如果有登录，肯定有ticket。 70天

        2. 在redis里面才是真正存储用户信息的位置，只有确保redis里面有这个ticket对应的value值。
        才能表示用户已经登录了。

            redis存值的时候，可以设置有一个有效期时间，当这个有效期时间到了之后，redis会把这个数据删除掉

            redis.set("key","value"); ----->  redis.set("key","value" , 10000); :: 10s之后删除掉数据

     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 从cookie里面取出ticket
        String ticket = CookieUtil.findTicket(request);
        //如果从cookie里面取出ticket是空，表示没有登录
        if(StringUtils.isEmpty(ticket)){
            System.out.println("要去下单了，但现在没有登录，被拦截了");

            //获取当前的请求地址
           // String uri = request.getRequestURI();
            String uri = "/cart/cart.html";

            System.out.println("uri===" + uri);//uri===/order/order-cart.shtml
           // String url = request.getRequestURL().toString();
            //System.out.println("urL===" + url);//urL===http://127.0.0.1:8080/order/order-cart.shtml



            //要去登录
            response.sendRedirect("/page/login.shtml?redirect="+uri);//前面有那个 / 代表是从根网址下开始拼www.taotao.com  不跟 / 这个的话是直接接着当前的路径拼
            return false;
        }

        //2、在判断一层，才能真正的确保到底有没有登录
        User user = RedisUtil.findUserByTicket(redisTemplate, ticket);
        if(user==null){
            //要去登录
            //前面有那个 / 代表是从根网址www.taotao.com下开始拼后面接上的路径/page/login.shtml  不跟 / 这个的话是直接接着当前的路径拼
            response.sendRedirect("/page/login.shtml");
            return false;
        }

        //拦截器查询到对象后，直接存起来，以便controller直接使用，而不用去查询了
        request.setAttribute("user",user);

        //如果不是空，表示已经登录了
        System.out.println("登录成功不拦截");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
