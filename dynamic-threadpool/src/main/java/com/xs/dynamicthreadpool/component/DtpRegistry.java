package com.xs.dynamicthreadpool.component;

import com.xs.dynamicthreadpool.properties.ThreadPoolProperties;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class DtpRegistry {
    /**
     * 储存线程池
     */
    private static final Map<String, ThreadPoolExecutor> EXECUTOR_MAP = new ConcurrentHashMap<>();

    /**
     * 获取线程池
     * @param executorName 线程池名字
     */
    public static ThreadPoolExecutor getExecutor(String executorName) {
        return EXECUTOR_MAP.get(executorName);
    }


    public static Collection<String> getAllExecutorNames(){
        return EXECUTOR_MAP.keySet();
    }


    public static Collection<ThreadPoolExecutor> getAllDtpExecutor(){
        return EXECUTOR_MAP.values();
    }
    /**
     * 线程池注册
     * @param executorName 线程池名字
     */
    public static void registry(String executorName, ThreadPoolExecutor executor) {
        //注册
        EXECUTOR_MAP.put(executorName, executor);
    }


    /**
     * 刷新线程池参数
     * @param executorName 线程池名字
     * @param properties 线程池参数
     */
    public static void refresh(String executorName, ThreadPoolProperties properties) {
        ThreadPoolExecutor executor =  EXECUTOR_MAP.get(executorName);
        //刷新参数
        //.......

        //executor.setCorePoolSize(properties.xxx);
        //executor.setMaximumPoolSize(properties.xxx);

    }
}
