package com.xs.dynamicthreadpool.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * auther: 徐庶
 */

public class DtpExecutor extends ThreadPoolExecutor {

    public DtpExecutor(int corePoolSize, int maximumPoolSize) {
        super(corePoolSize, maximumPoolSize, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));
    }

}
