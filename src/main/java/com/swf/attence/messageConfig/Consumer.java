package com.swf.attence.messageConfig;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/**
 * @author : white.hou
 * @description : 生产着消费者
 * @date: 2019/3/31_15:42
 */
public class Consumer implements Runnable {

    private BlockingQueue<String> queue;
    private String cosumer;

    public Consumer(BlockingQueue<String> queue, String cosumer) {
        this.queue = queue;
        if (null!=cosumer) {
            this.cosumer=cosumer;
        } else {
            this.cosumer=null;
        }
    }
    @Override
    public void run() {
        try{
            String take = queue.take();
            System.out.println("consumer "+cosumer+"\\:  "+take+ "  "+Thread.currentThread());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
