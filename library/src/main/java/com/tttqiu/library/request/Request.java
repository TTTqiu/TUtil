package com.tttqiu.library.request;

import android.os.Handler;
import android.os.Looper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/17.
 */

public abstract class Request<T> {

    private RequestListener<T> listener;
    private String address;
    private Handler handler=new Handler(Looper.getMainLooper());

    public Request(String address,RequestListener<T> listener){
        this.address=address;
        this.listener=listener;
    }

    public void send(){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                T response=parseResponse(is);
                callbackComplete(response);
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            callbackError(e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void callbackComplete(final T response){
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onComplete(response);
            }
        });
    }

    private void callbackError(final String errorMessage){
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onError(errorMessage);
            }
        });
    }

    protected abstract T parseResponse(InputStream is);

    public interface RequestListener<T>{
        void onComplete(T response);
        void onError(String errorMessage);
    }
}
