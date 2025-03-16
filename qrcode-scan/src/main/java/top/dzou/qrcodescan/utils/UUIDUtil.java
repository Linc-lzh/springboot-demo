package top.dzou.qrcodescan.utils;

import java.util.UUID;

/**
 * @author xushu
 * @date 19-9-3 下午2:43
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
