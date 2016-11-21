package com.tttqiu.library.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/11/17.
 */

public class BitmapRequest extends Request<Bitmap> {

    public BitmapRequest(String address, RequestListener<Bitmap> listener) {
        super(address,listener);
    }

    @Override
    protected Bitmap parseResponse(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }
}
