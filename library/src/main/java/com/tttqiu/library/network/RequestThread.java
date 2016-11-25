package com.tttqiu.library.network;

import android.util.Log;

import com.tttqiu.library.request.Request;

import java.util.concurrent.BlockingQueue;

/**
 * 网络请求执行子线程
 * <p>
 * 循环从请求队列中取请求，取到了就交给请求执行者执行，取不到就阻塞住直到队列中有新的请求
 */

class RequestThread extends Thread {

    private BlockingQueue<Request<?>> queue;
    private ResponseDelivery mResponseDelivery;
    private RequestExecutor mRequestExecutor;

    RequestThread(BlockingQueue<Request<?>> queue,RequestExecutor requestExecutor,
                  ResponseDelivery responseDelivery) {
        this.queue = queue;
        mRequestExecutor=requestExecutor;
        mResponseDelivery=responseDelivery;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Request<?> request = queue.take();
                Log.d("TUtil_Network", "当前线程：" + Thread.currentThread().getName());
                Log.d("TUtil_Network", "queue size:" + queue.size());
                // 把request交给请求执行者执行请求并返回response
                Response response = mRequestExecutor.executeRequest(request);
                // 把response交给响应结果投递者投递到主线程处理
                mResponseDelivery.deliverResponse(request, response);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("TUtil_Network", "线程中断：" + Thread.currentThread().getName());
        }
    }

    /**
     * 停止线程
     */
    void quit() {
        interrupt();
    }
}
