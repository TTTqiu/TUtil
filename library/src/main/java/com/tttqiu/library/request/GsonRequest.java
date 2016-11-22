package com.tttqiu.library.request;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 返回JsonBean类型数据的请求
 * <p>
 * 创建时要传入Bean类，如 Weather.class
 */

public class GsonRequest<T> extends Request<T> {

    private Class<T> clazz;

    public GsonRequest(String address, Class<T> clazz, RequestListener<T> listener) {
        super(address, listener);
        this.clazz = clazz;
    }

    @Override
    public T parseResponse(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonString = result.toString();
        Gson gson = new Gson();
        return gson.fromJson(jsonString, clazz);
    }
}
