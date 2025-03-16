package com.xs.encry;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncryTests {
    String salt="x";    //随机

    /**
     * 不可逆
     * 1.抗碰撞性不强
     * 2.每次生成的密文都一样
     */
    @Test
    void MD5() {
        String password = "xushu";
        password=salt+password;
        // 使用Commons Codec进行MD5哈希
        String hashedPassword = DigestUtils.md5Hex(password);

        System.out.println("Salted MD5 Hash: " + hashedPassword);
    }



    @Test
    void MD5CollisionTest(){
        // 碰撞对                                               here↓
        String input1 = "TEXTCOLLBYfGiJUETHQ4hAcKSMd5zYpgqf1YRDhkmxHkhPWptrkoyz28wnI9V0aHeAuaKnak";
        String input2 = "TEXTCOLLBYfGiJUETHQ4hEcKSMd5zYpgqf1YRDhkmxHkhPWptrkoyz28wnI9V0aHeAuaKnak";


        // 计算MD5哈希
        String hash1 = DigestUtils.md5Hex(input1);
        String hash2 = DigestUtils.md5Hex(input2);

        // 输出结果
        System.out.println("MD5 Hash 1: " + hash1);
        System.out.println("MD5 Hash 2: " + hash2);
        if (hash1.equals(hash2)) {
            System.out.println("Collision detected!");
        } else {
            System.out.println("No collision.");
        }
    }


    /**
     * 64个字符
     * 抗碰撞性更强
     * 每次生成的密文都一样
     */
    @Test
    void SHA256(){
        String input = "xushu";

        // 计算加盐后的SHA-256哈希
        String saltedInput = input + salt;
        String sha256hex = DigestUtils.sha256Hex(saltedInput);

        // 输出结果
        System.out.println("Salt: " + salt);
        System.out.println("SHA-256 Hash with Salt: " + sha256hex);
        System.out.println(sha256hex.length());
    }


    /**
     * 60个字符长度
     * $2a$10$oKl4CB1GeF.6veuPOeKlXulyXYDiouj.u1dD4POGagZp5mfbQ3OHm
     * $2a$  = 版本号
     * 10$ = 加密强度
     *
     *
     */
    @Test
    void BCryptTest(){
        String password = "xushu";

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        System.out.println("BCrypt Hash: " + hashedPassword);

        boolean isMatch = passwordEncoder.matches(password, hashedPassword);
        System.out.println("Password Match: " + isMatch);
    }
}

