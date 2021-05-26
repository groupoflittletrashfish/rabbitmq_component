package com.noname.producer.broker;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author ：liwuming
 * @date ：Created in 2021/5/25 15:49
 * @description ：
 * @modified By：
 * @version:
 */

@Slf4j
public class AsyncBaseQueue {

    private static final int THREAD_SIZE = Runtime.getRuntime().availableProcessors();

    private static final int QUEUE_SIZE = 10000;

    private static ExecutorService sendAsync = new ThreadPoolExecutor(THREAD_SIZE, THREAD_SIZE, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(QUEUE_SIZE), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("rabbitmq _client_async_sender");
            return t;
        }
    }, (r, executor) -> {
        //拒绝策略
        log.error("async sender is error rejected,runnable :{},executor:{}", r, executor);
    });

    public static void submit(Runnable runnable) {
        sendAsync.submit(runnable);
    }
}
