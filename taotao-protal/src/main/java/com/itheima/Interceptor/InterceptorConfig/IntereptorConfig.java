package com.itheima.Interceptor.InterceptorConfig;

import com.itheima.Interceptor.OrderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by 85074 on 2019/1/1.
 */
@Component
public class IntereptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private OrderInterceptor orderInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(orderInterceptor).addPathPatterns("/order/**");
    }
}
