package com.tttqiu.library.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 返回String类型数据的请求
 */

public class StringRequest extends Request<String> {

    public StringRequest(String method,String address,Request.RequestListener<String> listener) {
        super(method,address,listener);
    }

    @Override
    protected String parseResponse(ByteArrayOutputStream byteArrayOutputStream) {
        String result=byteArrayOutputStream.toString();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
