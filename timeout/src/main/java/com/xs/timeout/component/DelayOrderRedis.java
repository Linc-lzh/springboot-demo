package com.xs.timeout.component;


import com.xs.timeout.domain.OrderMessage;
import com.xs.timeout.mapper.OrderMapper;
import com.xs.timeout.service.OrderService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static org.apache.rocketmq.client.producer.SendStatus.SEND_OK;

@Component
public class DelayOrderRedis  extends KeyExpirationEventMessageListener {

    public DelayOrderRedis(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String keyExpira = message.toString();
        System.out.println("监听到key：" + keyExpira + "已过期");
    }


}

