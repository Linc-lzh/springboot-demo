package com.baili.antiduplicate.controller;

import com.baili.antiduplicate.annotation.RepeatSubmit;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RepeatSubmit(limitType = RepeatSubmit.Type.TOKEN,lockTime = 10)
    @PostMapping("/saveCountInfo")
    public String saveCountInfo(String accountNo) {
        return "测试OK" + accountNo;
    }
}
