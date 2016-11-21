package com.tttqiu.library.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/11/17.
 */

public class GsonRequest<T> extends Request<T> {

    private Class<T> clazz;

    public GsonRequest(String address, Class<T> clazz, RequestListener<T> listener) {
        super(address,listener);
        this.clazz=clazz;
    }

    @Override
    protected T parseResponse(InputStream is) {
        StringBuilder response=new StringBuilder();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line=br.readLine())!=null){
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonString=response.toString();

        Gson gson=new Gson();
        return gson.fromJson(jsonString,clazz);
    }
}
