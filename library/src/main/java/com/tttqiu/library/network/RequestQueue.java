package com.tttqiu.library.network;

import android.util.Log;

import com.tttqiu.library.request.Request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 请求队列控制类
 * <p>
 * 队列使用LinkedBlockingQueue
 * <p>
 * 默认线程池大小为5个
 */

public class RequestQueue {

    public static final int DEFAULT_THREAD_NUM = 5;
    private BlockingQueue<Request<?>> queue;
    private int threadNum;
    private RequestThread[] mRequestThreads;

    public RequestQueue(int threadNum) {
        queue = new LinkedBlockingQueue<Request<?>>();
        this.threadNum = threadNum > 0 ? threadNum : DEFAULT_THREAD_NUM;
    }

    /**
     * 加入request到队列中
     */
    public void addRequest(Request<?> request) {
        if (!queue.contains(request)) {
            queue.add(request);
        }
        Log.d("TUtil_Network", "加入队列：" + request);
    }

    /**
     * 新开所有请求线程
     * <p>
     * 如果已有运行中的请求线程，先全部停止
     */
    public void start() {
        stop();
        mRequestThreads = new RequestThread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            mRequestThreads[i] = new RequestThread(queue);
            mRequestThreads[i].start();
        }
        Log.d("TUtil_Network", "新开所有请求线程");
    }

    /**
     * 停止所有请求线程
     */
    public void stop() {
        if (mRequestThreads != null) {
            for (int i = 0; i < threadNum; i++) {
                mRequestThreads[i].quit();
            }
        }
        mRequestThreads = null;
        Log.d("TUtil_Network", "停止所有请求线程");
    }

    /**
     * 清空请求列表
     */
    public void clear() {
        if (queue != null && queue.size() > 0) {
            queue.clear();
            Log.d("TUtil_Network", "清空队列");
        }
    }
}
