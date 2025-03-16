package cn.tulingxueyuan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class StudentController {



    public static void main(String[] args) throws InterruptedException {
        StudentController studentController = new StudentController();
        studentController.extracted();

    }

    private  void extracted() throws InterruptedException {

        Thread thread = new Thread(() -> saving(new String("北大")));
        Thread thread1 = new Thread(() -> saving(new String("清华")));
        Thread thread2 = new Thread(() -> saving(new String("清华")));
        thread.start();
        thread1.start();;
        thread2.start();;
        thread.join();
        thread1.join();
        thread2.join();
        System.out.println(values);
    }


    ConcurrentHashMap<String,Object> lock=new ConcurrentHashMap<>();
    // 考试
    @RequestMapping("/saving")
    public String saving(String school) {
        // 存在直接返回， 不存在执行自定义逻辑
        Object o = lock.computeIfAbsent(school, s -> new Object());
        // 常量池  全局
        synchronized (o) {
            System.out.println(school + "学生交卷");
            save(school);
            System.out.println(school + "学生交卷完成");
            return "ok";
        }
    }

    // 入学
    @RequestMapping("/saving")
    public String inschool(String school) {
        // 常量池  全局
        synchronized (school.intern()) {
            System.out.println(school + "学生入学");
            save(school);
            System.out.println(school + "学生入学完成");
            return "ok";
        }
    }


    static Map<String, Integer> values=new ConcurrentHashMap<>();


    private  void save(String account) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 同一个学校，数量+1
        if(values.containsKey(account)){
            values.put(account,values.get(account)+1);
        }else{
            values.put(account,1);
        }
    }

}
