package com.tttqiu.library.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tttqiu.library.network.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 返回Bitmap类型数据的请求
 */

public class BitmapRequest extends Request<Bitmap> {

    public BitmapRequest(String address, RequestListener<Bitmap> listener) {
        super(address,listener);
    }

    @Override
    public Bitmap parseResponse(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }
}
