package com.xushu.exquicker.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import com.xushu.exquicker.dao.StudentRepository;
import com.xushu.exquicker.pojo.ExParams;
import com.xushu.exquicker.pojo.Student;
import com.xushu.exquicker.service.ExportService;
import com.xushu.exquicker.task.EasyPoiExportTask;
import com.xushu.exquicker.task.PoiExportTask;
import com.xushu.exquicker.utils.ExcelUtil;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 导出service
 *
 * @author xs
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class ExportServiceImpl implements ExportService {
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private StudentRepository studentDao;

    @Override
    public void exportWithSingleThread() {
        ExportParams exportParams = new ExportParams("历届学生信息表-All", "学生信息(0-" + studentDao.count() + ")", ExcelType.XSSF);
        List<Student> allStudents = studentDao.findAll();

        ExcelUtil.export(exportParams, Student.class,allStudents);
    }

    @SneakyThrows
    @Override
    public void exportWithMultiThread() {
        Integer studentTotal =(int)studentDao.count();
        // 单文件总数据量
        Integer pageSize = 250000;
        // 数据分页==导出excel文件数量
        int pageTotal = studentTotal % pageSize == 0 ? studentTotal / pageSize : studentTotal / pageSize + 1;
        log.info("导出数据总量：{}条, 预计导出文件数量：{}件",studentTotal, pageTotal);
        CountDownLatch countDownLatch = new CountDownLatch(pageTotal);
        long start = System.currentTimeMillis();
        for (int i = 1; i <= pageTotal; i++) {
            EasyPoiExportTask<Student> studentBoExportTask = new EasyPoiExportTask<Student>()
                    .setExportParams(new ExportParams("历届学生信息表-"+i, "学生信息("+((i-1)*pageSize)+"-"+(Math.min(i*pageSize,studentTotal))+")", ExcelType.XSSF))
                    .setData(studentDao.findAllByColumn(i, pageSize))
                    .setClazz(Student.class)
                    .setCountDownLatch(countDownLatch);
            threadPoolTaskExecutor.execute(studentBoExportTask);
            //new Thread(studentBoExportTask,"exportWithMultiThread-"+i).start();
        }
        countDownLatch.await();
        log.info("所有导出任务完成，总计耗时：{}ms",System.currentTimeMillis()-start);
    }

    @Override
    public void exportWithSingleThreadByPoi() {
        List<Student> allStudents = studentDao.findAll();
        ExParams exParams = new ExParams();
        exParams.setFileName("历届学生信息表-All-POI");
        exParams.setSheetName("学生信息");
        exParams.setDataNumsOfSheet(2<<15);
        ExcelUtil.exportBySxssf(exParams, Student.class, allStudents);
    }

    @SneakyThrows
    @Override
    public void exportWithMultiThreadByPoi() {
        Integer studentTotal = (int)studentDao.count();
        // 单文件总数据量
        Integer pageSize = 100000;
        // 数据分页==导出excel文件数量
        int pageTotal = studentTotal % pageSize == 0 ? studentTotal / pageSize : studentTotal / pageSize + 1;
        log.info("导出数据总量：{}条, 预计导出文件数量：{}件",studentTotal, pageTotal);
        CountDownLatch countDownLatch = new CountDownLatch(pageTotal);
        long start = System.currentTimeMillis();
        for (int i = 1; i <= pageTotal; i++) {
            ExParams exParams = new ExParams();
            exParams.setFileName("历届学生信息表-POI-"+i);
            exParams.setSheetName("学生信息");
            exParams.setDataNumsOfSheet(50000);
            PoiExportTask<Student> studentBoExTask = new PoiExportTask<Student>()
                    .setExParams(exParams)
                    .setData(studentDao.findAllByColumn(i, pageSize))
                    .setClazz(Student.class)
                    .setCountDownLatch(countDownLatch);
            threadPoolTaskExecutor.execute(studentBoExTask);
            //new Thread(studentBoExTask,"exportWithMultiThreadByPoi-"+i).start();
        }
        countDownLatch.await();
        log.info("所有导出任务完成，总计耗时：{}ms",System.currentTimeMillis()-start);
    }
}
