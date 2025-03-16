package com.roy.shardingDemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * @author ：楼兰
 * @description:
 **/
@SpringBootApplication
@MapperScan("com.roy.shardingDemo.mapper")
public class ShardingJDBCApplication{

    public static void main(String[] args) {
        SpringApplication.run(ShardingJDBCApplication.class,args);
    }

}
