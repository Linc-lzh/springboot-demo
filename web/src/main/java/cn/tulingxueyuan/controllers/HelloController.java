package cn.tulingxueyuan.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@RestController
@RequestMapping("/hello")
public class HelloController {


    @RequestMapping("/world")
    public String sayHi() throws InterruptedException {
        Thread.sleep(10000);
        System.out.println("完成!");
        return "hello world!";
    }
}
