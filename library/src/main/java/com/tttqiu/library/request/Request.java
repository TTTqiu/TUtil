package com.tttqiu.library.request;

import android.util.Log;

import com.tttqiu.library.network.Response;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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

    public static final String GET = "GET";
    public static final String POST = "POST";
    private static final String DEFAULT_PARAM_ENCODE = "UTF-8";
    private RequestListener<T> listener;
    private String url;
    private String method;
    private Map<String, String> params = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();

    public Request(String method, String url, RequestListener<T> listener) {
        this.method = method;
        this.url = url;
        this.listener = listener;
    }

    /**
     * 添加header
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * 添加header
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * 添加请求参数
     */
    public void addParam(String key, String value) {
        params.put(key, value);
    }

    /**
     * 获得body参数字节数组
     */
    public byte[] getBody() {
        StringBuilder bodyString = new StringBuilder();
        byte[] body = null;
        if (params != null && params.size() > 0) {
            try {
                // 把参数拼接为"name=TTT&password=123456"形式的字符串
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    bodyString.append(URLEncoder.encode(entry.getKey(), DEFAULT_PARAM_ENCODE))
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(), DEFAULT_PARAM_ENCODE))
                            .append("&");
                }
                bodyString.deleteCharAt(bodyString.length() - 1);
                body = bodyString.toString().getBytes(DEFAULT_PARAM_ENCODE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return body;
    }

    /**
     * 回调监听者
     */
    public void callbackComplete(Response response) {
        if (response.getByteArrayOutputStream() != null) {
            T result = parseResponse(response.getByteArrayOutputStream());
            listener.onComplete(result);
        } else {
            listener.onError(response.getExceptionMessage());
        }
    }

    /**
     * 获取拼接了参数的url
     */
    public String getParamUrl() {
        StringBuilder paramUrl = new StringBuilder();
        paramUrl.append(url);
        if (params != null && params.size() > 0) {
            try {
                paramUrl.append("?");
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    paramUrl.append(URLEncoder.encode(entry.getKey(), DEFAULT_PARAM_ENCODE))
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(), DEFAULT_PARAM_ENCODE))
                            .append("&");
                }
                paramUrl.deleteCharAt(paramUrl.length() - 1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return paramUrl.toString();
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    /**
     * 解析响应数据
     */
    protected abstract T parseResponse(ByteArrayOutputStream byteArrayOutputStream);

    /**
     * 请求结果监听者
     */
    public interface RequestListener<T> {
        void onComplete(T result);

        void onError(String errorMessage);
    }
}
