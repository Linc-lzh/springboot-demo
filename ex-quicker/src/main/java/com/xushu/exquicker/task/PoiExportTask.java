package com.xushu.exquicker.task;

import lombok.extern.slf4j.Slf4j;
import com.xushu.exquicker.pojo.ExParams;
import com.xushu.exquicker.utils.ExcelUtil;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;

/**

 *
 * @author xs
 */
@Slf4j
public class PoiExportTask<T> implements Runnable {
    private ExParams exParams;
    private Collection<T> data;
    private Class<T> clazz;
    private CountDownLatch countDownLatch;

    @Override
    public void run() {
        log.info("{}-PoiExportTask is running", Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        try {
            ExcelUtil.exportBySxssf(exParams, clazz, data);
            log.info("{}-PoiExportTask is finished, cost {}ms", Thread.currentThread().getName(), System.currentTimeMillis() - start);
        }catch (OutOfMemoryError e) {
            log.error("{}-PoiExportTask is error,message={}", Thread.currentThread().getName(), e.getMessage());
        }finally {
            this.countDownLatch.countDown();
        }
    }

    public PoiExportTask<T> setExParams(ExParams exParams) {
        this.exParams = exParams;
        return this;
    }

    public PoiExportTask<T> setData(Collection<T> data) {
        this.data = data;
        return this;
    }

    public PoiExportTask<T> setClazz(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }

    public PoiExportTask<T> setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        return this;
    }
}
