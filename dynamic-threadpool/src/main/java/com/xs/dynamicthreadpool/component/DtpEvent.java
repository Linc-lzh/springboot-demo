package com.xs.dynamicthreadpool.component;

import com.xs.dynamicthreadpool.properties.ThreadPoolProperties;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

/***
 * 事件
 */
public class DtpEvent  extends ApplicationEvent {

    private ThreadPoolProperties properties;

    public DtpEvent(ThreadPoolProperties properties) {
        super(properties);
        this.properties = properties;
    }

    public ThreadPoolProperties getProperties() {
        return properties;
    }
}