package com.tttqiu.library.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 返回Bitmap类型数据的请求
 */

public class BitmapRequest extends Request<Bitmap> {

    public BitmapRequest(String method,Boolean needMemoryCache,Boolean needDiskCache,
                         String address,Request.RequestListener<Bitmap> listener) {
        super(method,needMemoryCache,needDiskCache,address,listener);
    }

    @Override
    protected Bitmap parseResponse(byte[] data) {
        return BitmapFactory.decodeByteArray(data,0,data.length);
    }
}
