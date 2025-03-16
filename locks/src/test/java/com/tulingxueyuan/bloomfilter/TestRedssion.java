package com.tulingxueyuan.bloomfilter;

import com.tulingxueyuan.locks.RedisConfig;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringJUnitConfig(classes ={RedissonAutoConfiguration.class, RedisConfig.class} )
public class TestRedssion {


    @Autowired
    RedissonClient redisson;

    /**
     * 下单
     */
    public void testRedisson() {
        RBloomFilter<String> bloomFilter=redisson.getBloomFilter("pro_code_key");
        bloomFilter.tryInit(1000,0.03);

        for (int i=0;i<1000;i++){
            bloomFilter.add("xushu "+i);
        }

        System.out.println("'xushu 1'是否存在："+bloomFilter.contains("xushu "+1));
        System.out.println("'zhuge'是否存在："+bloomFilter.contains("zhuge"));
        System.out.println("预计插入数量："+bloomFilter.getExpectedInsertions());
        System.out.println("容错率："+bloomFilter.getFalseProbability());
        System.out.println("hash函数的个数："+bloomFilter.getHashIterations());
        System.out.println("插入对象的个数："+bloomFilter.count());
    }




}