package com.xushu.exquicker.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import com.xushu.exquicker.pojo.ExParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 导出工具类
 *
 * @author xs
 */
@Slf4j
public class ExcelUtil {
    /**
     * 导出路径
     */
    public static String filePath  = "D:\\exquicker\\exquicker-excel\\";
    /**
     * xlsx格式文件最大行数
     */
    public static Integer SHEET_DATA_MAX_LIMIT_XLSX = 2 << 19;
    /**
     * xls格式文件最大行数
     */
    public static Integer SHEET_DATA_MAX_LIMIT_XLS = 2 << 15;

    /**
     * 下载Excel
     * @param fileName 文件名
     * @param workbook Excel对象
     */
    public static void downLoadExcel(String fileName, Workbook workbook) {
        if (workbook instanceof HSSFWorkbook) {
            fileName = fileName + ".xls";
        } else {
            fileName = fileName + ".xlsx";
        }


        File excelFile = new File(filePath + fileName);
        if(!excelFile.getParentFile().exists()) {
            excelFile.getParentFile().mkdirs();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(excelFile);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 使用EasyPOI导出Excel
     * @param entity 导出参数
     * @param pojoClass 数据类型
     * @param data 数据
     */
    public static void export(ExportParams entity, Class<?> pojoClass, Collection<?> data) {
        log.info("线程-【{}】-导出开始",Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        Workbook workbook = ExcelExportUtil.exportExcel(entity, pojoClass, data);
        if (workbook != null){
            downLoadExcel(entity.getTitle(), workbook);
        }
        log.info("线程-【{}】-导出结束-耗时:{}ms",Thread.currentThread().getName(), System.currentTimeMillis() - start);
    }

    /**
     * 使用POI导出Excel
     * @param exParams 导出参数
     * @param pojoClass 数据类型
     * @param data 数据
     */
    public static void exportBySxssf(ExParams exParams, Class<?> pojoClass, Collection<?> data) {
        log.info("线程-【{}】-导出开始",Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        SXSSFWorkbook workbook = new SXSSFWorkbook(2<<10);
        if (exParams.getIfOpenMultiSheet() && (data instanceof List || data instanceof Set)){
            if (exParams.getDataNumsOfSheet() == null || exParams.getDataNumsOfSheet() <= 0){
                throw new RuntimeException("单页导出数据量不合法");
            }
            if (exParams.getDataNumsOfSheet() > SHEET_DATA_MAX_LIMIT_XLSX){
                throw new RuntimeException("单页导出数据量过大");
            }
            List<?> list = new ArrayList<>(data);
            int sheetPageNum = list.size() % exParams.getDataNumsOfSheet() == 0 ? list.size() / exParams.getDataNumsOfSheet() : list.size() / exParams.getDataNumsOfSheet() + 1;
            for (int i = 0; i < sheetPageNum; i++) {
                createSheet(exParams.getSheetName()+"("+(i* exParams.getDataNumsOfSheet())+"-"+(Math.min(list.size(), (i + 1) * exParams.getDataNumsOfSheet()))+")"
                    , pojoClass
                    , list.subList(i* exParams.getDataNumsOfSheet(), Math.min(list.size(), (i + 1) * exParams.getDataNumsOfSheet()))
                    , workbook);
            }
        }else {
            createSheet(exParams.getSheetName(), pojoClass, data, workbook);
        }
        downLoadExcel(exParams.getFileName(), workbook);
        log.info("线程-【{}】-导出结束-耗时:{}ms",Thread.currentThread().getName(), System.currentTimeMillis() - start);
    }

    /**
     * 创建sheet(适合大数据量)
     * @param sheetName sheet名称
     * @param pojoClass 数据类型
     * @param data 数据
     * @param workbook 工作簿
     */
    public static void createSheet(String sheetName, Class<?> pojoClass, Collection<?> data, SXSSFWorkbook workbook) {
        SXSSFSheet sheet = workbook.createSheet(sheetName);
        Iterator<?> iterator = data.iterator();
        Field[] fields = pojoClass.getDeclaredFields();
        int rowNum = 0;
        while (iterator.hasNext()){
            Object item = iterator.next();
            Row row = sheet.createRow(rowNum);
            for (int i = 0; i < fields.length; i++) {
                Cell cell = row.createCell(i);
                if (rowNum == 0){
                    cell.setCellValue(fields[i].getName());
                }else {
                    try {
                        fields[i].setAccessible(true);
                        cell.setCellValue(String.valueOf(fields[i].get(item)));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            rowNum++;
        }
    }
}
