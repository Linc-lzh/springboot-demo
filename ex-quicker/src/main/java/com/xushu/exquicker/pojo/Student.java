package com.xushu.exquicker.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**

 *
 * @author xs
 */
@Data
@NoArgsConstructor
@ExcelTarget("student")
@Table(name="st_student")
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "s_id")
    private Integer sId;
    @Excel(name = "学生姓名", orderNum = "1", width = 20)
    private String name;
    @Excel(name = "年龄", orderNum = "2")
    private Integer age;
    @Excel(name = "家庭住址", orderNum = "8",width = 30)
    private String address;
    @Excel(name = "邮箱", orderNum = "7",width = 30)
    private String email;
    @Excel(name = "手机号码", orderNum = "5",width = 20)
    private String phone;
    @Excel(name = "身份证号码", orderNum = "6",width = 20)
    private String idcard;
    @Excel(name = "专业", orderNum = "1")
    private String major;
    @Excel(name = "年级", orderNum = "3")
    private String grade;
    @Excel(name = "班级", orderNum = "4")
    private String className;

    public Student(String name, Integer age, String address, String email, String phone, String idcard, String major, String grade, String className) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.idcard = idcard;
        this.major = major;
        this.grade = grade;
        this.className = className;
    }
}
