package com.swf.attence.messageConfig;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private BlockingQueue<String> queue;
    private String producer;

    public Producer(BlockingQueue<String> queue, String producer) {
        this.queue = queue;
        if (null!=producer) {
            this.producer=producer;
        } else {
            this.producer=null;
        }
    }
    @Override
    public void run() {
        String replace = UUID.randomUUID().toString().replace("-", "");
        try{
            Thread.sleep(200);
            queue.put(producer+" :"+replace);
            System.out.println("producer "+producer+"\\:  "+replace+ "  "+Thread.currentThread());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
