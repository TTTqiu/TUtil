package com.tttqiu.library.request;

import com.tttqiu.library.network.Response;

import java.io.ByteArrayOutputStream;

/**
 * 网络请求抽象类
 * <p>
 * 继承此类自定义请求类型
 * <p>
 * 必须要复写parseResponse()抽象方法
 * <p>
 * 包含一个请求结果监听者RequestListener接口
 */

public abstract class Request<T> {

    private RequestListener<T> listener;
    private String url;

    public Request(String url, RequestListener<T> listener) {
        this.url = url;
        this.listener = listener;
    }

    /**
     * 回调监听者
     */
    public void callbackComplete(Response response) {
        T result =parseResponse(response.getByteArrayOutputStream());
        if (result != null) {
            listener.onComplete(result);
        } else {
            listener.onError(response.getExceptionMessage());
        }
    }

    public String getUrl() {
        return url;
    }

    /**
     * 解析响应数据
     */
    public abstract T parseResponse(ByteArrayOutputStream byteArrayOutputStream);

    /**
     * 请求结果监听者
     */
    public interface RequestListener<T> {
        void onComplete(T result);

        void onError(String errorMessage);
    }
}
