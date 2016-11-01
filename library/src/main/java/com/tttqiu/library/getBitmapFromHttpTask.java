package com.tttqiu.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 从网络获取bitmap，并加载到传入的ImageView中
 */

public class GetBitmapFromHttpTask extends AsyncTask<Object,Void,Bitmap>{

    private Context context;
    private ImageView imageView;
    private String address;

    @Override
    protected Bitmap doInBackground(Object... params) {
        context=(Context) params[0];
        address=(String) params[1];
        imageView=(ImageView) params[2];
        Bitmap bitmap=null;

        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.d("TUtil", "从网络读取:" + bitmap+"("+address+")");
        imageView.setImageBitmap(bitmap);
        MemoryCacheUtil.putBitmapToMemory(address,bitmap);
        DiskCacheUtil.putBitmapToDisk(context,address,bitmap);
    }
}
