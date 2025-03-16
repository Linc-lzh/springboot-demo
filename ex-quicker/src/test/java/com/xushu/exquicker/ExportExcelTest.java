package com.xushu.exquicker;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.xushu.exquicker.pojo.Student;
import com.xushu.exquicker.utils.ExcelUtil;

import java.util.List;

/**

 *
 * @author 谭亚军 at 2022/5/3 16:15
 */
@Slf4j 
public class ExportExcelTest {
    private final static Integer DATA_MAX_SIZE = 1000000;
    private static List<Student> students;

    @BeforeAll
    public static void before(){
        students = Lists.newArrayList();
        long start = System.currentTimeMillis();
        for (int i = 0; i < DATA_MAX_SIZE; i++) {
            students.add(new Student("张三" + i,(int)(Math.random()*20+10),"北京市朝阳区",System.currentTimeMillis()+"@qq.com", "1008611", String.valueOf(System.currentTimeMillis()), "计算机系",(int)(Math.random()*5+2018)+"级",(int)(Math.random()*6+1)+"班"));
        }
        log.info("生成数据耗时：{}ms", System.currentTimeMillis() - start);
    }

    @Test
    public void testExportExcel() {
        long start = System.currentTimeMillis();
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        exportParams.setTitle("历年学生信息");
        exportParams.setSheetName("学生信息");
        exportParams.setFixedTitle(true);
        ExcelUtil.export(exportParams, Student.class, students);
        log.info("导出耗时：{}ms", System.currentTimeMillis() - start);
    }
}
