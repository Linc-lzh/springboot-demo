package com.xs.dynamicthreadpool;

import com.xs.dynamicthreadpool.component.DtpEvent;
import com.xs.dynamicthreadpool.component.DtpRegistry;
import com.xs.dynamicthreadpool.properties.ThreadPoolProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/dtp")
public class DtpController implements ApplicationEventPublisherAware {

    ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/refresh")
    public String refresh(ThreadPoolProperties properties){
        applicationEventPublisher.publishEvent(new DtpEvent(properties));
        return "success!";
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher=applicationEventPublisher;
    }
}
