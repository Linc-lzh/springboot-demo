package com.xs.dynamicthreadpool.utils;

import java.util.concurrent.ArrayBlockingQueue;

public class QueueType {


    public static Object getInstance(String queueType, int queueSize) {
        // if

        return new ArrayBlockingQueue<>(queueSize);
    }
}
