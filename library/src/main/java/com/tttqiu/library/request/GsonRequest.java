package com.tttqiu.library.request;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 返回JsonBean类型数据的请求
 * <p>
 * 创建时要传入Bean类，如 Weather.class
 */

public class GsonRequest<T> extends Request<T> {

    private Class<T> clazz;

    public GsonRequest(String method,Boolean shouldCache, Class<T> clazz,String address, RequestListener<T> listener) {
        super(method,shouldCache, address,listener);
        this.clazz = clazz;
    }

    @Override
    protected T parseResponse(byte[] data) {
        String jsonString = new String(data);
        data=null;
        Gson gson = new Gson();
        return gson.fromJson(jsonString, clazz);
    }
}
