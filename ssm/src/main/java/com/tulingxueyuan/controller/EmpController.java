package com.tulingxueyuan.controller;

import com.tulingxueyuan.mapper.EmpMapper;
import com.tulingxueyuan.pojo.Emp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@RestController
@RequestMapping("/emp")
public class EmpController {

    @Autowired
    private EmpMapper empMapper;

    @RequestMapping("/all")
    public List<Emp> getAll(){
       return empMapper.selectAll();
    }

}
