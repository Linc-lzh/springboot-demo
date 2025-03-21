package top.dzou.qrcodescan.redis;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author xushu
 * @date 19-9-2 下午11:32
 */
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class BasePrefix implements KeyPrefix {

    @NonNull
    private String prefix;//前缀
    private int expireSeconds;//过期时间，默认永久


    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
