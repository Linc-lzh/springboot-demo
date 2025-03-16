package com.xs.timeout.domain;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class OrderMessage implements Delayed {
    private Long orderId;
    private Date createTime;
    // 延迟时间三十分钟
    private long delayTime=60*30*1000;

    @Override
    public long getDelay(TimeUnit unit) {
        // 获取createTime 毫秒
        long diff = delayTime - (System.currentTimeMillis() - createTime.getTime());
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)) {
            return -1;
        }
        if (this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) {
            return 1;
        }
        return 0;
    }

    public OrderMessage(Long orderId, Date createTime) {
        this.orderId = orderId;
        this.createTime = createTime;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
