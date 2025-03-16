package com.xs.timeout.component;


import com.xs.timeout.domain.OverTimeOrder;
import com.xs.timeout.mapper.OrderMapper;
import com.xs.timeout.service.OrderService;
import com.xs.timeout.domain.OrderMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

@Component
public class DelayOrderDelayQueue {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    private static DelayQueue<OrderMessage> delayQueue = new DelayQueue<OrderMessage>();

    @PostConstruct
    public void init() throws Exception {
        /**初始化时加载数据库中需处理超时的订单**/
        List<OverTimeOrder> orderList = orderMapper.selectOverTimeOrder();
        for (int i = 0; i < orderList.size(); i++) {
            OrderMessage orderMessage = new OrderMessage
                    (orderList.get(i).getOrderId(), orderList.get(i).getCreateTime());
            this.addToOrderDelayQueue(orderMessage);
        }

        /**启动一个线程，去取延迟消息**/
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                OrderMessage message = null;
                while (true) {
                    try {
                        //等待队列中的元素达到delay时间后才会返回该元素
                        message = delayQueue.take();
                        //处理超时订单
                        orderService.closeOverTimeOrder(message.getOrderId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 加入延迟消息队列
     **/
    private boolean addToOrderDelayQueue(OrderMessage orderMessage) {
        return delayQueue.add(orderMessage);
    }


    /**
     * 从延迟队列中移除
     **/
    private void removeToOrderDelayQueue(OrderMessage orderMessage) {
        if (StringUtils.isEmpty(orderMessage)) {
            return;
        }
        for (Iterator<OrderMessage> iterator = delayQueue.iterator(); iterator.hasNext(); ) {
            OrderMessage queue = (OrderMessage) iterator.next();
            if (orderMessage.getOrderId().equals(queue.getOrderId())) {
                delayQueue.remove(queue);
            }
        }
    }
}

