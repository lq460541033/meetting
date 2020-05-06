package com.swf.attence.messageConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Tester {
    public static void main(String[] args) {
        LinkedBlockingQueue<String> strings = new LinkedBlockingQueue<>(10);
        ExecutorService service = Executors.newCachedThreadPool();
        for (int  i=0;i<6;i++){
            service.submit(new Consumer(strings,"消费者A：  "+i));
        }
        for (int  i=0;i<6;i++){
            service.submit(new Producer(strings,"生产者甲：  "+i));
        }
        service.shutdown();
    }
}
