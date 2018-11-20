package com.itheima.test;

import com.sun.javafx.fxml.builder.URLBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 85074 on 2018/11/8.
 */
public class TestHttpClient {

    @Test
    public void  testDemo() throws IOException {

        CloseableHttpClient httpClient= HttpClients.createDefault();
        //构建get请求
        HttpGet httpGet=new HttpGet("http://www.baidu.com");
        //发起请求
        HttpResponse response = httpClient.execute(httpGet);
        int code=response.getStatusLine().getStatusCode();

        if(200==code){
            //获取响应实体
            HttpEntity entity=response.getEntity();
            String content = EntityUtils.toString(entity, "UTF-8");
            System.out.println("content==" + content);
        }

        //关闭客户端
        httpClient.close();

    }


    @Test
    public void  testGet() throws IOException{

        CloseableHttpClient httpClient= HttpClients.createDefault();

        String url="http://manager.gqd.com/item/536563";
        HttpGet httpGet=new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        String content =EntityUtils.toString(entity,"UTF-8");
        System.out.println("content==" + content);
        httpClient.close();

    }

    //这个方法仅仅针对以前的那种API接口，如果是resful风格的接口，参数是直接在地址上面拼的。
    @Test
    public void  testGetParam() throws Exception{

        CloseableHttpClient httpClient= HttpClients.createDefault();

        //String url="http://manager.gqd.com/item/536563";
        //String url="http://manager.gqd.com/item";
        String url="http://manager.gqd.com/rest/content/category";

        //对地址进行再一次封装，以便携带参数
        URIBuilder uriBuilder=new URIBuilder(url);
        //添加参数
        uriBuilder.addParameter("id","0");

        URI uri = uriBuilder.build();
        System.out.println("uri===" + url.toString());

        HttpGet httpGet=new HttpGet(uri);

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        String content =EntityUtils.toString(entity,"UTF-8");
        System.out.println("content==" + content);
        httpClient.close();

    }


    @Test
    public void  testPost() throws IOException{

        CloseableHttpClient httpClient= HttpClients.createDefault();
       String url="http://manager.gqd.com/item";

        HttpPost httpPost=new HttpPost(url);

        //给post请求带参数
        List<NameValuePair> paramters=new ArrayList<>();

        //给Post请求携带参数
        paramters.add(new BasicNameValuePair("title","huawei"));
        paramters.add(new BasicNameValuePair("desc","超级贵"));
        paramters.add(new BasicNameValuePair("sellPoint","iphoneXXX好贵"));
        paramters.add(new BasicNameValuePair("price","19999"));
        paramters.add(new BasicNameValuePair("num","200"));
        paramters.add(new BasicNameValuePair("cid","76"));

        //对传递的参数进行url编码，使用utf-8
        HttpEntity entity=new UrlEncodedFormEntity(paramters,"utf-8");
       //给Post请求设置实体
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);

        if(response.getStatusLine().getStatusCode()==200){
            String content = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println("content==" + content);
        }
        httpClient.close();

    }

    @Test
    public void  testPut() throws IOException{

        CloseableHttpClient httpClient= HttpClients.createDefault();

        String url="http://manager.gqd.com/item";

        HttpPut httpPut=new HttpPut(url);

        //封装传递的数据
        List<NameValuePair> list=new ArrayList<>();

        list.add(new BasicNameValuePair("id","1433500495290"));
        list.add(new BasicNameValuePair("title","test"));

        //对传递的参数进行url编码，使用utf-8
        HttpEntity entity=new UrlEncodedFormEntity(list,"utf-8");
       //携带参数数据
        httpPut.setEntity(entity);

        HttpResponse response=httpClient.execute(httpPut);
        if(response.getStatusLine().getStatusCode()==200){
            String content = EntityUtils.toString(response.getEntity(), "UTf-8");
            System.out.println("content==" + content);
        }

        httpClient.close();
    }

    @Test
    public void  testDelete() throws IOException{

        CloseableHttpClient httpClient= HttpClients.createDefault();

        String url="http://manager.gqd.com/item/1540289437373";

        HttpDelete httpDelete=new HttpDelete(url);

        HttpResponse response=httpClient.execute(httpDelete);
        if(response.getStatusLine().getStatusCode()==200){
            String content = EntityUtils.toString(response.getEntity(), "UTf-8");
            System.out.println("content==" + content);
        }

        httpClient.close();
    }

}