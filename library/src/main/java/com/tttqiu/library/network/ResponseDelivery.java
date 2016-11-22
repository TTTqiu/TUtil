package com.tttqiu.library.network;

import android.os.Handler;
import android.os.Looper;

import com.tttqiu.library.request.Request;

/**
 * 响应结果投递者
 * <p>
 * 将响应结果投递到主线程处理
 */

class ResponseDelivery {

    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 将响应结果投递到主线程处理
     */
    void deliverResponse(final Request<?> request, final Response response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                request.callbackComplete(response);
            }
        });
    }
}
