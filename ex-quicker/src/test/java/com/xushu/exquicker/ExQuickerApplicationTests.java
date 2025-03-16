package com.xushu.exquicker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.xushu.exquicker.dao.StudentRepository;
import com.xushu.exquicker.pojo.Student;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ExQuickerApplicationTests {


    @Autowired
    StudentRepository repository;

    @Test
    void contextLoads() {

        List<Student> list=new ArrayList<>();

        for (int i = 0; i < 1000000; i++) {
            list.add(new Student("张三" + i,(int)(Math.random()*20+10),"北京市朝阳区",System.currentTimeMillis()+"@qq.com", "1008611", String.valueOf(System.currentTimeMillis()), "计算机系",(int)(Math.random()*5+2018)+"级",(int)(Math.random()*6+1)+"班"));
        }
        repository.saveAll(list);
    }

    @Test
    void test2() {
        List<Student> all = repository.findAll();
        Runtime runtime = Runtime.getRuntime();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory used by the list: " + memory + " bytes");


    }
}
