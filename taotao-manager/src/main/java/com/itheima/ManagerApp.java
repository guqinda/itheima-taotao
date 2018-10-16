package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by 85074 on 2018/10/13.
 */
//告诉SpringBoot  不要检测数据源  exclude:不要包含数据源的位置
    @EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)

@SpringBootApplication
public class ManagerApp {
    public  static void main(String [] args){
        SpringApplication.run(ManagerApp.class,args);
    }
}
