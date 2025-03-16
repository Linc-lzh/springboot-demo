package com.xushu.exquicker.task;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import lombok.extern.slf4j.Slf4j;
import com.xushu.exquicker.utils.ExcelUtil;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;

/**

 *
 * @author xs
 */
@Slf4j
public class EasyPoiExportTask<T> implements Runnable {
    private ExportParams exportParams;
    private Collection<T> data;
    private Class<T> clazz;
    private CountDownLatch countDownLatch;

    @Override
    public void run() {
        log.info("{}-ExportTask is running", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        try {
            ExcelUtil.export(exportParams, clazz, data);
            log.info("{}-ExportTask is finished, cost {}ms", Thread.currentThread().getName(), System.currentTimeMillis() - start);
        }catch (OutOfMemoryError e) {
            log.error("{}-ExportTask is error,message={}", Thread.currentThread().getName(), e.getMessage());
        }finally {
            this.countDownLatch.countDown();
        }
    }

    public EasyPoiExportTask<T> setExportParams(ExportParams exportParams) {
        this.exportParams = exportParams;
        return this;
    }

    public EasyPoiExportTask<T> setData(Collection<T> data) {
        this.data = data;
        return this;
    }

    public EasyPoiExportTask<T> setClazz(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }

    public EasyPoiExportTask<T> setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        return this;
    }
}
