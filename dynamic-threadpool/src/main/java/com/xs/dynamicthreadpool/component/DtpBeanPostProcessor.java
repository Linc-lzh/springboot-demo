package com.xs.dynamicthreadpool.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class DtpBeanPostProcessor implements BeanPostProcessor {
    private DefaultListableBeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ThreadPoolExecutor) {
            //直接纳入管理
            DtpRegistry.registry(beanName, (ThreadPoolExecutor) bean);
        }
        return bean;
    }
}