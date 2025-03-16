package com.tulingxueyuan.order;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HttpClientReuseExample {


    public static HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);

        //设置整个连接池最大连接数
        connectionManager.setMaxTotal(10000);

        //MaxPerRoute路由是对maxTotal的细分,每个主机的并发，这里route指的是域名
        connectionManager.setDefaultMaxPerRoute(10000);
        connectionManager.closeIdleConnections(10, TimeUnit.SECONDS);
        RequestConfig requestConfig = RequestConfig.custom()
                //返回数据的超时时间
                .setSocketTimeout(20000)
                //连接上服务器的超时时间
                .setConnectTimeout(10000)
                //从连接池中获取连接的超时时间
                .setConnectionRequestTimeout(10000)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                //.setKeepAliveStrategy((response, context) -> 0)  //设置 Keep-Alive 时间为
                .build();
    }

    public static void main(String[] args) throws IOException {

        HttpClient httpClient = httpClient();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(10000);
        factory.setConnectTimeout(10000);
        factory.setHttpClient(httpClient);


        RestTemplate restTemplate = new RestTemplate(factory);

        long begin = System.currentTimeMillis();

        List<CompletableFuture> list=new ArrayList<>();
        // 发起多次请求
        for (int i = 0; i < 100000; i++) {
                // java 远程访问网络
            list.add( CompletableFuture.runAsync(() -> {

                HttpGet httpget = new HttpGet("http://localhost:8011/stock/reduct");
                try {
                    httpClient.execute(httpget,HttpClientContext.create());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //restTemplate.getForObject("http://localhost:8011/stock/reduct", String.class);
            }));
        }
        CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()])).join();
        long end = System.currentTimeMillis();
        System.out.println(end-begin);


    }
}
