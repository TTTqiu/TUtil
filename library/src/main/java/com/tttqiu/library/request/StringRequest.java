package com.tttqiu.library.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 返回String类型数据的请求
 */

public class StringRequest extends Request<String> {

    public StringRequest(String method,Boolean needMemoryCache,Boolean needDiskCache,
                         String address,Request.RequestListener<String> listener) {
        super(method,needMemoryCache,needDiskCache,address,listener);
    }

    @Override
    protected String parseResponse(byte[] data) {
        String result=new String(data);
        data=null;
        return result;
    }
}
