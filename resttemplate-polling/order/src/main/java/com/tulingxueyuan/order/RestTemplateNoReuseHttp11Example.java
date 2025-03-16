package com.tulingxueyuan.order;

import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestTemplateNoReuseHttp11Example {

    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());
        // 添加拦截器，设置 Connection: close 请求头
        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                //request.getHeaders().set("HTTP-Version", "HTTP/1.0");
                request.getHeaders().set("Connection", "close");
                return execution.execute(request, body);
            }
        });

        long begin = System.currentTimeMillis();
        List<CompletableFuture> list=new ArrayList<>();
        // 发起多次请求
        for (int i = 0; i < 100000; i++) {
            // java 远程访问网络
            list.add( CompletableFuture.runAsync(() -> {
                restTemplate.getForObject("http://localhost:8011/stock/reduct", String.class);
            }));
        }
        CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()])).join();
        long end = System.currentTimeMillis();
        System.out.println(end-begin);

    }









}
