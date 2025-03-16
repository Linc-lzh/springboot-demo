package com.xs.timeout.component;


import com.xs.timeout.domain.OrderMessage;
import com.xs.timeout.domain.OverTimeOrder;
import com.xs.timeout.mapper.OrderMapper;
import com.xs.timeout.service.OrderService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

import static org.apache.rocketmq.client.producer.SendStatus.SEND_OK;

@Component
public class DelayOrderRocketMQ {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    public static final String PRODUCER_GROUP = "TimerMessageProducerGroup";
    /**
     * 加入延迟消息队列
     **/
    private boolean addToOrder(OrderMessage orderMessage) {
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
        Message message = new Message("OrderTopic", orderMessage.getOrderId().toString().getBytes());

        message.setDeliverTimeMs(System.currentTimeMillis() + 10_000L);
        // Send the message
        SendResult result = null;
        try {
            result = producer.send(message);
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        } catch (RemotingException e) {
            throw new RuntimeException(e);
        } catch (MQBrokerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.printf(result + "\n");
        return result.getSendStatus() == SEND_OK;
    }

}

