package com.xs.dynamicthreadpool.utils;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.thread.RejectPolicy;
import com.xs.dynamicthreadpool.properties.ThreadPoolProperties;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class BeanUtil {
    public static void registerIfAbsent(BeanDefinitionRegistry registry, ThreadPoolProperties executorProp) {
        register(registry, executorProp.getPoolName(), executorProp);
    }

    public static void register(BeanDefinitionRegistry registry, String beanName, ThreadPoolProperties executorProp) {
        //Class<? extends Executor> executorType = ExecutorType.getClazz(executorProp.getPoolType());
        Object[] args = assembleArgs(executorProp);
        register(registry, beanName, ThreadPoolExecutor.class, args);
    }

    public static void register(BeanDefinitionRegistry registry, String beanName, Class<?> clazz, Object[] args) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object arg : args) {
            builder.addConstructorArgValue(arg);
        }
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    private static Object[] assembleArgs(ThreadPoolProperties executorProp) {
        return new Object[]{
                executorProp.getCorePoolSize(),
                executorProp.getMaximumPoolSize(),
                executorProp.getKeepAliveTime(),
                executorProp.getTimeUnit(),
                QueueType.getInstance(executorProp.getQueueType(), executorProp.getQueueSize()),
                new NamedThreadFactory(
                        executorProp.getPoolName() + executorProp.getThreadFactoryPrefix(),
                        executorProp.isDaemon()
                ),
                //先默认不做设置
                RejectPolicy.ABORT.getValue()
        };
    }
}
