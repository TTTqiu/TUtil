package com.tttqiu.library.http;

import android.util.Log;

import com.tttqiu.library.request.Request;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Administrator on 2016/11/20.
 */

class RequestThread extends Thread{

    private BlockingQueue<Request<?>> queue;

    RequestThread(BlockingQueue<Request<?>> queue){
        this.queue=queue;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()){
                sleep(2000);
                Request<?> request=queue.take();
                Log.d("TUtil_Network","当前线程："+Thread.currentThread().getName());
                Log.d("TUtil_Network","queue size:"+queue.size());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("TUtil_Network","线程中断："+Thread.currentThread().getName());
        }
    }

    void quit(){
        interrupt();
    }
}
