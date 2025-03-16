package com.tulingxueyuan.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/add")
    public String add(){
        System.out.println("下单成功!");
        String msg = restTemplate.getForObject("http://localhost:8011/stock/reduct", String.class);
        return msg;
    }


    @RequestMapping("/add2")
    public String add2(){
        HttpURLConnection("http://localhost:8011/stock/reduct");
        //String msg = new RestTemplate().getForObject("http://localhost:8011/stock/reduct", String.class);
        //return msg;
        return "";
    }


    public  String HttpURLConnection(String url) {

        StringBuffer sb = new StringBuffer();
        try {

            URL realUrl = new URL(url);
            //将realUrl以open方法返回的urlConnection 连接强转为HttpURLConnection连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();// 此时cnnection只是为一个连接对象,待连接中
            //设置连接输出流为true,默认false
            connection.setDoOutput(true);
            //设置连接输入流为true
            connection.setDoInput(true);
            //设置请求方式为post
            connection.setRequestMethod("POST");
            //post请求缓存设为false
            connection.setUseCaches(false);
            //设置该HttpURLConnection实例是否自动执行重定向
            connection.setInstanceFollowRedirects(true);
            //设置请求头里面的各个属性
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //建立连接
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String lines;
            while ((lines = reader.readLine()) != null) {

                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            reader.close();
            connection.disconnect();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return sb.toString();
    }
}
