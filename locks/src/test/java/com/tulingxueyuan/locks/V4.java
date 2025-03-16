package com.tulingxueyuan.locks;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.TimeUnit;

/***
 * @Author 徐庶
 * @Slogan 致敬大师，致敬未来的你
 *
 * redisson 实现分布式锁
 */
@SpringJUnitConfig(classes ={RedissonAutoConfiguration.class,RedisConfig.class} )
public class V4 {

    public static Long stock=1L;
    public static  final  String LOCK_KEY="lock::productId";

    @Autowired
    RedissonClient redisson;
    /**
     * 下单
     */
    public   void placeOrder() {
        RLock lock = redisson.getLock(LOCK_KEY);
        lock.lock();
        try {
                if (Thread.currentThread().getName().equals("Thread-1")) {
                    throw new RuntimeException("人为异常!");
                }
                if (stock > 0) {
                    Thread.sleep(100);      //模拟执行业务...
                    stock--;
                    System.out.println(Thread.currentThread().getName() + "秒杀成功");
                } else {
                    System.out.println(Thread.currentThread().getName() + "秒杀失败！库存不足");
                }
        } catch (Exception ex) {
            System.out.println(Thread.currentThread().getName() + "异常：");
            ex.printStackTrace();
        }
        finally {
            lock.unlock();
            System.out.println(stock);
        }
    }

    @Test
    public  void main() throws InterruptedException {
        for (int i=0;i<3;i++){
            Thread thread = new Thread(() -> {
                placeOrder();
            });
            thread.start();
            thread.join();
        }
    }
}
