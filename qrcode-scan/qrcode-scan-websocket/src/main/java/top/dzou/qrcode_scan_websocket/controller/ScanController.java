package top.dzou.qrcode_scan_websocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.dzou.qrcode_scan_websocket.model.ErrorCodeEnum;
import top.dzou.qrcode_scan_websocket.model.QrCodeStatus;
import top.dzou.qrcode_scan_websocket.model.Result;
import top.dzou.qrcode_scan_websocket.redis.RedisService;
import top.dzou.qrcode_scan_websocket.redis.prefix.QrCodeKey;
import top.dzou.qrcode_scan_websocket.service.QrCodeService;
import top.dzou.qrcode_scan_websocket.utils.UUIDUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xushu
 * @date 2024 下午2:28
 */
@/*Rest*/Controller
@Slf4j
@RequestMapping("api/")
public class ScanController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private Map<String,String> qrCodeMap = new HashMap<>();

    //app端使用处理
    @GetMapping("/doScan")
    @ResponseBody
    public Result doAppScanQrCode(@RequestParam("username")String username,
                                  @RequestParam("password")String password,
                                  @RequestParam("uuid")String uuid){
        QrCodeStatus status = redisService.get(QrCodeKey.UUID,uuid,QrCodeStatus.class);
        log.info(
                status.getStatus());
        if(status.getStatus().isEmpty()) return Result.error(ErrorCodeEnum.UUID_EXPIRED);
        switch (status){
            case NOT_SCAN:
                if(username.equals("dzou")&&password.equals("1234")){
                    redisService.set(QrCodeKey.UUID,uuid, QrCodeStatus.SCANNED);
                    simpMessagingTemplate.convertAndSend("/topic/ws","请确认");
                    return Result.success("请手机确认");
                }else{
                    return Result.error(ErrorCodeEnum.LOGIN_FAIL);
                }
            case SCANNED:
                return Result.error(ErrorCodeEnum.QRCODE_SCANNED);
            case VERIFIED:
                return Result.success("你已经确认过了");
        }
        return Result.error(ErrorCodeEnum.SEVER_ERROR);
    }

    //app端确认登录
    @GetMapping("/verify")
    @ResponseBody
    public Result verifyQrCode(@RequestParam("uuid")String uuid){
        String status = redisService.get(QrCodeKey.UUID,uuid,String.class);
        if(status.isEmpty()) return Result.error(ErrorCodeEnum.UUID_EXPIRED);
        redisService.set(QrCodeKey.UUID,uuid,QrCodeStatus.VERIFIED);
        simpMessagingTemplate.convertAndSend("/topic/ws","已经确认");
        return Result.success("确认成功");
    }

    /*@GetMapping("/getUuid")
    public Result<String> getUuid(){
        String uuid = UUIDUtil.uuid();
        redisService.set(QrCodeKey.UUID,uuid,QrCodeStatus.NOT_SCAN);
        return Result.success(uuid);
    }*/

    @GetMapping("/createQr")
    @ResponseBody
    public Result<String> createQrCode() throws IOException {
        String uuid = UUIDUtil.uuid();
        log.info(uuid);
        String qrCode = qrCodeService.createQrCode(uuid,200,200);
        qrCodeMap.put(qrCode,uuid);
        redisService.set(QrCodeKey.UUID,uuid,QrCodeStatus.NOT_SCAN);
        return Result.success(qrCode);
    }

    @GetMapping("/query")
    @ResponseBody
    public Result<String> queryIsScannedOrVerified(@RequestParam("img")String img){
        String uuid = qrCodeMap.get(img);
        QrCodeStatus s = redisService.get(QrCodeKey.UUID, uuid, QrCodeStatus.class);
        return Result.success(s.getStatus());
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }


    @GetMapping("/success")
    public String success(){
        return "success";
    }
}
