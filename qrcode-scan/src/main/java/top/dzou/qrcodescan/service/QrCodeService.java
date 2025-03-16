package top.dzou.qrcodescan.service;

import java.io.IOException;

/**
 * @author xushu
 * @date 2024 下午2:57
 */
public interface QrCodeService {

    String createQrCode(String content,int width,int height) throws IOException;
}
