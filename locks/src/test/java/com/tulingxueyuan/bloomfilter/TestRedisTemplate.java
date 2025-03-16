package com.tulingxueyuan.bloomfilter;

import com.tulingxueyuan.locks.RedisConfig;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringJUnitConfig(classes ={RedissonAutoConfiguration.class, RedisConfig.class} )
public class TestRedisTemplate {

    String KEY_NAME="pro_code_key";

    @Autowired
    RedisTemplate redisTemplate;

    public  void main() {
        int n = 1000;  // 预期插入的元素数量
        double p = 0.01;  // 容错率

        int m = calculateOptimalSize(n, p);
        int k = calculateOptimalHashFunctions(n, m);

        System.out.println("Optimal size (m): " + m);
        System.out.println("Optimal number of hash functions (k): " + k);

    }

    private  int calculateOptimalSize(int n, double p) {
        double m = -(n * Math.log(p)) / Math.pow(Math.log(2), 2);
        return (int) m;
    }

    private  int calculateOptimalHashFunctions(int n, int m) {
        double k = (m / n) * Math.log(2);
        return (int) Math.ceil(k);
    }

    // 添加
    public void addToWhitelist(String value,int m,int k) throws NoSuchAlgorithmException {

        // 如果k=3 就hash3次   ，每次取模m值， 这里不做实现了
        long md5Hash = (long) (hashToPositiveInt(value, "MD5") % Math.pow(2, m));
        long sha1Hash = (long) (hashToPositiveInt(value, "SHA-1") % Math.pow(2, m));
        long sha256Hash = (long) (hashToPositiveInt(value, "SHA-256") % Math.pow(2, m));
        redisTemplate.opsForValue().setBit(KEY_NAME, md5Hash, true);
        redisTemplate.opsForValue().setBit(KEY_NAME, sha1Hash, true);
        redisTemplate.opsForValue().setBit(KEY_NAME, sha256Hash, true);
    }

    // 检查是否存在
    public boolean checkInWhitelist(String value,int m,int k) throws NoSuchAlgorithmException {
        // 如果k=3 就hash3次   ，每次取模m值， 这里不做实现了
        long md5Hash = (long) (hashToPositiveInt(value, "MD5") % Math.pow(2, 23));
        long sha1Hash = (long) (hashToPositiveInt(value, "SHA-1") % Math.pow(2, 23));
        long sha256Hash = (long) (hashToPositiveInt(value, "SHA-256") % Math.pow(2, 23));
        return Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(KEY_NAME, md5Hash)) &&
                Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(KEY_NAME, sha1Hash)) &&
                Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(KEY_NAME, sha256Hash));
    }

    public   int hashToPositiveInt(String input, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        // 使用BigInteger将字节数组表示的哈希值转换为正整数
        BigInteger bigIntegerHash = new BigInteger(1, hashBytes);
        // 获取正整数表示的哈希值
        return bigIntegerHash.intValue();

    }
}
