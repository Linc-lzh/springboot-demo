package top.dzou.qrcode_scan_websocket.redis.prefix;

import lombok.NonNull;
import top.dzou.qrcode_scan_websocket.redis.BasePrefix;

/**
 * @author xushu
 * @date 2024 下午2:43
 */
public class QrCodeKey extends BasePrefix {
    public QrCodeKey(@NonNull String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }
    public QrCodeKey(String prefix){super(prefix);}

    public static QrCodeKey UUID = new QrCodeKey("uuid",300);
}
