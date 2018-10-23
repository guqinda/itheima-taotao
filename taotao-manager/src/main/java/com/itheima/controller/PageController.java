package com.itheima.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 85074 on 2018/10/17.
 */

@Controller
public class PageController {

    /*
    @RequestMapping("index")
    public String index(){

        System.out.println("要显示首页了--");

        /*
        如果使用了SpringMVC，需要指定资源路径所在
        如果使用了SpringBoot，按摩默认跳转的位置是在
        resource/static |public |xx | xx |templates
        现在的资源是在/web-inf/view/

        spring.mvc.view.prefix=/WEB-INF/view/
        spring.mvc.view.suffix=.jsp

        最终的跳转位置就是：/WEB-INF/view/index.jsp

        return "index";
    }
*/
    /*
    /rest/page/{pageName}  :{pageName} 用于截取后面的字符
    @PathVariable  String pageName ：把上面截取到的字符，赋值给参数pageName
    有一个需要注意的地方是：{}里面的字符 和参数名称必须一致
     */
    //@RequestMapping("/rest/page/item-add")
    @RequestMapping("/rest/page/{pageName}")
    public String page(@PathVariable String pageName){

        System.out.println("pageName="+pageName);

        return pageName;
    }

    @RequestMapping("/")
    public String index(){

        return "index";
    }

}
