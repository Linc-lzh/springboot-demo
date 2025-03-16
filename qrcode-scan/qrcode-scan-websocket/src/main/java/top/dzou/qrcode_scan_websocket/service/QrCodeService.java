package top.dzou.qrcode_scan_websocket.service;

import java.io.IOException;

/**
 * @author xushu
 * @date 2024 下午2:57
 */
public interface QrCodeService {

    String createQrCode(String content, int width, int height) throws IOException;
}
