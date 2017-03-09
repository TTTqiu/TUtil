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
    private DiskCacheUtil mDiskCacheUtil;
    private MemoryCacheUtil mMemoryCacheUtil;

    RequestThread(BlockingQueue<Request<?>> queue, RequestExecutor requestExecutor,
                  ResponseDelivery responseDelivery, DiskCacheUtil diskCacheUtil,
                  MemoryCacheUtil memoryCacheUtil) {
        this.queue = queue;
        mRequestExecutor = requestExecutor;
        mResponseDelivery = responseDelivery;
        mDiskCacheUtil = diskCacheUtil;
        mMemoryCacheUtil = memoryCacheUtil;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Request<?> request = queue.take();
                Log.d("TUtil_Network", "当前线程：" + Thread.currentThread().getName());
                Log.d("TUtil_Network", "queue size:" + queue.size());

                Response response;
                // 从缓存中获得数据
                response = getFromCache(request);
                if (response == null) {
                    // 如果没缓存，把request交给请求执行者执行请求并返回response
                    response = mRequestExecutor.executeRequest(request);
                }

                // 把response交给响应结果投递者投递到主线程处理
                mResponseDelivery.deliverResponse(request, response);

                // 按需要缓存
                cacheIfNeed(request, response);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("TUtil_Network", "线程中断：" + Thread.currentThread().getName());
        }
    }

    /**
     * 从缓存中获得数据
     * <p>
     * 先从内存中找，没找到再去文件中找
     *
     * @return 返回一个带数据的response
     */
    private Response getFromCache(Request request) {
        byte[] data = mMemoryCacheUtil.getByteFromMemory(request.getUrl());
        if (data != null) {
            Response response = new Response();
            response.setData(data);
            return response;
        } else {
            if (mDiskCacheUtil.isDiskCached(request)) {
                data = mDiskCacheUtil.getByteFromDisk(request.getUrl());
                Response response = new Response();
                response.setData(data);
                return response;
            } else return null;
        }
    }

    /**
     * 如果需要就缓存到内存或文件
     */
    private void cacheIfNeed(Request request, Response response) {
        // 如果需要就把数据缓存到内存
        if (request.isNeedMemoryCache()) {
            byte[] data = response.getData();
            if (data != null && data.length != 0) {
                mMemoryCacheUtil.putByteToMemory(request.getUrl(), data);
            }
        }

        // 如果需要就把数据缓存到文件
        if (request.isNeedDiskCache() && !mDiskCacheUtil.isDiskCached(request)) {
            byte[] data = response.getData();
            if (data != null && data.length != 0) {
                mDiskCacheUtil.putByteToDisk(request.getUrl(), data);
            }
        }
    }

    /**
     * 停止线程
     */
    void quit() {
        interrupt();
    }
}
