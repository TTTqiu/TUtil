package com.tttqiu.library.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 返回Bitmap类型数据的请求
 */

public class BitmapRequest extends Request<Bitmap> {

    public BitmapRequest(String method,String address, RequestListener<Bitmap> listener) {
        super(method,address,listener);
    }

    @Override
    protected Bitmap parseResponse(ByteArrayOutputStream byteArrayOutputStream) {
        Bitmap result=BitmapFactory.decodeByteArray(
                byteArrayOutputStream.toByteArray(),0,byteArrayOutputStream.size());
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
