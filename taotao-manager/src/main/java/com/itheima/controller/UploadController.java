package com.itheima.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.util.UploadUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 85074 on 2018/10/23.
 */
@Controller
public class UploadController {

    /***
     * 用于处理JSON转换问题
     */
    @Autowired
    private ObjectMapper objectMapper;

    @ResponseBody
    @RequestMapping(value="/rest/pic/upload",method= RequestMethod.POST)
    public String upload(@RequestParam(value="uploadFile")MultipartFile file, HttpSession session) throws Exception{


        //获取文件后缀
        String subfix = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");

        //String TrackerServer="tracker_server=192.168.227.131:22122";
        //获取resources地址
        String path=System.getProperty("user.dir")+"/src/main/resources/";
        //执行文件上传  参数一：写的是配置文件地址路径，配置文件的内容是：tracker_server=192.168.227.131:22122
        String[] uploadinfos = UploadUtil.upload(path+"tracker.conf", file.getBytes(), subfix);

        for (String string : uploadinfos) {
            System.out.println(string);
        }


        /****
         * error   	0标识成功，1失败
         * url		成功后文件访问地址
         * height	高度
         * width	宽度
         */
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("error", 0);          //group1
        //map.put("url", "http://192.168.227.131/"+uploadinfos[0]+"/"+uploadinfos[1]);
        //防止别人知道ip地址在哪
        map.put("url", "http://image.gqd.com/"+uploadinfos[0]+"/"+uploadinfos[1]);
        map.put("height", 100);
        map.put("width", 100);

        //将Map对象转成JSON字符串
        String json = objectMapper.writeValueAsString(map);

        return json;
    }




}