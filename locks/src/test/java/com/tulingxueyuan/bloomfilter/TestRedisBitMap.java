package com.tulingxueyuan.bloomfilter;

import cn.hutool.core.date.DateUtil;
import com.tulingxueyuan.locks.RedisConfig;
import org.junit.jupiter.api.Test;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@SpringJUnitConfig(classes ={RedissonAutoConfiguration.class, RedisConfig.class} )
public class TestRedisBitMap {

    public static final String USER_SIGN_KEY = "sign:";

    @Autowired
    RedisTemplate redisTemplate;


    // 打卡、登录  计入
    @Test
    public void sign() {
        // 1.获取当前登录用户
        //Long userId = UserHolder.getUser().getId();
        Long userId =999L;  //暂时写死
        // 2.获取日期 使用hutool的日期时间工具-DateUtil
        Date date = DateUtil.date();
        // 3.拼接key
        String keySuffix = DateUtil.format(date, ":yyyyMM");
        // sign:999:202408
        String key = USER_SIGN_KEY + userId + keySuffix;
        // 记录本月当日前10天打卡
        for (int i=0;i<10;i++) {
            // 4.获取今天是本月的第几天
            int dayOfMonth = DateUtil.dayOfMonth(date) - i;
            // 5.写入Redis SETBIT key offset 1
            redisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
        }
    }

    @Test
    public void signCount() {
        // Long userId = UserHolder.getUser().getId();
        Long userId =999L;  //暂时写死
        // 2.获取日期 使用hutool的日期时间工具-DateUtil
        Date date = DateUtil.date();
        // 3.拼接key
        String keySuffix = DateUtil.format(date, ":yyyyMM");
        String key = USER_SIGN_KEY + userId + keySuffix;
        // 4.获取今天是本月的第几天
        int dayOfMonth =  DateUtil.dayOfMonth(date);
        // 5.获取本月截止今天为止的所有的签到记录，返回的是一个十进制的数字 BITFIELD sign:999:202408 GET u18 0
        List<Long> result = redisTemplate.opsForValue().bitField(
                key,
                BitFieldSubCommands.create()
                        .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0)
        );
        // 15号
        // 000001111111111    1023
        System.out.println(result);
        if (result == null || result.isEmpty()) {
            // 没有任何签到结果
            System.out.println("未签到");
        }
        //num为0，直接返回0
        Long num = result.get(0);
        if (num == null || num == 0) {
            System.out.println("未签到");
        }
        // 6.循环遍历
        int count = 0;
        /*
         * 7 = 0111  & 0001  = 0001 ==1
         * 7 >>> 1 = 3  =0011 & 0001  = 0001 ==1
         * 3 >>> 1 = 1  =0001 & 0001  = 0001 ==1
         * 1 >>> 1 = 0  =0 & 0001  = 0 !=1
         * */
        while (true) {
            // 6.1.让这个数字与1做与运算，得到数字的最后一个bit位  // 判断这个bit位是否为0
            if ((num & 1) == 0) {
                // 如果为0，说明未签到，结束
                break;
            }else {
                // 如果不为0，说明已签到，计数器+1
                count++;
            }
            // 把数字右移一位，抛弃最后一个bit位，继续下一个bit位
            num >>>= 1;
        }
        System.out.println("连续签到天数："+count);
    }
}
