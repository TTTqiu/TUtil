package com.tttqiu.library.imageCache;

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
 * 获取成功后，在内存和文件中分别保存一份
 */

 class GetBitmapFromHttpTask extends AsyncTask<Object, Void, Bitmap> {

    private Context context;
    private ImageView imageView;
    private String address;
    private int maxMemorySpace;
    private int maxDiskSpace;

    @Override
    protected Bitmap doInBackground(Object... params) {
        context = (Context) params[0];
        address = (String) params[1];
        imageView = (ImageView) params[2];
        maxMemorySpace = (int) params[3];
        maxDiskSpace = (int) params[4];
        Bitmap bitmap = null;

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
        Log.d("TUtil_Image", "NET：从网络读取:" + bitmap + "(" + address + ")");
        imageView.setImageAlpha(255);
        imageView.setImageBitmap(bitmap);
//        if (maxMemorySpace != 0) {
//            MemoryCacheUtil.putBitmapToMemory(address, bitmap);
//        }
//        if (maxDiskSpace != 0) {
//            DiskCacheUtil.putBitmapToDisk(context, address, bitmap);
//        }
    }
}
