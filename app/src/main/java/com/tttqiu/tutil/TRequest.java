package com.tttqiu.tutil;

import com.tttqiu.library.request.Request;

import java.io.InputStream;

/**
 * Created by Administrator on 2016/11/22.
 */

public class TRequest extends Request<String>{

    public TRequest(String url, RequestListener<String> listener) {
        super(url, listener);
    }

    @Override
    public String parseResponse(InputStream inputStream) {
        return null;
    }
}
